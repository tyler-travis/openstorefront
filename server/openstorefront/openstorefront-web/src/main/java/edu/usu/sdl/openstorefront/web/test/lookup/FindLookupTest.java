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
package edu.usu.sdl.openstorefront.web.test.lookup;

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class FindLookupTest
		extends BaseTestCase
{

	public FindLookupTest()
	{
		this.description = "Find Lookup Test";
	}

	@Override
	protected void runInternalTest()
	{
		Collection<Class<?>> entityClasses = DBManager.getConnection().getEntityManager().getRegisteredEntities();
		for (Class entityClass : entityClasses) {
			if (ReflectionUtil.LOOKUP_ENTITY.equals(entityClass.getSimpleName()) == false) {
				if (ReflectionUtil.isSubLookupEntity(entityClass)) {
					List<LookupEntity> lookupEnities = service.getLookupService().findLookup(entityClass);
					results.append("Lookup: ").append(entityClass.getSimpleName()).append("<br>");
					lookupEnities.forEach(entity -> {
						results.append(entity.getCode()).append("<br>");
					});
				}
			}
		}

	}

}
