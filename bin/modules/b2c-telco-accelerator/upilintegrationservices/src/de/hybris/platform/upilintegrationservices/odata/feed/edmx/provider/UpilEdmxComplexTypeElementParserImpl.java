/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;

import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxPropertyElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxSchemaElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxTypeParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.atom.UpilEdmxNamespaces;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


/**
 * To Parse Edmx complex type elements
 * 
 * @since 1911
 */
public class UpilEdmxComplexTypeElementParserImpl extends UpilEdmxTypeParser
{

	public UpilEdmxComplexTypeElementParserImpl(final UpilEdmxSchemaElementParser schema)
	{
		this.schema = schema;
	}

	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader) throws XMLStreamException
	{
		setAttributes(startElement);
		while (xmlEventReader.hasNext())
		{
			final XMLEvent event = xmlEventReader.nextEvent();
			if (UpilEdmxNamespaces.isEndElement(event, UpilEdmxNamespaces.COMPLEX_TYPES))
			{
				return;
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.COMPLEX_TYPES))
			{
				final UpilEdmxComplexTypeElementParserImpl embedded = new UpilEdmxComplexTypeElementParserImpl(this.schema);
				embedded.parse(startElement, xmlEventReader);
				this.complexTypes.add(embedded);
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.PROPERTIES))
			{
				final UpilEdmxPropertyElementParser property = new UpilEdmxPropertyElementParserImpl(this);
				property.parse(event.asStartElement(), xmlEventReader);
				this.properties.add(property);
			}
		}
	}

}
