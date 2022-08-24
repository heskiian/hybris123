/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.priceterm;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.QuantityWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for duration attribute between {@link SubscriptionTermData} and
 * {@link ProductOfferingTermWsDTO}
 *
 * @deprecated Since 1911 - use TmaPoTermOfServiceAttributeMapper class instead
 * @since 1907
 */
@Deprecated(since = "1911", forRemoval= true)
public class TmaPoTermDurationAttributeMapper extends TmaAttributeMapper<SubscriptionTermData, ProductOfferingTermWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final SubscriptionTermData source, final ProductOfferingTermWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (source.getTermOfServiceFrequency() == null || ObjectUtils.isEmpty(source.getTermOfServiceNumber()))
		{
			return;
		}

		final QuantityWsDTO duration = new QuantityWsDTO();
		duration.setAmount((long) source.getTermOfServiceNumber());
		duration.setUnits(source.getTermOfServiceFrequency().getCode());
		target.setDuration(duration);
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductOfferingTermWsDTO target, final SubscriptionTermData source,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (target.getDuration() == null || target.getDuration().getAmount() == null)
		{
			return;
		}

		source.setTermOfServiceNumber(target.getDuration().getAmount().intValue());
	}
}
