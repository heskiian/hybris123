/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.product;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BillingAccountRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for billingAccount attribute between {@link TmaSubscribedProductData} and
 * {@link Product}
 *
 * @since 2102
 */
public class ProductBillingAccountRefAttributeMapper extends TmaAttributeMapper<TmaSubscribedProductData, Product>
{
	private final MapperFacade mapperFacade;

	public ProductBillingAccountRefAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaSubscribedProductData source, final Product target,
			final MappingContext context)
	{
		if (source.getBillingAccountRef() == null)
		{
			return;
		}
		if (source.getBillingAccountRef() != null)
		{
			target.setBillingAccount(getMapperFacade().map(source.getBillingAccountRef(), BillingAccountRef.class));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
