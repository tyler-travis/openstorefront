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
package edu.usu.sdl.openstorefront.service.io;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.core.entity.Article;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class ArticleImporter
		extends BaseDirImporter
{

	private static final Logger log = Logger.getLogger(ArticleImporter.class.getName());

	@Override
	protected String getSyncProperty()
	{
		return ApplicationProperty.ARTICLE_IMPORTER_LAST_SYNC_DTS;
	}

	@Override
	protected void processFile(File file)
	{
		log.log(Level.INFO, MessageFormat.format("Importing article: {0}", file.getName()));
		if (file.length() <= OpenStorefrontConstant.FIELD_SIZE_ARTICLE_SIZE) {

			try {
				String key = file.getName();
				if (file.getName().contains(".")) {
					int index = file.getName().lastIndexOf(".");
					key = file.getName().substring(0, index);
				}

				if (key.contains(ReflectionUtil.COMPOSITE_KEY_SEPERATOR)) {
					AttributeCodePk attributeCodePk = AttributeCodePk.fromKey(key);
					String articleText = new String(Files.readAllBytes(Paths.get(file.getPath())));
					AttributeCode attributeCode = serviceProxy.getPersistenceService().findById(AttributeCode.class, attributeCodePk);
					if (attributeCode != null) {
						Article article = new Article();
						attributeCode.setArticle(article);
						serviceProxy.getAttributeService().saveArticle(attributeCode, articleText);
					} else {
						log.log(Level.SEVERE, MessageFormat.format("Unable to process article: {0}  Unable able to find attribute code.  Check the filename.", file.getPath()));
					}
				} else {
					log.log(Level.WARNING, MessageFormat.format("Invalid filename: {0} make sure to follow this format.  <TYPE>" + ReflectionUtil.COMPOSITE_KEY_SEPERATOR + "<CODE>.htm", file.getName()));
				}
			} catch (Exception e) {
				log.log(Level.SEVERE, "Unable to process article: " + file.getPath(), e);
			}
		} else {
			log.log(Level.WARNING, MessageFormat.format("Article: {0} has exceeded max allowed.  (MAX: {1})", new Object[]{file.getPath(), OpenStorefrontConstant.FIELD_SIZE_ARTICLE_SIZE}));
		}
	}

}
