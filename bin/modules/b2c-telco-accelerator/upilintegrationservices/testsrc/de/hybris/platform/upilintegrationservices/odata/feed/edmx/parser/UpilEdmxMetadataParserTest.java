/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx.parser;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxMetadataElementParser;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Test class for {@link UpilEdmxMetadataParser}.
 *
 * @since 1911
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
{ "classpath:/test/spring/test-context.xml" })
public class UpilEdmxMetadataParserTest
{
	static final Logger log = Logger.getLogger(UpilEdmxMetadataParserTest.class);

	@Inject
	ApplicationContext appContext;
	BufferedInputStream bufferedInputStream;

	@After
	public void tearDown() throws Exception
	{
		IOUtils.closeQuietly(this.bufferedInputStream);
	}

	@Test
	public void testEdmxMetaDataFileToParseSuccess() throws XMLStreamException, FactoryConfigurationError, IOException
	{
		final Resource resource = this.appContext.getResource("classpath:/upilintegrationservices/edmxData/metadata.xml");
		final UpilEdmxMetadataParser parser = new UpilEdmxMetadataParser();
		final UpilEdmxMetadataElementParser metaData = parser.parseXml(resource.getInputStream());
		Assert.assertNotNull(metaData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToNotToParseBlankMetaDataFile() throws XMLStreamException, FactoryConfigurationError, IOException
	{
		final Resource resource = this.appContext.getResource("classpath:/test/edmxTestData/blank.xml");
		final UpilEdmxMetadataParser parser = new UpilEdmxMetadataParser();
		parser.parseXml(resource.getInputStream());
	}

}
