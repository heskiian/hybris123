/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;


import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxAssociationElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxAssociationEndElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxAssociationEndElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxAssociationEndElementParserImpl implements UpilEdmxAssociationEndElementParser
{

	private final UpilEdmxAssociationElementParser association;
	private String type;
	private String role;
	private String multiplicity;
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();


	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader)
	{
		this.qName = startElement.getName();
		setAttributes(startElement);
	}

	public UpilEdmxAssociationEndElementParserImpl(final UpilEdmxAssociationElementParser association)
	{
		this.association = association;
	}

	private void setAttributes(final StartElement startElement)
	{
		final Iterator<Attribute> iter = startElement.getAttributes();
		while (iter.hasNext())
		{
			final Attribute att = iter.next();
			this.attributes.add(att);
			final String x = att.getName().getLocalPart();
			if (x.equalsIgnoreCase(UpilEdmxConstants.TYPE))
			{
				this.type = att.getValue();
			}
			else if (x.equalsIgnoreCase(UpilEdmxConstants.ROLE))
			{
				this.role = att.getValue();
			}
			else if (x.equalsIgnoreCase(UpilEdmxConstants.MULTIPLICITY))
			{
				this.multiplicity = att.getValue();
			}
		}
	}

	protected UpilEdmxAssociationElementParser getAssociation()
	{
		return this.association;
	}


	public String getType()
	{
		return this.type;
	}

	public String getRole()
	{
		return this.role;
	}


	public String getMultiplicity()
	{
		return this.multiplicity;
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
