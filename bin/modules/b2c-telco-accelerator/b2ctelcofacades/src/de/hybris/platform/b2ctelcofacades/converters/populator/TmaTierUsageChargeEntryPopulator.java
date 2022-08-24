/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.product.converters.populator.TierUsageChargeEntryPopulator;
import de.hybris.platform.subscriptionservices.model.TierUsageChargeEntryModel;


/**
 * Populates {@link TierUsageChargeEntryData} with basic attributes from {@link TierUsageChargeEntryModel}.
 *
 * @since 1810
 */
public class TmaTierUsageChargeEntryPopulator<SOURCE extends TierUsageChargeEntryModel, TARGET extends TierUsageChargeEntryData>
		extends TierUsageChargeEntryPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);
		target.setId(source.getId());
		target.setModifiedTime(source.getModifiedtime());
	}
}
