/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx;


import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxComplexTypeElementParserImpl;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


/**
 * Abstract class to parser upil edmx type elements
 * 
 * @since 1911
 */
public abstract class UpilEdmxTypeParser
{

	final Logger log = LoggerFactory.getLogger(UpilEdmxTypeParser.class);

	private static final String BASE_TYPE = "BaseType";
	private static final String ABSTRACT = "Abstract";
	private static final String ANNOTATION_ATTRIBUTE = "AnnotationAttribute";
	protected List<UpilEdmxPropertyElementParser> properties = Lists.newArrayList();
	protected List<UpilEdmxComplexTypeElementParserImpl> complexTypes = Lists.newArrayList();
	protected String documentation;
	protected String name;
	protected UpilEdmxSchemaElementParser schema;
	protected Boolean abstractType = false;
	protected String baseType;
	protected String annotationAttribute;
	protected QName qName;
	protected final List<Attribute> attributes = Lists.newArrayList();

	/**
	 * To parse Edmx type
	 * 
	 * @param startElement
	 *           Start element
	 * @param xmlEventReader
	 *           XML event reader
	 * @throws XMLStreamException
	 *            Exception thrown by this method
	 */
	public abstract void parse(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException;

	protected void setAttributes(final StartElement startElement)
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
			else if (att.getName().getLocalPart().equalsIgnoreCase(BASE_TYPE))
			{
				this.baseType = att.getValue();
			}
			else if (att.getName().getLocalPart().equalsIgnoreCase(ABSTRACT))
			{
				this.abstractType = BooleanUtils.toBooleanObject(att.getValue());
			}
			else if (att.getName().getLocalPart().equalsIgnoreCase(ANNOTATION_ATTRIBUTE))
			{
				this.annotationAttribute = att.getValue();
			}

		}
	}


	public String getBaseType()
	{
		return baseType;
	}


	public QName getqName()
	{
		return qName;
	}


	public List<Attribute> getAttributes()
	{
		return attributes;
	}

	public List<UpilEdmxPropertyElementParser> getProperties()
	{
		return this.properties;
	}


	public String getName()
	{
		return this.name;
	}


	public UpilEdmxSchemaElementParser getSchema()
	{
		return this.schema;
	}


	public List<UpilEdmxComplexTypeElementParserImpl> getComplexTypes()
	{
		return complexTypes;
	}


	public String getDocumentation()
	{
		return documentation;
	}


	public Boolean isAbstractType()
	{
		return abstractType;
	}


	public String getAnnotationAttribute()
	{
		return annotationAttribute;
	}



}
