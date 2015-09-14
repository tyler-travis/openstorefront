/*
 * To change this license header, choose License Headers in Project Properties.
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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;

/**
 * Gets service proxy.
 *
 * @author dshurtleff
 */
public class ServiceProxyFactory
{

	private static final Logger log = Logger.getLogger(ServiceProxyFactory.class.getName());

	private static BundleContext context;

	public static Service getServiceProxy()
	{
		if (context == null) {
			throw new OpenStorefrontRuntimeException("Unable to look up service context is null");
		}

		Service service = context.getService(context.getServiceReference(Service.class));
		return service;
	}

	public static void setContext(BundleContext bundleContext)
	{
		context = bundleContext;
	}

}