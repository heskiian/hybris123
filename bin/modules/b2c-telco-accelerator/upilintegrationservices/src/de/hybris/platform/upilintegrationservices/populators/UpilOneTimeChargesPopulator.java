/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgOneTimeType;

import java.math.BigDecimal;

import org.springframework.util.ObjectUtils;


/**
 * Populates {@link OneTimeChargeEntryModel} to {@link I_UtilsProdChgOneTimeType}
 * 
 * @since 1911
 */

public class UpilOneTimeChargesPopulator implements Populator<OneTimeChargeEntryModel, I_UtilsProdChgOneTimeType>
{

	@Override
	public void populate(final OneTimeChargeEntryModel source, final I_UtilsProdChgOneTimeType target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);
		target.setUtilitiesProduct(source.getSubscriptionPricePlanOneTime().getCode());
		target.setUtilsPriceAmount(BigDecimal.valueOf(source.getPrice()));
		if (!ObjectUtils.isEmpty(source.getCurrency()))
		{
			target.setUtilsPriceCurrency(source.getCurrency().getIsocode());
		}
		if (!ObjectUtils.isEmpty(source.getSemantics()))
		{
			target.setUtilsSemanticsName1(source.getSemantics().getSemanticsName1());
			target.setUtilsSemanticsName2(source.getSemantics().getSemanticsName2());
		}
	}
}
