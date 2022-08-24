/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate.impl;



import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxNavigationPropertyElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxPropertyElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxTypeParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate.UpilEdmxNavigationPropertyWrapper;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate.UpilEdmxTypeWrapper;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxEntityTypeElementParserImpl;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxSchemaElementParserImpl;

import java.beans.Introspector;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilEdmxTypeWrapper}.
 * 
 * @since 1911
 */
public class UpilEdmxTypeWrapperImpl implements UpilEdmxTypeWrapper
{
	private final UpilEdmxTypeParser type;
	private final List<UpilEdmxPropertyWrapper> propWrappers = Lists.newArrayList();
	private final List<UpilEdmxNavigationPropertyWrapper> navPropWrappers = Lists.newArrayList();

	private static final List<String> reservedWords = Lists.newArrayList("assert", "boolean", "break", "byte", "case", "catch",
			"char",
			"class", "const", "continue",
			"default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if",
			"implements", "import",
			"instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return",
			"short", "static",
			"strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile",
			"while");


	private static final String RESERVED = "RESERVED_";

	public UpilEdmxTypeWrapperImpl(final UpilEdmxTypeParser type)
	{
		this.type = type;
		for (final UpilEdmxPropertyElementParser p : this.type.getProperties())
		{
			this.propWrappers.add(new UpilEdmxPropertyWrapper(p));
		}
		if (type instanceof UpilEdmxEntityTypeElementParserImpl)
		{
			final UpilEdmxEntityTypeElementParserImpl et = (UpilEdmxEntityTypeElementParserImpl) type;
			for (final UpilEdmxNavigationPropertyElementParser navProp : et.getNavigationProperties())
			{
				this.navPropWrappers.add(new UpilEdmxNavigationPropertyWrapperImpl(navProp));
			}
		}
	}

	@Override
	public String getJavaName()
	{
		String name = this.type.getName();
		if (isReserved(name))
		{
			name = RESERVED + name;
		}
		return Introspector.decapitalize(name);
	}

	@Override
	public String getClassName()
	{
		return StringUtils.capitalize(getJavaName());
	}

	@Override
	public String getFullClassName()
	{
		return getPackageName() + UpilEdmxConstants.DOT + StringUtils.capitalize(getJavaName());
	}

	@Override
	public String getPackageName()
	{
		String pack = ((UpilEdmxSchemaElementParserImpl) this.type.getSchema()).getNamespace();
		if (isReserved(pack))
		{
			pack = RESERVED + pack;
		}
		return pack;
	}

	@Override
	public String print()
	{
		final StringBuilder bldr = new StringBuilder();
		bldr.append(getFullClassName()).append("\n");
		for (final UpilEdmxPropertyWrapper pw : this.propWrappers)
		{
			bldr.append("\t").append(pw.getJavaClassType()).append(" ").append(pw.getJavaName()).append("\n");
		}
		return bldr.toString();
	}

	private static boolean isReserved(final String name)
	{
		return (reservedWords.stream().anyMatch(name::equals));
	}

	public List<UpilEdmxPropertyWrapper> getPropWrappers()
	{
		return this.propWrappers;
	}

	public List<UpilEdmxNavigationPropertyWrapper> getNavPropWrappers()
	{
		return this.navPropWrappers;
	}

}
