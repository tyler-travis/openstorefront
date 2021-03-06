/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import static edu.usu.sdl.openstorefront.core.entity.LookupEntity.newLookup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Defines the reports")
public class ReportType
		extends LookupEntity
{

	public static final String USAGE = "USAGE";
	public static final String LINK_VALIDATION = "LINKVALID";
	public static final String COMPONENT = "COMPONENT";
	public static final String USER = "USER";
	public static final String ORGANIZATION = "ORGANIZATION";
	public static final String COMPONENT_ORGANIZATION = "CMPORG";
	public static final String SUBMISSION = "SUBMISSION";
	public static final String CATEGORY_COMPONENT = "CATCOMP";
	public static final String COMPONENT_DETAIL = "TYPECOMP";

	private boolean adminOnly;
	private List<String> supportedFormats = new ArrayList<>();

	public ReportType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(USAGE, newLookup(ReportType.class, USAGE, "Usage", "Reports on overall application usage."));
		codeMap.put(LINK_VALIDATION, newLookup(ReportType.class, LINK_VALIDATION, "Link Validation", "Reports on potentially broken external url links."));
		codeMap.put(COMPONENT, newLookup(ReportType.class, COMPONENT, "Component", "Reports on component statistic and usage."));
		codeMap.put(USER, newLookup(ReportType.class, USER, "User", "Reports on users and thier usage of the application."));
		codeMap.put(ORGANIZATION, newLookup(ReportType.class, ORGANIZATION, "User Organization", "Reports on the user organizations in the appliaction and thier usage."));
		codeMap.put(COMPONENT_ORGANIZATION, newLookup(ReportType.class, COMPONENT_ORGANIZATION, "Component by Organization", "Reports on components that belong to an organization"));
		codeMap.put(SUBMISSION, newLookup(ReportType.class, SUBMISSION, "Submissions", "Reports on component submissions."));
		codeMap.put(CATEGORY_COMPONENT, newLookup(ReportType.class, CATEGORY_COMPONENT, "Components by Category", "Reports on components in a category."));
		codeMap.put(COMPONENT_DETAIL, newLookup(ReportType.class, COMPONENT_DETAIL, "Component Detail", "Exports component details"));

		//update metadata
		((ReportType) codeMap.get(USAGE)).setAdminOnly(true);
		((ReportType) codeMap.get(LINK_VALIDATION)).setAdminOnly(true);
		((ReportType) codeMap.get(USER)).setAdminOnly(true);
		((ReportType) codeMap.get(SUBMISSION)).setAdminOnly(true);
		((ReportType) codeMap.get(ORGANIZATION)).setAdminOnly(true);

		//Add  formats
		for (LookupEntity lookupEntity : codeMap.values()) {
			((ReportType) lookupEntity).getSupportedFormats().add(ReportFormat.CSV);
		}
		((ReportType) codeMap.get(COMPONENT_DETAIL)).getSupportedFormats().add(ReportFormat.HTML);

		return codeMap;
	}

	public boolean getAdminOnly()
	{
		return adminOnly;
	}

	public void setAdminOnly(boolean adminOnly)
	{
		this.adminOnly = adminOnly;
	}

	public List<String> getSupportedFormats()
	{
		return supportedFormats;
	}

	public void setSupportedFormats(List<String> supportedFormats)
	{
		this.supportedFormats = supportedFormats;
	}

}
