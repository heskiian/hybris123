/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;

import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for isBundle attribute between {@link TmaSubscriptionBaseData} and
 * {@link Product}
 *
 * @since 2102
 */
public class ProductIsBundleAttributeMapper extends TmaAttributeMapper<TmaSubscriptionBaseData, Product>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaSubscriptionBaseData source, final Product target,
			final MappingContext context)
	{
		if (CollectionUtils.isNotEmpty(source.getTmaSubscribedProductData()))
		{
			final Optional<TmaSubscribedProductData> bundledSubscribedProduct = source.getTmaSubscribedProductData().stream()
					.filter(subscribedProductData -> StringUtils.isNotEmpty(subscribedProductData.getParentPOCode())).findFirst();

			target.isBundle(bundledSubscribedProduct.isPresent());
		}
	}
}
