/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.money;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps price value for cart price attribute between {@link SubscriptionPricePlanData} and
 * {@link MoneyWsDTO}
 *
 * @since 1911
 */
public class TmaMoneyValueAttributeMapper extends TmaAttributeMapper<SubscriptionPricePlanData, MoneyWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final SubscriptionPricePlanData source, final MoneyWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getValue() != null)
		{
			target.setValue(source.getValue().toString());
		}
	}
}
