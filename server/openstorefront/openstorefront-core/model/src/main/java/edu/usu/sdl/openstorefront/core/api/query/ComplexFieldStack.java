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
package edu.usu.sdl.openstorefront.core.api.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Used to hold complex field mappings for querying
 *
 * @author dshurtleff
 */
public class ComplexFieldStack
{

	private final Stack<String> fieldStack = new Stack<>();
	private String pathSeparator = ".";

	public ComplexFieldStack()
	{
	}

	public ComplexFieldStack(String pathSeparator)
	{
		this.pathSeparator = pathSeparator;
	}

	public String getQueryFieldName()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("");

		if (fieldStack.empty() == false) {
			List<String> fieldList = new ArrayList<>(fieldStack);
			Collections.reverse(fieldList);

			fieldList.forEach(field -> {
				sb.append(field);
				sb.append(pathSeparator);
			});
		}
		return sb.toString();
	}

	public Stack<String> getFieldStack()
	{
		return fieldStack;
	}

}
