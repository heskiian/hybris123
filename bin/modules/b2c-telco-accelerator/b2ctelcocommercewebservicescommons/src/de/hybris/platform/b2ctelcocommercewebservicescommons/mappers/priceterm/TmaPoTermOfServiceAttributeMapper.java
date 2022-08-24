/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.priceterm;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attribute between {@link SubscriptionTermData} and
 * {@link ProductOfferingTermWsDTO}
 *
 * @since 1911
 */
public class TmaPoTermOfServiceAttributeMapper extends TmaAttributeMapper<SubscriptionTermData, ProductOfferingTermWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final SubscriptionTermData source, final ProductOfferingTermWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (!(ObjectUtils.isEmpty(source.getTermOfServiceFrequency())))
		{
			target.setTermOfServiceFrequency(source.getTermOfServiceFrequency().getCode());
		}
		if (!(ObjectUtils.isEmpty(source.getTermOfServiceRenewal())))
		{
			target.setTermOfServiceRenewal(source.getTermOfServiceRenewal().getCode());
		}
		if (source.isCancellable())
		{
			target.setCancellable(Boolean.TRUE);
		}
	}
}
