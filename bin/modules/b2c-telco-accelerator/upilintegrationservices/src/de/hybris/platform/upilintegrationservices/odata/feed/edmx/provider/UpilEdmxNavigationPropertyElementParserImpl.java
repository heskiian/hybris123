/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;


import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxNavigationPropertyElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxNavigationPropertyElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxNavigationPropertyElementParserImpl implements UpilEdmxNavigationPropertyElementParser
{

	private static final String NAV_RELATIONSHIP = "Relationship";
	private static final String TO_ROLE = "ToRole";
	private static final String FROM_ROLE = "FromRole";
	private final UpilEdmxEntityTypeElementParserImpl entityType;
	private String name;
	private String relationship;
	private String toRole;
	private String fromRole;
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();

	public UpilEdmxNavigationPropertyElementParserImpl(final UpilEdmxEntityTypeElementParserImpl entityType)
	{
		this.entityType = entityType;
	}

	@Override
	public void parse(final StartElement startElement, final XMLEventReader reader)
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
			final String attributeName = att.getName().getLocalPart();
			if (attributeName.equalsIgnoreCase(UpilEdmxConstants.NAME))
			{
				this.name = att.getValue();
			}
			else if (attributeName.equalsIgnoreCase(NAV_RELATIONSHIP))
			{
				this.relationship = att.getValue();
			}
			else if (attributeName.equalsIgnoreCase(TO_ROLE))
			{
				this.toRole = att.getValue();
			}
			else if (attributeName.equalsIgnoreCase(FROM_ROLE))
			{
				this.fromRole = att.getValue();
			}
		}
	}

	public UpilEdmxEntityTypeElementParserImpl getEntityType()
	{
		return this.entityType;
	}

	public String getName()
	{
		return this.name;
	}

	public String getRelationship()
	{
		return this.relationship;
	}

	public String getToRole()
	{
		return this.toRole;
	}


	public String getFromRole()
	{
		return this.fromRole;
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
