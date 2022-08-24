/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderConsignmentPopulator;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Order consignment populator with TUA specific details.
 *
 * @since 2102
 */
public class TmaOrderConsignmentPopulator extends OrderConsignmentPopulator
{
	@Override
	protected void addUnconsignedEntries(final OrderModel source, final OrderData target)
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<>(source.getEntries());
		for (final ConsignmentModel consignmentModel : source.getConsignments())
		{
			if (CollectionUtils.isNotEmpty(consignmentModel.getConsignmentEntries()))
			{
				for (final ConsignmentEntryModel consignmentEntryModel : consignmentModel.getConsignmentEntries())
				{
					entries.remove(consignmentEntryModel.getOrderEntry());
				}
			}
		}

		final List<AbstractOrderEntryModel> unconsignedEntries = entries.stream()
				.filter((AbstractOrderEntryModel entry) -> entry.getMasterEntry() == null)
				.collect(Collectors.toList());

		target.setUnconsignedEntries(Converters.convertAll(unconsignedEntries, getOrderEntryConverter()));
	}
}
