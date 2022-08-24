/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscribedproducttmfwebservices.v1.objectfactory;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.webservicescommons.mapping.config.DynamicTypeFactory;

import ma.glasnost.orika.MappingContext;


/**
 * Object factory used to create destination objects that extend {@link ItemModel}.
 *
 * @since 2111
 */
public class SpiObjectFactory<T extends ItemModel> extends DynamicTypeFactory<T>
{
	private SpiGenericService spiGenericService;

	public SpiObjectFactory(final SpiGenericService spiGenericService)
	{
		this.spiGenericService = spiGenericService;
	}

	@Override
	public T create(final Object o, final MappingContext mappingContext)
	{
		if (mappingContext.getResolvedDestinationType() != null && mappingContext.getResolvedDestinationType().getRawType()
				.equals(getType()))
		{
			return (T) getSpiGenericService().createItem(getType());
		}
		else
		{
			return super.create(o, mappingContext);
		}
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}

}
