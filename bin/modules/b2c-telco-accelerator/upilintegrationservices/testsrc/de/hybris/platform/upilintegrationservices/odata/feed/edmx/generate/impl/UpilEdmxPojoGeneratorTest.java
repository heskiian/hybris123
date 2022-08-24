/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxMetadataElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.parser.UpilEdmxMetadataParser;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Test class for {@link UpilEdmxPojoGenerator}.
 *
 * @since 1911
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
{ "classpath:/test/spring/test-context.xml" })
public class UpilEdmxPojoGeneratorTest
{
	@Inject
	ApplicationContext appContext;
	private static File createDir = null;
	private static final String SLASH_SYMBOL = "/";
	private static final String TEST_EDMX_POJO_DIR = "/testEdmsPojos";

	@BeforeClass
	public static void before() throws IOException
	{

		final String classFolderPath = UpilEdmxPojoGeneratorTest.class.getProtectionDomain().getCodeSource().getLocation()
				.getPath();
		final int lastCharindex = classFolderPath.lastIndexOf(SLASH_SYMBOL);
		final String classPath = classFolderPath.substring(0, lastCharindex);
		final int pathIndexForRootFolder = classPath.lastIndexOf(SLASH_SYMBOL);
		final String rootPath = classPath.substring(0, pathIndexForRootFolder);
		createDir = new File(rootPath + TEST_EDMX_POJO_DIR);
		createDir.mkdirs();

	}

	@Test
	public void testGeneratePojosForDataServiceVersion1() throws Exception
	{
		final Resource resource = this.appContext.getResource("classpath:/test/edmxTestData/dataServiceVersion1metadata.xml");
		final UpilEdmxMetadataParser parser = new UpilEdmxMetadataParser();
		final UpilEdmxMetadataElementParser metadata = parser.parseXml(resource.getInputStream());
		new UpilEdmxPojoGenerator(createDir).upilGenerate(metadata);
		Assert.assertTrue(createDir.isDirectory());
		if (createDir.exists())
		{
			Assert.assertEquals(true, createDir.list().length > 0);
		}
	}

	@Test
	public void testGeneratePojosForDataServiceVersion2() throws Exception
	{
		final Resource resource = this.appContext.getResource("classpath:/upilintegrationservices/edmxData/metadata.xml");
		final UpilEdmxMetadataParser parser = new UpilEdmxMetadataParser();
		final UpilEdmxMetadataElementParser metadata = parser.parseXml(resource.getInputStream());
		new UpilEdmxPojoGenerator(createDir).upilGenerate(metadata);
		Assert.assertTrue(createDir.isDirectory());
		if (createDir.exists())
		{
			Assert.assertEquals(true, createDir.list().length > 0);
		}
	}

	@Test
	public void testGeneratePojosForDataServiceVersion3() throws Exception
	{
		final Resource resource = this.appContext.getResource("classpath:/test/edmxTestData/dataServiceVersion3Metadata.xml");
		final UpilEdmxMetadataParser parser = new UpilEdmxMetadataParser();
		final UpilEdmxMetadataElementParser metadata = parser.parseXml(resource.getInputStream());
		new UpilEdmxPojoGenerator(createDir).upilGenerate(metadata);
		Assert.assertTrue(createDir.isDirectory());
		if (createDir.exists())
		{
			Assert.assertEquals(true, createDir.list().length > 0);
		}
	}
}
