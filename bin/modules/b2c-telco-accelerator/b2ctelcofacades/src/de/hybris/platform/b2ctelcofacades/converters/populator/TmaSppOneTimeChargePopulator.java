/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import static de.hybris.platform.converters.Converters.convertAll;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates {@link SubscriptionPricePlanData#oneTimeChargeEntries} based on {@link SubscriptionPricePlanModel=#_oneTimeCharges}
 *
 * @since 2007
 */
public class TmaSppOneTimeChargePopulator<SOURCE extends PriceRowModel, TARGET extends SubscriptionPricePlanData>
		implements Populator<SOURCE, TARGET>
{
	private Converter<OneTimeChargeEntryModel, OneTimeChargeEntryData> oneTimeChargeEntryConverter;

	public TmaSppOneTimeChargePopulator(Converter<OneTimeChargeEntryModel, OneTimeChargeEntryData> oneTimeChargeEntryConverter)
	{
		this.oneTimeChargeEntryConverter = oneTimeChargeEntryConverter;
	}

	@Override
	public void populate(SOURCE source, TARGET target) 
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (source instanceof SubscriptionPricePlanModel)
		{
			target.setOneTimeChargeEntries(convertAll(((SubscriptionPricePlanModel) source).getOneTimeChargeEntries(),
					getOneTimeChargeEntryConverter()));
		}
	}

	protected Converter<OneTimeChargeEntryModel, OneTimeChargeEntryData> getOneTimeChargeEntryConverter()
	{
		return oneTimeChargeEntryConverter;
	}
}
