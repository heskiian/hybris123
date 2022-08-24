/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaAllowanceProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcoservices.model.TmaAllowanceProdOfferPriceAlterationModel;
import de.hybris.platform.converters.Populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of {@link TmaAllowanceProdOfferPriceAlterationData} from
 * {@link TmaAllowanceProdOfferPriceAlterationModel}
 *
 * @since 2011.
 */
public class TmaAllowancePopAlterationPopulator<SOURCE extends TmaAllowanceProdOfferPriceAlterationModel,
		TARGET extends TmaAllowanceProdOfferPriceAlterationData> implements Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setAllowanceType(source.getType());
	}
}
