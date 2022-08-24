/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcoservices.model.TmaProdOfferPriceAlterationModel;
import de.hybris.platform.converters.Populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of {@link TmaProdOfferPriceAlterationData} from {@link TmaProdOfferPriceAlterationModel}
 *
 * @since 2007.
 */
public class TmaProdOfferPriceAlterationPopulator<SOURCE extends TmaProdOfferPriceAlterationModel,
		TARGET extends TmaProdOfferPriceAlterationData> implements Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setCycleStart(source.getCycleStart());
		target.setCycleEnd(source.getCycleEnd());
		target.setIsPercentage(source.getIsPercentage());
	}
}
