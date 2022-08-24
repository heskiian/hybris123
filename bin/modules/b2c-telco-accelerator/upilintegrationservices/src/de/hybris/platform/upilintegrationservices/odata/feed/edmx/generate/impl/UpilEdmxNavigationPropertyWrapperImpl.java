/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate.impl;


import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxNavigationPropertyElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.constants.UpilEdmxConstants;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.exception.UpilEdmxAssociationEndCreationException;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate.UpilEdmxNavigationPropertyWrapper;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxAssociationElementParserImpl;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxAssociationEndElementParserImpl;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxNavigationPropertyElementParserImpl;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxSchemaElementParserImpl;

import java.beans.Introspector;

import org.apache.commons.lang.StringUtils;


/**
 * Default implementation of the {@link UpilEdmxNavigationPropertyWrapper}.
 * 
 * @since 1911
 */
public class UpilEdmxNavigationPropertyWrapperImpl implements UpilEdmxNavigationPropertyWrapper
{
	private final UpilEdmxNavigationPropertyElementParserImpl navProperty;
	private final UpilEdmxAssociationElementParserImpl association;


	public UpilEdmxNavigationPropertyWrapperImpl(final UpilEdmxNavigationPropertyElementParser navProperty)
	{
		this.navProperty = (UpilEdmxNavigationPropertyElementParserImpl) navProperty;
		final UpilEdmxSchemaElementParserImpl schema = (UpilEdmxSchemaElementParserImpl) this.navProperty.getEntityType()
				.getSchema();
		this.association = schema
				.getAssociationByName(((UpilEdmxNavigationPropertyElementParserImpl) navProperty).getRelationship());
	}

	@Override
	public String getJavaNavType()
	{
		final UpilEdmxAssociationEndElementParserImpl toEnd = getToEndRole();
		String type = toEnd.getType();
		final UpilEdmxSchemaElementParserImpl schema = (UpilEdmxSchemaElementParserImpl) this.navProperty.getEntityType()
				.getSchema();
		final String namespace = schema.getNamespace();
		final String packageName = namespace + UpilEdmxConstants.DOT;
		if (StringUtils.startsWith(type, packageName))
		{
			final String temp = type.substring(packageName.length());
			type = temp;
		}
		return type;
	}

	@Override
	public boolean isMultiple()
	{
		final UpilEdmxAssociationEndElementParserImpl toEnd = getToEndRole();
		return UpilEdmxConstants.ASTERISK_SYMBOL.equals(toEnd.getMultiplicity());
	}

	@Override
	public String getJavaName()
	{
		return Introspector.decapitalize(this.navProperty.getName());
	}

	@Override
	public String getGetter()
	{
		return UpilEdmxConstants.GET + StringUtils.capitalize(getJavaName());
	}

	@Override
	public String getSetter()
	{
		return UpilEdmxConstants.SET + StringUtils.capitalize(getJavaName());
	}


	public UpilEdmxAssociationElementParserImpl getAssociation()
	{
		return this.association;
	}


	private UpilEdmxAssociationEndElementParserImpl getToEndRole()
	{
		final String toRole = this.navProperty.getToRole();
		for (final UpilEdmxAssociationEndElementParserImpl end : this.association.getEnds())
		{
			if (end.getRole().equals(toRole))
			{
				return end;
			}
		}
		throw new UpilEdmxAssociationEndCreationException(
				"Error creating navigation property for: " + this.navProperty.getName() + " while looking for nav toRole: " + toRole);
	}



}
