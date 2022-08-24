/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartPrice;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for description attribute between {@link TmaAbstractOrderChargePriceData} and
 * {@link CartPrice}
 *
 * @since 1907
 */
public class CartPriceDescriptionAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderChargePriceData, CartPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderChargePriceData source, final CartPrice target,
			final MappingContext context)
	{
		if (source.getBillingTime() != null)
		{
			target.setDescription(source.getBillingTime().getDescription());
		}
	}
}
