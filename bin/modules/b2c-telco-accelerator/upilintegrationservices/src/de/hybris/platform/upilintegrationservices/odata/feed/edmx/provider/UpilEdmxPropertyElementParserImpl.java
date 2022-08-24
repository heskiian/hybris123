/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider;


import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxPropertyElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxTypeParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.atom.UpilEdmxNamespaces;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.exception.UpilEdmxAssociationEndCreationException;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxPropertyElementParser}.
 * 
 * @since 1911
 */
public class UpilEdmxPropertyElementParserImpl implements UpilEdmxPropertyElementParser
{

	private static final Logger log = LoggerFactory.getLogger(UpilEdmxPropertyElementParserImpl.class);
	private static final String TRUE = "True";

	private final UpilEdmxTypeParser parentType;
	private String name;
	private Boolean nullable = true;
	private String defaultValue;
	private String maxLength;
	private Boolean fixedLength;
	private Integer precision;
	private Integer scale = 0;
	private Boolean unicode;
	private String type;
	private String mimeType;
	private String collation;
	private String srid;
	private String concurrencyMode = "None";
	private QName qName;
	private final List<Attribute> attributes = Lists.newArrayList();


	public UpilEdmxPropertyElementParserImpl(final UpilEdmxTypeParser parentType)
	{
		this.parentType = parentType;
	}

	@Override
	public void parse(final StartElement startElement, final XMLEventReader xmlEventReader)
	{
		final String printStartElement = UpilEdmxNamespaces.printStartElement(startElement);
		log.trace(printStartElement);
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
			switch ((att.getName().getLocalPart()).toUpperCase(Locale.ENGLISH))
			{
				case "NAME":
					this.name = att.getValue();
					break;
				case "NULLABLE":
					this.nullable = (att.getValue().equalsIgnoreCase(TRUE));
					break;
				case "TYPE":
					this.type = att.getValue();
					break;
				case "MAXLENGTH":
					this.maxLength = att.getValue();
					break;
				case "UNICODE":
					this.unicode = (att.getValue().equalsIgnoreCase(TRUE));
					break;
				case "FIXEDLENGTH":
					this.fixedLength = (att.getValue().equalsIgnoreCase(TRUE));
					break;
				case "SCALE":
					try
					{
						this.scale = Integer.parseInt(att.getValue());
					}
					catch (final NumberFormatException e)
					{
						throw new UpilEdmxAssociationEndCreationException(
								"Error converting to integer attribute [scale]: " + att.getValue());
					}

					break;
				case "PRECISION":
					try
					{
						this.precision = Integer.parseInt(att.getValue());
					}
					catch (final NumberFormatException e)
					{
						throw new UpilEdmxAssociationEndCreationException(
								"Error converting to integer attribute [precision]: " + att.getValue());
					}
					break;
				case "MIMETYPE":
					this.mimeType = att.getValue();
					break;
				case "COLLATION":
					this.collation = att.getValue();
					break;
				case "SRID":
					this.srid = att.getValue();
					break;
				case "CONCURRENCYMODE":
					this.concurrencyMode = att.getValue();
					break;
				default:
					if ((att.getName().getLocalPart()).startsWith("FC_"))
					{
						log.error(name, "{} - Customized Property Mappings are not yet supported...");
					}
			}


		}
	}

	public String getName()
	{
		return this.name;
	}

	public Boolean getNullable()
	{
		return this.nullable;
	}

	public String getDefaultValue()
	{
		return this.defaultValue;
	}

	public String getMaxLength()
	{
		return this.maxLength;
	}

	public Boolean getFixedLength()
	{
		return this.fixedLength;
	}

	public Integer precision()
	{
		return this.precision;
	}

	public Integer getScale()
	{
		return this.scale;
	}

	public Boolean getUnicode()
	{
		return this.unicode;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public UpilEdmxTypeParser getParentType()
	{
		return this.parentType;
	}

	public String getMimeType()
	{
		return this.mimeType;
	}

	public QName getqName()
	{
		return qName;
	}

	public List<Attribute> getAttributes()
	{
		return attributes;
	}

	public String getCollation()
	{
		return collation;
	}

	public String getSrid()
	{
		return srid;
	}

	public String getConcurrencyMode()
	{
		return concurrencyMode;
	}

}
