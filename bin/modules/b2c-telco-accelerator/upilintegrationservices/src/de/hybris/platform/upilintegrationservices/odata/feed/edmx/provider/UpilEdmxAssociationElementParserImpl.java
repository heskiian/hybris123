/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */


package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;


import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxAssociationElementParser;
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
import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxAssociationElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxAssociationElementParserImpl implements UpilEdmxAssociationElementParser
{

	private final UpilEdmxSchemaElementParserImpl schema;
	private String name;
	private QName qName;

	private final List<Attribute> attributes = Lists.newArrayList();
	private final List<UpilEdmxAssociationEndElementParserImpl> ends = Lists.newArrayList();

	public UpilEdmxAssociationElementParserImpl(final UpilEdmxSchemaElementParserImpl schema)
	{
		this.schema = schema;
	}

	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader) throws XMLStreamException
	{
		this.qName = startElement.getName();
		setAttributes(startElement);
		while (xmlEventReader.hasNext())
		{
			final XMLEvent event = xmlEventReader.nextEvent();
			if (UpilEdmxNamespaces.isEndElement(event, UpilEdmxNamespaces.ASSOCIATIONS))
			{
				return;
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.ENDS))
			{
				final UpilEdmxAssociationEndElementParserImpl end = new UpilEdmxAssociationEndElementParserImpl(this);
				end.parse(event.asStartElement(), xmlEventReader);
				this.ends.add(end);
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
			if (att.getName().getLocalPart().equalsIgnoreCase(UpilEdmxConstants.NAME))
			{
				this.name = att.getValue();
			}
		}
	}

	public String getName()
	{
		return this.name;
	}

	protected UpilEdmxSchemaElementParserImpl getSchema()
	{
		return this.schema;
	}

	public List<UpilEdmxAssociationEndElementParserImpl> getEnds()
	{
		return this.ends;
	}


	protected QName getqName()
	{
		return qName;
	}

	protected List<Attribute> getAttributes()
	{
		return attributes;
	}
}
