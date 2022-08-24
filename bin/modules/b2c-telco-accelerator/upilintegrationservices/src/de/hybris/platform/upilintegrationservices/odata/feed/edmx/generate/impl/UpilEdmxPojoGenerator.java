/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate.impl;



import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxDataServiceElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxMetadataElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.exception.UpilEdmxAssociationEndCreationException;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxEntityTypeElementParserImpl;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxSchemaElementParserImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * To generate pojo's for Edmx
 *
 * @since 1911
 */
public class UpilEdmxPojoGenerator
{

	private static final Logger LOG = LoggerFactory.getLogger(UpilEdmxPojoGenerator.class);
	private static final String GENERATED_POJOS_DIR = "de" + File.separator + "hybris" + File.separator + "platform"
			+ File.separator + "upilintegrationservices" + File.separator + "data";
	private static final String ENTITY_TO_POJO_TEMPALTE_FILE = "entity.ftl";
	private static final String ENTITY_TO_POJO_TEMPALTE_FILE_DIR = File.separator + "resources" + File.separator
			+ "upilintegrationservices" + File.separator + "edmxData" + File.separator + ENTITY_TO_POJO_TEMPALTE_FILE;
	private static final String ENCODING = "UTF-8";
	private static final String TYPE_WRAPPER = "typeWrapper";
	private final File outputDir;
	Configuration cfg;


	public UpilEdmxPojoGenerator(final File outputDir)
	{
		this.outputDir = outputDir;
		this.createCfg();
	}

	/**
	 * For each edmx schema in edmx meta data provided generate's schema
	 *
	 * @param metadata
	 *           Edmx meta data
	 * @throws IOException
	 *            Throws exception IO exception
	 * @throws TemplateException
	 *            Throws template exception
	 */
	public void upilGenerate(final UpilEdmxMetadataElementParser metadata) throws IOException, TemplateException
	{
		if (this.outputDir == null)
		{
			throw new UpilEdmxAssociationEndCreationException("Generator must have its output directory set");
		}
		for (final UpilEdmxDataServiceElementParser ds : metadata.getDataServices())
		{
			final List<UpilEdmxSchemaElementParserImpl> schemas = ds.getSchemas();
			if (schemas != null)
			{
				for (final UpilEdmxSchemaElementParserImpl schema : schemas)
				{
					this.generateSchema(schema);
				}
			}
		}
	}

	protected File createOutputDir() throws IOException
	{
		final File packDir = new File(this.outputDir, GENERATED_POJOS_DIR);
		packDir.mkdirs();
		FileUtils.forceMkdir(packDir);
		return packDir;
	}

	protected void generateSchema(final UpilEdmxSchemaElementParserImpl schema) throws IOException, TemplateException
	{
		if (schema.getEntityTypes().isEmpty() && schema.getComplexTypes().isEmpty() && schema.getAssociations().isEmpty())
		{
			return;
		}
		final File output = createOutputDir();


		if (!schema.getEntityTypes().isEmpty())
		{
			for (final UpilEdmxEntityTypeElementParserImpl et : schema.getEntityTypes())
			{
				final File outputClass = new File(output, et.getName() + ".java");
				final UpilEdmxTypeWrapperImpl wrapper = new UpilEdmxTypeWrapperImpl(et);
				final HashMap<String, Object> root = Maps.newHashMap();
				root.put(TYPE_WRAPPER, wrapper);
				final Template temp = this.cfg.getTemplate(ENTITY_TO_POJO_TEMPALTE_FILE, ENCODING);
				try (FileWriter writer = new FileWriter(outputClass))
				{
					temp.process(root, writer);
				}
				catch (final IOException e)
				{
					LOG.info(e.getMessage(),e);
				}

			}
		}

	}

	protected final void createCfg()
	{
		try
		{
			final File entityTmpl = new File(outputDir.getParent(), ENTITY_TO_POJO_TEMPALTE_FILE_DIR);
			final File configDir = entityTmpl.getParentFile();
			if (!configDir.exists())
			{
				throw new UpilEdmxAssociationEndCreationException(
						"Freemarker Template Dir: " + configDir.getPath() + " doesn't exist");
			}
			this.cfg = new Configuration();
			this.cfg.setDirectoryForTemplateLoading(configDir);
			this.cfg.setObjectWrapper(new DefaultObjectWrapper());
		}
		catch (final Exception e)
		{
			throw new UpilEdmxAssociationEndCreationException(e);
		}
	}

}
