/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */


package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;

import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxAssociationSetElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxAssociationSetEndElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxAssociationSetEndElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxAssociationSetEndElementParserImpl implements UpilEdmxAssociationSetEndElementParser
{

	private final UpilEdmxAssociationSetElementParser associationSet;
	private String role;
	private String entitySet;
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();
	private static final String ENTITY_SET = "EntitySet";

	/**
	 * To set associationSet to given UpilEdmxAssociationSet value
	 * 
	 * @param associationSet
	 *           The UpilEdmxAssociationSet
	 */
	public UpilEdmxAssociationSetEndElementParserImpl(final UpilEdmxAssociationSetElementParser associationSet)
	{
		this.associationSet = associationSet;
	}

	@Override
	public void parse(final StartElement el, final XMLEventReader reader)
	{
		this.qName = el.getName();
		setAttributes(el);
	}

	private void setAttributes(final StartElement startElement)
	{
		final Iterator<Attribute> iter = startElement.getAttributes();
		while (iter.hasNext())
		{
			final Attribute att = iter.next();
			this.attributes.add(att);
			final String x = att.getName().getLocalPart();
			if (x.equalsIgnoreCase(ENTITY_SET))
			{
				this.entitySet = att.getValue();
			}
			else if (x.equalsIgnoreCase(UpilEdmxConstants.ROLE))
			{
				this.role = att.getValue();
			}
		}
	}

	public UpilEdmxAssociationSetElementParser getAssociationSet()
	{
		return this.associationSet;
	}

	public String getRole()
	{
		return this.role;
	}

	public String getEntitySet()
	{
		return this.entitySet;
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
