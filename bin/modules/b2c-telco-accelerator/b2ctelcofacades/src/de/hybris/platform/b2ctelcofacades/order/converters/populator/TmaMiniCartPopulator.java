/*
 * Copyright (c)  2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.commercefacades.order.converters.populator.MiniCartPopulator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import org.apache.commons.collections.CollectionUtils;


/**
 * Mini cart populator with TUA specific details.
 *
 * @since 2102
 */
public class TmaMiniCartPopulator extends MiniCartPopulator
{
	@Override
	protected Integer calcTotalItems(final AbstractOrderModel source)
	{
		return Math.toIntExact(source.getEntries().stream()
				.filter((AbstractOrderEntryModel entry) -> CollectionUtils.isEmpty(entry.getChildEntries()) &&
						!(entry.getProduct() instanceof TmaBundledProductOfferingModel)).count());
	}

	@Override
	protected Integer calcTotalUnitCount(final AbstractOrderModel source)
	{
		return source.getEntries().stream()
				.filter((AbstractOrderEntryModel entry) -> CollectionUtils.isEmpty(entry.getChildEntries()) &&
						!(entry.getProduct() instanceof TmaBundledProductOfferingModel))
				.map(AbstractOrderEntryModel::getQuantity)
				.map(Long::intValue)
				.reduce(0, Integer::sum);
	}
}
