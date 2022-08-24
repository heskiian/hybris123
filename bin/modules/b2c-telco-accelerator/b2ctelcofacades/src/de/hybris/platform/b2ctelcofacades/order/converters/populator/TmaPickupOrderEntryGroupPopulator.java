/*
 * Copyright (c)  2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcofacades.order.TmaOrderEntryFacade;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.commercefacades.order.converters.populator.PickupOrderEntryGroupPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderEntryGroupData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


/**
 * Populator for {@link PointOfServiceData} with details from {@link PointOfServiceModel}.
 *
 * @since 2102
 */
public class TmaPickupOrderEntryGroupPopulator extends PickupOrderEntryGroupPopulator
{
	private TmaOrderEntryFacade orderEntryFacade;

	public TmaPickupOrderEntryGroupPopulator(final TmaOrderEntryFacade orderEntryFacade)
	{
		this.orderEntryFacade = orderEntryFacade;
	}

	@Override
	public long sumOrderGroupQuantity(final OrderEntryGroupData group)
	{
		long sum = 0;
		for (final OrderEntryData entry : group.getEntries())
		{
			if (entry != null)
			{
				sum += entry.getQuantity();
			}
		}
		return sum;
	}

	@Override
	public long sumPickupItemsQuantity(final AbstractOrderModel source)
	{
		long sum = 0;
		for (final AbstractOrderEntryModel entryModel : source.getEntries())
		{
			if (!(entryModel.getProduct() instanceof TmaBundledProductOfferingModel)
					&& entryModel.getDeliveryPointOfService() != null)
			{
				sum += entryModel.getQuantity();
			}
		}
		return sum;
	}

	@Override
	protected OrderEntryData getOrderEntryData(final AbstractOrderData target, final Integer entryNumber)
	{
		return getOrderEntryFacade().getEntry(target.getEntries(), entryNumber);
	}

	protected TmaOrderEntryFacade getOrderEntryFacade()
	{
		return orderEntryFacade;
	}
}
