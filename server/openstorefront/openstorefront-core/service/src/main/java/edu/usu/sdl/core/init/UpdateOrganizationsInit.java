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
package edu.usu.sdl.core.init;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Extracts organizations from the data
 *
 * @author dshurtleff
 */
public class UpdateOrganizationsInit
		extends ApplyOnceInit
{

	private static final Logger log = Logger.getLogger(UpdateOrganizationsInit.class.getName());

	public UpdateOrganizationsInit()
	{
		super("Update Organizations");
	}

	@Override
	protected String internalApply()
	{
		boolean success = false;
		try {
			service.getOrganizationService().extractOrganizations();
			success = true;
		} catch (Exception e) {
			log.log(Level.WARNING, "Failed extracting Organizations", e);
		}
		return Boolean.toString(success);
	}

}
