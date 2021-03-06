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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.core.api.AlertService;
import edu.usu.sdl.openstorefront.core.api.AsyncService;
import edu.usu.sdl.openstorefront.core.api.AttributeService;
import edu.usu.sdl.openstorefront.core.api.ComponentService;
import edu.usu.sdl.openstorefront.core.api.LookupService;
import edu.usu.sdl.openstorefront.core.api.OrganizationService;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.PluginService;
import edu.usu.sdl.openstorefront.core.api.ReportService;
import edu.usu.sdl.openstorefront.core.api.SearchService;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.SystemService;
import edu.usu.sdl.openstorefront.core.api.UserService;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.service.api.AttributeServicePrivate;
import edu.usu.sdl.openstorefront.service.api.ComponentServicePrivate;
import edu.usu.sdl.openstorefront.service.api.PluginServicePrivate;
import edu.usu.sdl.openstorefront.service.api.UserServicePrivate;
import java.util.Objects;

/**
 * Entry point to the service layer; Expecting one Service Proxy per thread. Not
 * thread Safe...there needs to be a new db connection per thread.
 *
 * @author dshurtleff
 */
public class ServiceProxy
		implements Service
{

	protected PersistenceService persistenceService = new OrientPersistenceService();
	private LookupService lookupService;
	private AttributeService attributeService;
	private AttributeServicePrivate attributeServicePrivate;
	private ComponentService componentService;
	private ComponentServicePrivate componentServicePrivate;
	private SearchService searchService;
	private UserService userService;
	private UserServicePrivate userServicePrivate;
	private SystemService systemService;
	private AlertService alertService;
	private ReportService reportService;
	private OrganizationService organizationService;
	private PluginService pluginService;
	private PluginServicePrivate pluginServicePrivate;

	public ServiceProxy()
	{
	}

	public ServiceProxy(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public static ServiceProxy getProxy()
	{
		return new ServiceProxy();
	}

	@Override
	public PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	@Override
	public PersistenceService getNewPersistenceService()
	{
		return new OrientPersistenceService();
	}

	@Override
	public LookupService getLookupService()
	{
		if (lookupService == null) {
			lookupService = DynamicProxy.newInstance(new LookupServiceImpl());
		}
		return lookupService;
	}

	@Override
	public AttributeService getAttributeService()
	{
		if (attributeService == null) {
			attributeService = DynamicProxy.newInstance(new AttributeServiceImpl());
		}
		return attributeService;
	}

	@Override
	public ComponentService getComponentService()
	{
		if (componentService == null) {
			componentService = DynamicProxy.newInstance(new ComponentServiceImpl());
		}
		return componentService;
	}

	public ComponentServicePrivate getComponentServicePrivate()
	{
		if (componentServicePrivate == null) {
			componentServicePrivate = DynamicProxy.newInstance(new ComponentServiceImpl());
		}
		return componentServicePrivate;
	}

	@Override
	public SearchService getSearchService()
	{
		if (searchService == null) {
			searchService = DynamicProxy.newInstance(new SearchServiceImpl());
		}
		return searchService;
	}

	@Override
	public UserService getUserService()
	{
		if (userService == null) {
			userService = DynamicProxy.newInstance(new UserServiceImpl());
		}
		return userService;
	}

	public UserServicePrivate getUserServicePrivate()
	{
		if (userService == null) {
			userServicePrivate = DynamicProxy.newInstance(new UserServiceImpl());
		}
		return userServicePrivate;
	}

	@Override
	public SystemService getSystemService()
	{
		if (systemService == null) {
			systemService = DynamicProxy.newInstance(new SystemServiceImpl());
		}
		return systemService;
	}

	@Override
	public AlertService getAlertService()
	{
		if (alertService == null) {
			alertService = DynamicProxy.newInstance(new AlertServiceImpl());
		}
		return alertService;
	}

	@Override
	public ReportService getReportService()
	{
		if (reportService == null) {
			reportService = DynamicProxy.newInstance(new ReportServiceImpl());
		}
		return reportService;
	}

	@Override
	public OrganizationService getOrganizationService()
	{
		if (organizationService == null) {
			organizationService = DynamicProxy.newInstance(new OrganizationServiceImpl());
		}
		return organizationService;
	}

	@Override
	public PluginService getPluginService()
	{
		if (pluginService == null) {
			pluginService = DynamicProxy.newInstance(new PluginServiceImpl());
		}
		return pluginService;
	}

	public PluginServicePrivate getPluginServicePrivate()
	{
		if (pluginServicePrivate == null) {
			pluginServicePrivate = DynamicProxy.newInstance(new PluginServiceImpl());
		}
		return pluginServicePrivate;
	}

	public AttributeServicePrivate getAttributeServicePrivate()
	{
		if (attributeServicePrivate == null) {
			attributeServicePrivate = DynamicProxy.newInstance(new AttributeServiceImpl());
		}
		return attributeServicePrivate;
	}

	@Override
	public <T extends AsyncService> T getAsyncProxy(T originalProxy)
	{
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setAllowMultiple(true);
		taskRequest.setName("Aync Service Call");
		return getAsyncProxy(originalProxy, taskRequest);
	}

	@Override
	public <T extends AsyncService> T getAsyncProxy(T originalProxy, TaskRequest taskRequest)
	{
		Objects.requireNonNull(originalProxy, "Original Service is required");
		T asyncService = AsyncProxy.newInstance(originalProxy, taskRequest);
		return asyncService;
	}

}
