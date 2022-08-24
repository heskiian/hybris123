/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart.entries;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps processType attributes between {@link OrderEntryData} and {@link OrderEntryWsDTO}
 *
 * @since 1911
 */
public class TmaCartEntriesRootBpoCodeAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderEntryWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final OrderEntryWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getBpo() != null)
		{
			target.setRootBpoCode(source.getBpo().getCode());
		}
	}
}

