/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderterm;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderTerm;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Quantity;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import ma.glasnost.orika.MappingContext;
import org.springframework.util.ObjectUtils;


/**
 * This attribute Mapper class maps data for duration attribute between {@link SubscriptionTermData} and
 * {@link OrderTerm}
 *
 * @since 1907
 */
public class OrderTermDurationAttributeMapper extends TmaAttributeMapper<SubscriptionTermData, OrderTerm>
{
	@Override
	public void populateTargetAttributeFromSource(final SubscriptionTermData source, final OrderTerm target,
			final MappingContext context)
	{
		if (source.getTermOfServiceFrequency() == null || ObjectUtils.isEmpty(source.getTermOfServiceNumber()))
		{
			return;
		}

		final Quantity termDuration = new Quantity();
		termDuration.setUnits(source.getTermOfServiceFrequency().getCode());
		termDuration.setAmount((float) source.getTermOfServiceNumber());

		target.setDuration(termDuration);
	}
}
