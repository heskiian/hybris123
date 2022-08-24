/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */


package de.hybris.platform.upilintegrationservices.odata.feed.edmx.parser;



import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxMetadataElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.atom.UpilEdmxNamespaces;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxMetadataElementParserImpl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Upil Edmx Metadata Parser
 *
 * @since 1911
 */
public class UpilEdmxMetadataParser
{

	private static final Logger log = LoggerFactory.getLogger(UpilEdmxMetadataParser.class);

	private UpilEdmxMetadataElementParser metadata;
	private XMLEventReader reader;

	/**
	 * To parse the XML input stream to UpilEdmxMetadata
	 *
	 * @param inputStream
	 *           The InputStream
	 * @return
	 *         Returns EdmxMetadata
	 * @throws XMLStreamException
	 *            throws XMLStreamException
	 * @throws FactoryConfigurationError
	 *            throws FactoryConfigurationError
	 * @throws IOException
	 *            throws IOException
	 */
	public UpilEdmxMetadataElementParser parseXml(final InputStream inputStream)
			throws XMLStreamException, FactoryConfigurationError, IOException
	{

		try (BufferedInputStream br = new BufferedInputStream(inputStream))
		{

			final XMLInputFactory factory = XMLInputFactory.newInstance();
			factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
			factory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
			reader = factory.createXMLEventReader(br);
			while (reader.hasNext())
			{
				final XMLEvent event = reader.nextEvent();
				if (event.isStartDocument())
				{
					confirmEdmx();
				}
				else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.EDMX))
				{
					metadata = new UpilEdmxMetadataElementParserImpl();
					metadata.parse(event.asStartElement(), reader);
				}
			}

			return metadata;
		}
	}

	protected void confirmEdmx()
	{
		while (true)
		{
			try
			{
				final XMLEvent event = reader.peek();
				if (null != event && event.isStartElement())
				{
					final StartElement root = event.asStartElement();
					if (!root.getName().equals(UpilEdmxNamespaces.EDMX))
					{
						throw new IllegalArgumentException(
								String.format("Root of Xml Doc = %s - Only edmx files accepted", root.getName().toString()));
					}
					return;
				}
			}
			catch (final Exception e)
			{
				log.info("exception", e);
				log.error("Only edmx files accepted");
				throw new IllegalArgumentException("Ony edmx files accepted");
			}
		}
	}

}
