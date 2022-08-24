/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.objectfactory;

import de.hybris.platform.agreementtmfwebservices.v1.dto.Quantity;
import de.hybris.platform.webservicescommons.mapping.config.DynamicTypeFactory;

import ma.glasnost.orika.MappingContext;

/**
 * Object factory used to create destination objects for properties of type {@link Quantity}.
 *
 * @since 2111
 */
public class AgrQuantityFactory extends DynamicTypeFactory
{

	@Override
	public Object create(final Object o, final MappingContext mappingContext)
	{
		if (o instanceof Quantity)
		{
			return Long.valueOf(0);
		}
		else
		{
			return super.create(o, mappingContext);
		}
	}
}
