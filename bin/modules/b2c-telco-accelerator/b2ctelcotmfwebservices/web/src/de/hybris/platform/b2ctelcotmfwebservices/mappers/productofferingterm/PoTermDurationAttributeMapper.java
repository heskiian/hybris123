/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingterm;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingTerm;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import ma.glasnost.orika.MappingContext;
import org.springframework.util.ObjectUtils;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Quantity;


/**
 * This attribute Mapper class maps data for duration attribute between {@link SubscriptionTermData} and
 * {@link ProductOfferingTerm}
 *
 * @since 1903
 */
public class PoTermDurationAttributeMapper extends TmaAttributeMapper<SubscriptionTermData, ProductOfferingTerm>
{
	@Override
	public void populateTargetAttributeFromSource(final SubscriptionTermData source, final ProductOfferingTerm target,
			final MappingContext context)
	{
		if (source.getTermOfServiceFrequency() == null || ObjectUtils.isEmpty(source.getTermOfServiceNumber()))
		{
			return;
		}

		final Quantity duration = new Quantity();
		duration.setAmount((float) source.getTermOfServiceNumber());
		duration.setUnits(source.getTermOfServiceFrequency().getCode());
		target.setDuration(duration);
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductOfferingTerm target, final SubscriptionTermData source,
			final MappingContext context)
	{
		if (target.getDuration() == null || target.getDuration().getAmount() == null)
		{
			return;
		}

		source.setTermOfServiceNumber(target.getDuration().getAmount().intValue());
	}
}
