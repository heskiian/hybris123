/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaRecurringProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;
import de.hybris.platform.converters.Populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the cycle attributes of {@link TmaRecurringProdOfferPriceChargeData} from
 * {@link TmaRecurringProdOfferPriceChargeModel}.
 *
 * @since 2007
 */
public class TmaRecurringProdOfferPricePopulator<SOURCE extends TmaRecurringProdOfferPriceChargeModel,
		TARGET extends TmaRecurringProdOfferPriceChargeData> implements Populator<SOURCE, TARGET>
{
	@Override
	public void populate(SOURCE source, TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setCycleStart(source.getCycleStart());
		if (source.getCycleEnd() != null)
		{
			target.setCycleEnd(source.getCycleEnd());
		}
	}
}
