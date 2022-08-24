/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.commercefacades.order.converters.populator.DeliveryOrderEntryGroupPopulator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;


/**
 * Delivery order items populator with TUA specific details.
 *
 * @since 2102
 */
public class TmaDeliveryOrderEntryGroupPopulator extends DeliveryOrderEntryGroupPopulator
{
	@Override
	public long sumDeliveryItemsQuantity(final AbstractOrderModel source)
	{
		long sum = 0;
		for (final AbstractOrderEntryModel entryModel : source.getEntries())
		{
			if (!(entryModel.getProduct() instanceof TmaBundledProductOfferingModel)
					&& entryModel.getDeliveryPointOfService() == null)
			{
				sum += entryModel.getQuantity();
			}
		}
		return sum;
	}
}
