/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.priceterm;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.BillingPlanWsDTO;
import de.hybris.platform.subscriptionfacades.data.BillingPlanData;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps attribute between {@link BillingPlanData} and {@link BillingPlanWsDTO}
 *
 * @since 1911
 */
public class TmaSubscriptionTermBillingPlanAttributeMapper extends TmaAttributeMapper<BillingPlanData, BillingPlanWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final BillingPlanData source, final BillingPlanWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getBillingCycleDay() != null)
		{
			target.setBillingCycleDay(source.getBillingCycleDay());
		}
		if (!ObjectUtils.isEmpty(source.getBillingCycleType()))
		{
			target.setBillingCycleType(source.getBillingCycleType().getCode());
		}
		if (!ObjectUtils.isEmpty(source.getBillingTime()))
		{
			target.setBillingTime(source.getBillingTime().getCode());
		}
	}

}
