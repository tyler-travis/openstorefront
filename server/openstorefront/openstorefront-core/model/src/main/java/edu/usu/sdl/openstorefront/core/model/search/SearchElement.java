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
package edu.usu.sdl.openstorefront.core.model.search;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.MergeCondition;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.NumberOperation;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.SearchType;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.StringOperation;
import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
public class SearchElement
{

	@NotNull
	@APIDescription("Default: COMPONENT")
	@ValidValueType(value = {}, enumClass = SearchType.class)
	private SearchType searchType = SearchType.COMPONENT;

	@APIDescription("Field on the entity to be searched. Doesn't apply in all search types.")
	private String field;
	@APIDescription("Value to look for.  It will be interrpeted base on the field type. ")
	private String value;

	@APIDescription("Used in Attribute and Metadata searches.  This should be the main type code/key")
	private String keyField;
	@APIDescription("Used in Attribute and Metadata searches.  This should be the code or value.")
	private String keyValue;

	@APIDescription("Used for date data types")
	private Date startDate;

	@APIDescription("Used for date data types")
	private Date endDate;

	@APIDescription("Used for string data types")
	private boolean caseInsensitive;

	@APIDescription("Default: EQUALS")
	@ValidValueType(value = {}, enumClass = NumberOperation.class)
	private NumberOperation numberOperation = NumberOperation.EQUALS;

	@APIDescription("Default: EQUALS")
	@ValidValueType(value = {}, enumClass = StringOperation.class)
	private StringOperation stringOperation = StringOperation.EQUALS;

	@NotNull
	@APIDescription("Default: AND")
	@ValidValueType(value = {}, enumClass = MergeCondition.class)
	private MergeCondition mergeCondition = MergeCondition.OR;

	public SearchElement()
	{
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public SearchType getSearchType()
	{
		return searchType;
	}

	public void setSearchType(SearchType searchType)
	{
		this.searchType = searchType;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getKeyField()
	{
		return keyField;
	}

	public void setKeyField(String keyField)
	{
		this.keyField = keyField;
	}

	public String getKeyValue()
	{
		return keyValue;
	}

	public void setKeyValue(String keyValue)
	{
		this.keyValue = keyValue;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public boolean getCaseInsensitive()
	{
		return caseInsensitive;
	}

	public void setCaseInsensitive(boolean caseInsensitive)
	{
		this.caseInsensitive = caseInsensitive;
	}

	public NumberOperation getNumberOperation()
	{
		return numberOperation;
	}

	public void setNumberOperation(NumberOperation numberOperation)
	{
		this.numberOperation = numberOperation;
	}

	public StringOperation getStringOperation()
	{
		return stringOperation;
	}

	public void setStringOperation(StringOperation stringOperation)
	{
		this.stringOperation = stringOperation;
	}

	public SearchOperation.MergeCondition getMergeCondition()
	{
		return mergeCondition;
	}

	public void setMergeCondition(SearchOperation.MergeCondition mergeCondition)
	{
		this.mergeCondition = mergeCondition;
	}
}
