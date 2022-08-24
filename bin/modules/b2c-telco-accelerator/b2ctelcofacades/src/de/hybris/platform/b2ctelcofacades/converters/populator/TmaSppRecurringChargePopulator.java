/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import static de.hybris.platform.converters.Converters.convertAll;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates {@link SubscriptionPricePlanData#recurringChargeEntries} based on {@link SubscriptionPricePlanModel=#recurringChargeEntries}}
 *
 * @since 2007
 */
public class TmaSppRecurringChargePopulator<SOURCE extends PriceRowModel, TARGET extends SubscriptionPricePlanData>
		implements Populator<SOURCE, TARGET>
{
	private Converter<RecurringChargeEntryModel, RecurringChargeEntryData> recurringChargeEntryConverter;

	public TmaSppRecurringChargePopulator(
			Converter<RecurringChargeEntryModel, RecurringChargeEntryData> recurringChargeEntryConverter)
	{
		this.recurringChargeEntryConverter = recurringChargeEntryConverter;
	}

	@Override
	public void populate(SOURCE source, TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (source instanceof SubscriptionPricePlanModel)
		{
			target.setRecurringChargeEntries(convertAll(((SubscriptionPricePlanModel) source).getRecurringChargeEntries(),
					getRecurringChargeEntryConverter()));
		}
	}

	protected Converter<RecurringChargeEntryModel, RecurringChargeEntryData> getRecurringChargeEntryConverter()
	{
		return recurringChargeEntryConverter;
	}
}
