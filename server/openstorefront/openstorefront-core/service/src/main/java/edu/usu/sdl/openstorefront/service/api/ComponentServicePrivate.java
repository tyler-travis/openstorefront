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
package edu.usu.sdl.openstorefront.service.api;

import com.atlassian.jira.rest.client.api.domain.Issue;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegrationConfig;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.view.RequiredForComponent;
import edu.usu.sdl.openstorefront.service.ServiceInterceptor;
import edu.usu.sdl.openstorefront.service.TransactionInterceptor;

/**
 *
 * @author dshurtleff
 */
public interface ComponentServicePrivate
{

	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentAttribute(ComponentAttribute attribute, boolean updateLastActivity);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void doSaveComponentTag(ComponentTag tag, boolean updateLastActivity);

	@ServiceInterceptor(TransactionInterceptor.class)
	public RequiredForComponent doSaveComponent(RequiredForComponent component);

	@ServiceInterceptor(TransactionInterceptor.class)
	public void doDeactivateComponent(String componentId);

	/**
	 * This will handle all the mapping for the component attributes based on a
	 * config and jira issue. Then this will sync index and last activity
	 *
	 * @param issue
	 * @param integrationConfig
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void mapComponentAttributes(Issue issue, ComponentIntegrationConfig integrationConfig);

	/**
	 * This will process the queue and apply component updates. It shouldn't be
	 * ran concurrently. It will block other threads on the same jvm.
	 */
	public void processComponentUpdates();

}
