/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;


import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxDataServiceElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxEntityContainerElementParser;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxSchemaElementParser}.
 *
 * @since 1911
 */
public class UpilEdmxSchemaElementParserImpl implements UpilEdmxSchemaElementParser
{

	private static final Logger log = LoggerFactory.getLogger(UpilEdmxSchemaElementParserImpl.class);
	private static final String NAME_SPACE = "Namespace";
	private static final String ALIAS_FOR_NAME = "Alias";
	private String namespace;
	private String alias;

	private final List<UpilEdmxAssociationElementParserImpl> associations = Lists.newArrayList();
	private final List<UpilEdmxComplexTypeElementParserImpl> complexTypes = Lists.newArrayList();
	private final List<UpilEdmxEntityTypeElementParserImpl> entityTypes = Lists.newArrayList();
	private final List<UpilEdmxEntityContainerElementParser> entityContainers = Lists.newArrayList();
	private final UpilEdmxDataServiceElementParser dataService;
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();

	public UpilEdmxSchemaElementParserImpl(final UpilEdmxDataServiceElementParser dataService)
	{
		this.dataService = dataService;
	}

	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader) throws XMLStreamException
	{
		this.qName = startElement.getName();
		setAttributes(startElement);
		while (xmlEventReader.hasNext())
		{
			final XMLEvent event = xmlEventReader.nextEvent();
			if (UpilEdmxNamespaces.isEndElement(event, UpilEdmxNamespaces.SCHEMAS))
			{
				return;
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.ENTITY_TYPES))
			{
				final UpilEdmxEntityTypeElementParserImpl entityType = new UpilEdmxEntityTypeElementParserImpl(this);
				final String printStartElement = UpilEdmxNamespaces.printStartElement(event.asStartElement());
				log.trace(printStartElement);
				entityType.parse(event.asStartElement(), xmlEventReader);
				this.entityTypes.add(entityType);
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.ASSOCIATIONS))
			{
				final UpilEdmxAssociationElementParserImpl association = new UpilEdmxAssociationElementParserImpl(this);
				association.parse(event.asStartElement(), xmlEventReader);
				this.associations.add(association);
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.COMPLEX_TYPES))
			{
				final UpilEdmxComplexTypeElementParserImpl complexType = new UpilEdmxComplexTypeElementParserImpl(this);
				complexType.parse(event.asStartElement(), xmlEventReader);
				this.complexTypes.add(complexType);
			}
			else if (UpilEdmxNamespaces.isStartElement(event, UpilEdmxNamespaces.ENTITY_CONTAINERS))
			{
				final UpilEdmxEntityContainerElementParser entityContainer = new UpilEdmxEntityContainerElementParserImpl(this);
				entityContainer.parse(event.asStartElement(), xmlEventReader);
				this.entityContainers.add(entityContainer);
			}
		}
	}

	protected void setAttributes(final StartElement startElement)
	{
		final Iterator<Attribute> iter = startElement.getAttributes();
		while (iter.hasNext())
		{
			final Attribute att = iter.next();
			this.attributes.add(att);
			if (att.getName().getLocalPart().equalsIgnoreCase(NAME_SPACE))
			{
				this.namespace = att.getValue();
			}
			else if (att.getName().getLocalPart().equalsIgnoreCase(ALIAS_FOR_NAME))
			{
				this.alias = att.getValue();
			}
		}
	}

	public List<UpilEdmxAssociationElementParserImpl> getAssociations()
	{
		return this.associations;
	}

	public List<UpilEdmxComplexTypeElementParserImpl> getComplexTypes()
	{
		return this.complexTypes;
	}

	public List<UpilEdmxEntityTypeElementParserImpl> getEntityTypes()
	{
		return this.entityTypes;
	}

	public String getNamespace()
	{
		return this.namespace;
	}

	public String getAlias()
	{
		return this.alias;
	}

	public List<UpilEdmxEntityContainerElementParser> getEntityContainers()
	{
		return this.entityContainers;
	}

	public UpilEdmxDataServiceElementParser getDataService()
	{
		return this.dataService;
	}


	/**
	 * To get association by given name
	 *
	 * @param name
	 *           String association name
	 * @return
	 *         The UpilEdmxAssociationImpl association for given name
	 */
	public UpilEdmxAssociationElementParserImpl getAssociationByName(final String name)
	{
		if (this.associations == null || this.associations.isEmpty())
		{
			return null;
		}
		for (final UpilEdmxAssociationElementParserImpl temp : this.associations)
		{
			if (name.equals(this.namespace + UpilEdmxConstants.DOT + temp.getName()) || name.equals(temp.getName()))
			{
				return temp;
			}
		}
		return null;
	}

	public QName getQName()
	{
		return qName;
	}

	public List<Attribute> getAttributes()
	{
		return attributes;
	}

}
