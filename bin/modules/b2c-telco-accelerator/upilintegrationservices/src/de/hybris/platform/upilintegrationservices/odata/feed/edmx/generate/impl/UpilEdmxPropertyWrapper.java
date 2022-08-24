/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate.impl;


import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxPropertyElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxPropertyElementParserImpl;

import java.beans.Introspector;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class for EDMX property wrapper to wrap edmx types to java class types
 *
 * @since 1911
 */
public class UpilEdmxPropertyWrapper
{

	private static final Logger log = LoggerFactory.getLogger(UpilEdmxPropertyWrapper.class);
	private final UpilEdmxPropertyElementParserImpl property;
	private static final String BINARY = "Binary";
	private static final String BYTE_ARRAY = "Byte[]";
	private static final String BOOLEAN = "Boolean";
	private static final String BYTE = "Byte";
	private static final String JAVA_BYTE = "java.lang.Byte";
	private static final String DATE_TIME = "DateTime";
	private static final String JAVA_DATE = "java.util.Date";
	private static final String DATE_TIME_OFFSET = "DateTimeOffset";
	private static final String JODA_DATE_TIME = "org.joda.time.DateTime";
	private static final String DECIMAL = "Decimal";
	private static final String JAVA_BIG_DECIMAL = "java.math.BigDecimal";
	private static final String SINGLE = "Single";
	private static final String FLOAT = "Float";
	private static final String GUID = "Guid";
	private static final String INT16 = "Int16";
	private static final String SHORT = "Short";
	private static final String INT32 = "Int32";
	private static final String INTEGER = "Integer";
	private static final String INT64 = "Int64";
	private static final String LONG = "Long";
	private static final String SBYTE = "SByte";
	private static final String TIME = "Time";
	private static final String JODA_LOCALTIME = "org.joda.time.LocalTime";
	private static final String DOUBLE = "Double";
	private static final String STRING = "String";

	public UpilEdmxPropertyWrapper(final UpilEdmxPropertyElementParser property)
	{
		this.property = (UpilEdmxPropertyElementParserImpl) property;
	}

	/**
	 * To get the java class Type for edmx property type
	 *
	 * @return returns java class type's for edmx property type
	 */
	public String getJavaClassType()
	{

		final String PREFIX = "Edm.";
		String result = "Object";

		final HashMap<String, String> propertyMap = new HashMap<>();
		propertyMap.put(PREFIX + BINARY, BYTE_ARRAY);
		propertyMap.put(PREFIX + BOOLEAN, BOOLEAN);
		propertyMap.put(PREFIX + BYTE, JAVA_BYTE);
		propertyMap.put(PREFIX + DATE_TIME, JAVA_DATE);
		propertyMap.put(PREFIX + DATE_TIME_OFFSET, JODA_DATE_TIME);
		propertyMap.put(PREFIX + DECIMAL, JAVA_BIG_DECIMAL);
		propertyMap.put(PREFIX + SINGLE, FLOAT);
		propertyMap.put(PREFIX + GUID, STRING);
		propertyMap.put(PREFIX + INT16, SHORT);
		propertyMap.put(PREFIX + INT32, INTEGER);
		propertyMap.put(PREFIX + INT64, LONG);
		propertyMap.put(PREFIX + SBYTE, BYTE);
		propertyMap.put(PREFIX + STRING, STRING);
		propertyMap.put(PREFIX + TIME, JODA_LOCALTIME);

		if (propertyMap.get(this.property.getType()) != null)
		{
			result = propertyMap.get(this.property.getType());
		}
		else if (this.property.getType().endsWith(DOUBLE))
		{
			result = DOUBLE;
		}
		else
		{
			log.error("Can not find appropriate type for: {} .Setting to default Object", this.property.getType());
		}

		return result;
	}


	public String getJavaName()
	{
		return Introspector.decapitalize(this.property.getName());
	}

	public String getDefaultValue()
	{
		return this.property.getDefaultValue();
	}

	public String getGetter()
	{
		return UpilEdmxConstants.GET + StringUtils.capitalize(getJavaName());
	}

	public String getSetter()
	{
		return UpilEdmxConstants.SET + StringUtils.capitalize(getJavaName());
	}


}
