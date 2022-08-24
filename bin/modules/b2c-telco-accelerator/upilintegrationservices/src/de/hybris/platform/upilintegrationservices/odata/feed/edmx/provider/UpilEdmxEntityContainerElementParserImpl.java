/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;



import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxAssociationSetElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxEntityContainerElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxEntitySetElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxSchemaElementParser;
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
 * Default implementation of the {@link UpilEdmxEntityContainerElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxEntityContainerElementParserImpl implements UpilEdmxEntityContainerElementParser
{

	private UpilEdmxSchemaElementParser schema;
	private String name;
	private String extend;
	private List<UpilEdmxEntitySetElementParser> entitySets = Lists.newArrayList();
	private List<UpilEdmxAssociationSetElementParser> associationSets = Lists.newArrayList();
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();

	public UpilEdmxEntityContainerElementParserImpl()
	{
	}

	public UpilEdmxEntityContainerElementParserImpl(final UpilEdmxSchemaElementParser schema)
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
			if (UpilEdmxNamespaces.isEndElement(event, UpilEdmxNamespaces.ENTITY_CONTAINERS))
			{
				return;
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.ENTITY_SETS))
			{
				final UpilEdmxEntitySetElementParser entitySet = new UpilEdmxEntitySetElementParserImpl(this);
				entitySet.parse(event.asStartElement(), xmlEventReader);
				this.entitySets.add(entitySet);
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.ASSOCIATION_SETS))
			{
				final UpilEdmxAssociationSetElementParser associationSet = new UpilEdmxAssociationSetElementParserImpl(this);
				associationSet.parse(event.asStartElement(), xmlEventReader);
				this.associationSets.add(associationSet);
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
			if (att.getName().getLocalPart().equalsIgnoreCase(UpilEdmxConstants.ENTITY_TYPE))
			{
				this.extend = att.getValue();
			}
		}
	}

	public UpilEdmxSchemaElementParser getSchema()
	{
		return this.schema;
	}

	public void setSchema(final UpilEdmxSchemaElementParser schema)
	{
		this.schema = schema;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getExtend()
	{
		return this.extend;
	}

	public void setExtends(final String extends1)
	{
		this.extend = extends1;
	}

	public List<UpilEdmxEntitySetElementParser> getEntitySets()
	{
		return this.entitySets;
	}

	public void setEntitySets(final List<UpilEdmxEntitySetElementParser> entitySets)
	{
		this.entitySets = entitySets;
	}

	public List<UpilEdmxAssociationSetElementParser> getAssociationSets()
	{
		return this.associationSets;
	}

	public void setAssociationSets(final List<UpilEdmxAssociationSetElementParser> associationSets)
	{
		this.associationSets = associationSets;
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
