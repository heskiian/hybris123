/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.product.converters.populator.RecurringChargeEntryPopulator;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;


/**
 * Populates {@link RecurringChargeEntryData} with basic attributes from {@link RecurringChargeEntryModel}.
 *
 * @since 1810
 */
public class TmaRecurringChargeEntryPopulator<SOURCE extends RecurringChargeEntryModel, TARGET extends RecurringChargeEntryData>
		extends RecurringChargeEntryPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);
		target.setId(source.getId());
		target.setModifiedTime(source.getModifiedtime());
		super.populate(source, target);
	}
}
