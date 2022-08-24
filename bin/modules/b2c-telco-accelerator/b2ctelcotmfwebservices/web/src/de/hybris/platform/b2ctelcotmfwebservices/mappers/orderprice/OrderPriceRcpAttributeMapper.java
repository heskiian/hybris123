/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for recurringChargePeriod attribute between {@link TmaAbstractOrderChargePriceData} and
 * {@link OrderPrice}
 *
 * @since 1907
 */
public class OrderPriceRcpAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderChargePriceData, OrderPrice>
{
	@Override
	public void populateTargetAttributeFromSource(TmaAbstractOrderChargePriceData source,
			OrderPrice target, MappingContext context)
	{
		if (source.getBillingTime() != null)
		{
			target.setRecurringChargePeriod(source.getBillingTime().getCode());
		}
	}
}
