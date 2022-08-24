/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart.entries;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps term attribute between {@link OrderEntryData} and {@link OrderEntryWsDTO}
 *
 * @since 1911
 */
public class TmaCartEntriesTermAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderEntryWsDTO>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final OrderEntryWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getSubscriptionInfo() == null || source.getSubscriptionInfo().getSubscriptionTerm() == null)
		{
			return;
		}
		final SubscriptionTermData subscriptionTermData = source.getSubscriptionInfo().getSubscriptionTerm();
		final ProductOfferingTermWsDTO productOfferingTermWsDto = getMapperFacade().map(subscriptionTermData,
				ProductOfferingTermWsDTO.class, context);
		target.setSubscriptionTerm(productOfferingTermWsDto);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
