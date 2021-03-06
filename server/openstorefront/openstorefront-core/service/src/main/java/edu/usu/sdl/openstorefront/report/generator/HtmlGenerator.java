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
package edu.usu.sdl.openstorefront.report.generator;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.Report;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class HtmlGenerator
		extends BaseGenerator
{

	BufferedWriter writer;

	public HtmlGenerator(Report report)
	{
		super(report);
	}

	@Override
	public void init()
	{
		Objects.requireNonNull(report, "The generator requires the report to exist.");
		Objects.requireNonNull(report.getReportId(), "The report id is required.");
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(report.pathToReport().toFile())));
			writer.append("<html><body style='padding: 20px'>");
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to open file to write report.", "Check file system permissions", ex);
		}
	}

	public void addStyleBlock(String style)
	{
		try {
			writer.append("<style>");
			writer.newLine();
			writer.append(style);
			writer.newLine();
			writer.append("</style>");
			writer.newLine();

		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	public void addMainHeader(String data)
	{
		try {
			writer.append("<h1>");
			writer.append(data);
			writer.append("</h1>");
			writer.newLine();
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	public void addRuleLine()
	{
		addLine("<hr>");
	}

	public void addSpace()
	{
		addLine("<br>");
	}

	public void addLine(String line)
	{
		if (StringUtils.isNotBlank(line)) {
			try {
				writer.append(line);
				writer.newLine();
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException(ex);
			}
		}
	}

	@Override
	protected void internalFinish()
	{
		if (writer != null) {
			try {
				writer.append("</body></html>");
				writer.close();
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Failed to close report file. Report: " + report.pathToReport(), ex);
			}
		}
	}

}
