/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.QuotePopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.stream.Collectors;


/**
 * Hierarchical Cart populator with TMA specific details.
 *
 * @since 2102
 */
public class TmaHierarchicalQuotePopulator extends QuotePopulator
{

	@Override
	protected void addEntries(final AbstractOrderModel source, final AbstractOrderData prototype)
	{
		prototype.setEntries(getOrderEntryConverter()
				.convertAll(source.getEntries().stream().filter((order -> order.getMasterEntry() == null)).collect(
						Collectors.toList())));
	}
}
