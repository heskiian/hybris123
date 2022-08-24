/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.ConsignmentPopulator;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * Consignment populator with TUA specific details.
 *
 * @since 2102
 */
public class TmaConsignmentPopulator extends ConsignmentPopulator
{

	@Override
	public void populate(final ConsignmentModel source, final ConsignmentData target)
	{
		super.populate(source, target);

		final Set<ConsignmentEntryModel> consignmentEntries = source.getConsignmentEntries().stream()
				.filter((ConsignmentEntryModel consignmentEntry) -> consignmentEntry.getOrderEntry().getMasterEntry() == null)
				.collect(Collectors.toSet());

		target.setEntries(Converters.convertAll(consignmentEntries, getConsignmentEntryConverter()));
		target.setStatusDisplay(source.getStatusDisplay());
	}
}
