/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.objectfactory;


import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Money;
import de.hybris.platform.webservicescommons.mapping.config.DynamicTypeFactory;

import ma.glasnost.orika.MappingContext;


/**
 * Object factory used to create destination objects for properties of type {@link Double}.
 *
 * @since 2111
 */
public class SpiMoneyFactory extends DynamicTypeFactory
{

	@Override
	public Object create(final Object o, final MappingContext mappingContext)
	{
		if (o instanceof Money || o instanceof Float)
		{
			return (double) 0;
		}
		else
		{
			return super.create(o, mappingContext);
		}
	}
}
