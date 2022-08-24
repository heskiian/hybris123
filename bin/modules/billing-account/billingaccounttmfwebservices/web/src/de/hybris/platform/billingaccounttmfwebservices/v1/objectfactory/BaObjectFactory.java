/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.objectfactory;

import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.webservicescommons.mapping.config.DynamicTypeFactory;

import ma.glasnost.orika.MappingContext;


/**
 * Object factory used to create destination objects that extend {@link ItemModel}.
 *
 * @since 2111
 */
public class BaObjectFactory<T extends ItemModel> extends DynamicTypeFactory<T>
{
	private BaGenericService baGenericService;

	public BaObjectFactory(final BaGenericService baGenericService)
	{
		this.baGenericService = baGenericService;
	}

	@Override
	public T create(final Object o, final MappingContext mappingContext)
	{
		if (mappingContext.getResolvedDestinationType() != null && mappingContext.getResolvedDestinationType().getRawType()
				.equals(getType()))
		{
			return (T) getBaGenericService().createItem(getType());
		}
		else
		{
			return super.create(o, mappingContext);
		}
	}

	public BaGenericService getBaGenericService()
	{
		return baGenericService;
	}
}
