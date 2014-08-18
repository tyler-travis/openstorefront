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

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.api.LookupService;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.storage.model.TestEntity;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class LookupServiceImpl
	extends ServiceProxy
	implements LookupService	
{
	private static final Logger log = Logger.getLogger(LookupServiceImpl.class.getName());
	
	@Override	
	public <T extends BaseEntity> List<T> findLookup(Class<T> lookTableClass)
	{
		return findLookup(lookTableClass, false);
	}	
	
	@Override	
	public <T extends BaseEntity> List<T> findLookup(Class<T> lookTableClass, boolean all)
	{	
		try
		{
			T testExample = lookTableClass.newInstance();
			if (all == false)
			{
				testExample.setActiveStatus(TestEntity.ACTIVE_STATUS);		
			}
			return persistenceService.queryByExample(new QueryByExample(testExample));			
		} catch (InstantiationException | IllegalAccessException ex)
		{		
			throw new OpenStorefrontRuntimeException(ex);
		}				
	}

	@Override
	public  void saveLookupValue(LookupEntity lookupEntity)
	{
		LookupEntity oldEntity = persistenceService.findById(lookupEntity.getClass(), lookupEntity.getCode());
		if (oldEntity != null)
		{
			oldEntity.setDescription(lookupEntity.getDescription());
			oldEntity.setActiveStatus(lookupEntity.getActiveStatus());			
			oldEntity.setUpdateUser(lookupEntity.getUpdateUser());			
			oldEntity.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(oldEntity);					
		}
		else
		{
			lookupEntity.setCreateDts(TimeUtil.currentDate());
			lookupEntity.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(lookupEntity);
		}
	}
	
	
	
}