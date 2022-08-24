/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;



import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxDataServiceElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.atom.UpilEdmxNamespaces;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxDataServiceElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxDataServiceElementParserImpl implements UpilEdmxDataServiceElementParser
{

	private static final Logger log = LoggerFactory.getLogger(UpilEdmxDataServiceElementParserImpl.class);
	static final String DATA_SERVICE_VERSION = "DataServiceVersion";
	static final String MAX_DATA_SERVICE_VERSION = "MaxDataServiceVersion";
	private String version;
	private String maxVersion;
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();

	private final List<UpilEdmxSchemaElementParserImpl> schemas = Lists.newArrayList();

	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader) throws XMLStreamException
	{
		this.qName = startElement.getName();
		setAttributes(startElement);
		while (xmlEventReader.hasNext())
		{
			final XMLEvent event = xmlEventReader.nextEvent();
			if (UpilEdmxNamespaces.isEndElement(event, UpilEdmxNamespaces.EDMX_DATASERVICES))
			{
				return;
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.SCHEMAS))
			{
				final UpilEdmxSchemaElementParserImpl schema = new UpilEdmxSchemaElementParserImpl(this);
				final String element = UpilEdmxNamespaces.printStartElement(event.asStartElement());
				log.trace(element);
				schema.parse(event.asStartElement(), xmlEventReader);
				this.schemas.add(schema);
			}
		}
	}

	private void setAttributes(final StartElement startElement)
	{
		final Iterator<?> iter = startElement.getAttributes();
		while (iter.hasNext())
		{
			final Attribute att = (Attribute) iter.next();
			this.attributes.add(att);
			if (att.getName().getLocalPart().equalsIgnoreCase(DATA_SERVICE_VERSION))
			{
				this.version = att.getValue();
			}
			else if (att.getName().getLocalPart().equalsIgnoreCase(MAX_DATA_SERVICE_VERSION))
			{
				this.maxVersion = att.getValue();
			}
		}
	}


	public String getMaxVersion()
	{
		return maxVersion;
	}

	public String getVersion()
	{
		return this.version;
	}


	@Override
	public List<UpilEdmxSchemaElementParserImpl> getSchemas()
	{
		return this.schemas;
	}

	public static String getDataServiceVersion()
	{
		return DATA_SERVICE_VERSION;
	}


	public static String getMaxDataServiceVersion()
	{
		return MAX_DATA_SERVICE_VERSION;
	}

	public QName getqName()
	{
		return qName;
	}

	public List<Attribute> getAttributes()
	{
		return attributes;
	}

}
