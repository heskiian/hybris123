/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.product.converters.populator.OneTimeChargeEntryPopulator;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;


/**
 * Populates {@link OneTimeChargeEntryData} with basic attributes from {@link OneTimeChargeEntryModel}.
 *
 * @since 1810
 */
public class TmaOneTimeChargeEntryPopulator<SOURCE extends OneTimeChargeEntryModel, TARGET extends OneTimeChargeEntryData>
		extends OneTimeChargeEntryPopulator<SOURCE, TARGET>
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
