/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartterm;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartTerm;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Quantity;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for duration attribute between {@link SubscriptionTermData} and
 * {@link CartTerm}
 *
 * @since 1907
 */
public class CartTermDurationAttributeMapper extends TmaAttributeMapper<SubscriptionTermData, CartTerm>
{
	@Override
	public void populateTargetAttributeFromSource(final SubscriptionTermData source, final CartTerm target,
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
}
