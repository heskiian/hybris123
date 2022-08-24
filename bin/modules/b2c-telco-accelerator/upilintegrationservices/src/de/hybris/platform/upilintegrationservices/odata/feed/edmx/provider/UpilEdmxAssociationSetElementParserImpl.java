/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;



import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxAssociationSetElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxAssociationSetEndElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxEntityContainerElementParser;
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
 * Default implementation of the {@link UpilEdmxAssociationSetElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxAssociationSetElementParserImpl implements UpilEdmxAssociationSetElementParser
{
	private String name;
	private String association;
	private final UpilEdmxEntityContainerElementParser entityContainer;
	private final List<UpilEdmxAssociationSetEndElementParser> ends = Lists.newArrayList();
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();

	public UpilEdmxAssociationSetElementParserImpl(final UpilEdmxEntityContainerElementParser entityContainer)
	{
		this.entityContainer = entityContainer;
	}

	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader) throws XMLStreamException
	{
		this.qName = startElement.getName();
		setAttributes(startElement);
		while (xmlEventReader.hasNext())
		{
			final XMLEvent event = xmlEventReader.nextEvent();
			if (UpilEdmxNamespaces.isEndElement(event, UpilEdmxNamespaces.ASSOCIATION_SETS))
			{
				return;
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.ENDS))
			{
				final UpilEdmxAssociationSetEndElementParser associationSetEnd = new UpilEdmxAssociationSetEndElementParserImpl(this);
				associationSetEnd.parse(event.asStartElement(), xmlEventReader);
				this.ends.add(associationSetEnd);
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
			if (att.getName().getLocalPart().equalsIgnoreCase(UpilEdmxConstants.NAME))
			{
				this.name = att.getValue();
			}
			if (att.getName().getLocalPart().equalsIgnoreCase(UpilEdmxConstants.ASSOCIATION))
			{
				this.association = att.getValue();
			}
		}
	}


	public String getName()
	{
		return this.name;
	}

	public String getAssociation()
	{
		return this.association;
	}

	public UpilEdmxEntityContainerElementParser getEntityContainer()
	{
		return this.entityContainer;
	}

	public List<UpilEdmxAssociationSetEndElementParser> getEnds()
	{
		return this.ends;
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
