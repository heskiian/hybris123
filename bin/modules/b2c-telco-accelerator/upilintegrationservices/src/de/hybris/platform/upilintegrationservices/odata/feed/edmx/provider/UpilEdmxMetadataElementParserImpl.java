/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;



import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxDataServiceElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxMetadataElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.atom.UpilEdmxNamespaces;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;

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
 * Default implementation of the {@link UpilEdmxMetadataElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxMetadataElementParserImpl implements UpilEdmxMetadataElementParser
{

	private static final Logger log = LoggerFactory.getLogger(UpilEdmxAssociationElementParserImpl.class);
	private String version;
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();

	List<UpilEdmxDataServiceElementParser> dataServices = Lists.newArrayList();

	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader) throws XMLStreamException
	{
		final String startingElement = UpilEdmxNamespaces.printStartElement(startElement);
		log.trace(startingElement);
		this.qName = startElement.getName();
		setAttributes(startElement);
		while (xmlEventReader.hasNext())
		{
			final XMLEvent event = xmlEventReader.nextEvent();
			if (UpilEdmxNamespaces.isEndElement(event, UpilEdmxNamespaces.EDMX))
			{
				return;
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.EDMX_DATASERVICES))
			{
				final UpilEdmxDataServiceElementParser dataService = new UpilEdmxDataServiceElementParserImpl();
				dataService.parse(event.asStartElement(), xmlEventReader);
				this.getDataServices().add(dataService);
			}
		}

	}

	private void setAttributes(final StartElement startElement)
	{
		final Iterator<Attribute> iter = startElement.getAttributes();
		while (iter.hasNext())
		{
			final Attribute att = iter.next();
			this.attributes.add(att);
			if (att.getName().getLocalPart().equalsIgnoreCase(UpilEdmxConstants.VERSION))
			{
				this.version = att.getValue();
			}
		}

	}

	@Override
	public List<UpilEdmxDataServiceElementParser> getDataServices()
	{
		return this.dataServices;
	}

	public String getVersion()
	{
		return version;
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
