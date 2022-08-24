/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOffering attribute for Subscription between
 * {@link TmaSubscriptionBaseData} and {@link Product}
 *
 * @since 2102
 */
public class ProductPoAttributeMapper extends TmaAttributeMapper<TmaSubscriptionBaseData, Product>
{
	private final MapperFacade mapperFacade;

	public ProductPoAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaSubscriptionBaseData source, final Product target,
			final MappingContext context)
	{
		if (!ObjectUtils.isEmpty(source.getProductOfferingRef()))
		{
			final ProductOfferingRef productOffering = getMapperFacade().map(source.getProductOfferingRef(),
					ProductOfferingRef.class, context);
			target.setProductOffering(productOffering);
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
