/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.security;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * This is used to login based on Request Header info
 *
 * @author dshurtleff
 */
public class HeaderRealm
		extends AuthorizingRealm
{

	private static final Logger log = Logger.getLogger(HeaderRealm.class.getName());

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
	{
		UserContext userContext = (UserContext) principals.getPrimaryPrincipal();
		HeaderAuthToken headerAuthToken = new HeaderAuthToken();
		headerAuthToken.setUserContext(userContext);
		return populateAccount(headerAuthToken);
	}

	@Override
	public boolean supports(AuthenticationToken token)
	{
		return (token instanceof HeaderAuthToken);
	}

	@Override
	protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException
	{
		//just let this through
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
	{
		HeaderAuthToken headerAuthToken = (HeaderAuthToken) token;
		return populateAccount(headerAuthToken);
	}

	private HeaderAccount populateAccount(HeaderAuthToken headerAuthToken)
	{
		HeaderAccount headerAccount = new HeaderAccount();

		UserContext userContext = headerAuthToken.getUserContext();
		boolean admin = false;
		if (userContext == null) {
			ServiceProxy serviceProxy = new ServiceProxy();

			if (StringUtils.isBlank(headerAuthToken.getUsername())) {
				//They shouldn't get here unless open am is not configured
				throw new AuthenticationException("Unable to login.  No credentials passed.  Auth filter not set.");
			}

			UserProfile userProfile = new UserProfile();
			userProfile.setUsername(headerAuthToken.getUsername());
			userProfile.setFirstName(headerAuthToken.getFirstname());
			userProfile.setLastName(headerAuthToken.getLastname());
			userProfile.setOrganization(headerAuthToken.getOrganization());
			userProfile.setEmail(headerAuthToken.getEmail());
			userProfile.setExternalGuid(headerAuthToken.getGuid());

			if (StringUtils.isNotBlank(headerAuthToken.getGroup())
					&& StringUtils.isNotBlank(headerAuthToken.getAdminGroupName())) {
				admin = headerAuthToken.getGroup().contains(headerAuthToken.getAdminGroupName());
			}
			userContext = serviceProxy.getUserService().handleLogin(userProfile, headerAuthToken.getRequest(), admin);
		} else {
			admin = userContext.isAdmin();
		}
		headerAccount.setCredentials(userContext);
		headerAccount.getSimplePrincipals().add(userContext, "Open Am Header User");
		if (admin) {
			headerAccount.getRoles().add(SecurityUtil.ADMIN_ROLE);
		}

		return headerAccount;
	}

	public static boolean isUsingHeaderRealm()
	{
		boolean headerRealm = false;
		org.apache.shiro.mgt.SecurityManager securityManager = SecurityUtils.getSecurityManager();
		if (securityManager instanceof DefaultWebSecurityManager) {
			DefaultWebSecurityManager webSecurityManager = (DefaultWebSecurityManager) securityManager;
			for (Realm realm : webSecurityManager.getRealms()) {
				if (realm instanceof HeaderRealm) {
					headerRealm = true;
					break;
				}
			}
		}
		return headerRealm;
	}

	public static boolean handleHeaderLogin(HttpServletRequest request)
	{
		boolean loginSuccessful = false;

		final String STUB_HEADER = "X_STUBHEADER_X";
		if (isUsingHeaderRealm()) {
			HeaderAuthToken headerAuthToken = new HeaderAuthToken();
			headerAuthToken.setRequest(request);
			headerAuthToken.setAdminGroupName(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_ADMIN_GROUP));
			headerAuthToken.setEmail(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_EMAIL, STUB_HEADER)));
			headerAuthToken.setFirstname(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_FIRSTNAME, STUB_HEADER)));

			Enumeration<String> groupValues = request.getHeaders(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_GROUP, STUB_HEADER));
			StringBuilder group = new StringBuilder();
			while (groupValues.hasMoreElements()) {
				group.append(groupValues.nextElement());
				group.append(" | ");
			}

			headerAuthToken.setGroup(group.toString());
			headerAuthToken.setGuid(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_LDAPGUID, STUB_HEADER)));
			headerAuthToken.setLastname(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_LASTNAME, STUB_HEADER)));
			headerAuthToken.setOrganization(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_ORGANIZATION, STUB_HEADER)));
			headerAuthToken.setUsername(request.getHeader(PropertiesManager.getValue(PropertiesManager.KEY_OPENAM_HEADER_USERNAME, STUB_HEADER)));

			try {
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.login(headerAuthToken);
				loginSuccessful = true;
			} catch (Exception ex) {
				log.log(Level.WARNING, "Login failed on header; Check configuration, if needed", ex);
			}
		}

		return loginSuccessful;
	}

}
