/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;



import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxEntityContainerElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxEntitySetElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxEntitySetElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxEntitySetElementParserImpl implements UpilEdmxEntitySetElementParser
{

	private final UpilEdmxEntityContainerElementParser entityContainer;
	private String entityType;
	private String name;
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();

	public UpilEdmxEntitySetElementParserImpl(final UpilEdmxEntityContainerElementParser entityContainer)
	{
		this.entityContainer = entityContainer;
	}

	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader)
	{
		this.qName = startElement.getName();
		setAttributes(startElement);
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
			if (att.getName().getLocalPart().equalsIgnoreCase(UpilEdmxConstants.ENTITY_TYPE))
			{
				this.entityType = att.getValue();
			}
		}
	}


	public UpilEdmxEntityContainerElementParser getEntityContainer()
	{
		return this.entityContainer;
	}

	public String getEntityType()
	{
		return this.entityType;
	}

	public String getName()
	{
		return this.name;
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
