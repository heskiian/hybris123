/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.objectfactory;

import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.webservicescommons.mapping.config.DynamicTypeFactory;

import ma.glasnost.orika.MappingContext;


/**
 * Object factory used to create destination objects that extend {@link ItemModel}.
 *
 * @since 2111
 */
public class AgrObjectFactory<T extends ItemModel> extends DynamicTypeFactory<T>
{
	private AgrGenericService agrGenericService;

	public AgrObjectFactory(AgrGenericService agrGenericService)
	{
		this.agrGenericService = agrGenericService;
	}

	@Override
	public T create(final Object o, final MappingContext mappingContext)
	{
		if (mappingContext.getResolvedDestinationType() != null && mappingContext.getResolvedDestinationType().getRawType()
				.equals(getType()))
		{
			return (T) getAgrGenericService().createItem(getType());
		}
		else
		{
			return super.create(o, mappingContext);
		}
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}
}
