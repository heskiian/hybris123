/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;


import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxNavigationPropertyElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxPropertyElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxSchemaElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxTypeParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.atom.UpilEdmxNamespaces;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.exception.UpilEdmxAssociationEndCreationException;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxTypeParser}.
 * 
 * @since 1911
 */
public class UpilEdmxEntityTypeElementParserImpl extends UpilEdmxTypeParser
{

	private static final Logger logg = LoggerFactory.getLogger(UpilEdmxEntityTypeElementParserImpl.class);
	private static final String OPEN_TYPE = "openType";
	private final List<UpilEdmxNavigationPropertyElementParser> navigationProperties = Lists.newArrayList();
	private List<String> keys = Lists.newArrayList();
	protected Boolean openType;

	public UpilEdmxEntityTypeElementParserImpl(final UpilEdmxSchemaElementParser schema)
	{
		this.schema = schema;
	}


	public List<String> getKeys()
	{
		return this.keys;
	}

	public void setKeys(final List<String> keys)
	{
		this.keys = keys;
	}


	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader) throws XMLStreamException
	{
		final String startElementName = UpilEdmxNamespaces.printStartElement(startElement);
		logg.trace(startElementName);
		this.qName = startElement.getName();
		setAttributes(startElement);
		while (xmlEventReader.hasNext())
		{
			final XMLEvent event = xmlEventReader.nextEvent();
			if (UpilEdmxNamespaces.isEndElement(event, UpilEdmxNamespaces.ENTITY_TYPES))
			{
				return;
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.KEYS))
			{
				final String key = parseKey(xmlEventReader);
				this.keys.add(key);
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.PROPERTIES))
			{
				final UpilEdmxPropertyElementParser property = new UpilEdmxPropertyElementParserImpl(this);
				property.parse(event.asStartElement(), xmlEventReader);
				this.properties.add(property);
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.NAV_PROPS))
			{
				final UpilEdmxNavigationPropertyElementParser navProp = new UpilEdmxNavigationPropertyElementParserImpl(this);
				navProp.parse(event.asStartElement(), xmlEventReader);
				this.navigationProperties.add(navProp);
			}
		}
	}

	@Override
	protected void setAttributes(final StartElement startElement)
	{
		super.setAttributes(startElement);
		final Iterator<Attribute> iter = startElement.getAttributes();
		while (iter.hasNext())
		{
			final Attribute att = iter.next();
			if (att.getName().getLocalPart().equalsIgnoreCase(OPEN_TYPE))
			{
				this.openType = BooleanUtils.toBooleanObject(att.getValue());
			}
		}
	}

	private String parseKey(final XMLEventReader reader) throws XMLStreamException
	{
		String val = null;
		while (reader.hasNext())
		{
			final XMLEvent event = reader.nextEvent();
			if (UpilEdmxNamespaces.isEndElement(event, UpilEdmxNamespaces.KEYS))
			{
				break;
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.PROP_REFS))
			{
				final Attribute att = event.asStartElement().getAttributeByName(new QName(UpilEdmxConstants.NAME));
				if (att == null)
				{
					throw new UpilEdmxAssociationEndCreationException("Cannot find Key name for property");
				}
				else
				{
					val = att.getValue();
				}
			}
		}
		return val;
	}

	public List<UpilEdmxNavigationPropertyElementParser> getNavigationProperties()
	{
		return this.navigationProperties;
	}


}
