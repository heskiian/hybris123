/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.subscriptionfacades.data.OverageUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.product.converters.populator.OverageUsageChargeEntryPopulator;
import de.hybris.platform.subscriptionservices.model.OverageUsageChargeEntryModel;


/**
 * Populates {@link OverageUsageChargeEntryData} with basic attributes from {@link OverageUsageChargeEntryModel}.
 *
 * @since 1810
 */
public class TmaOverageUsageChargeEntryPopulator<SOURCE extends OverageUsageChargeEntryModel, TARGET extends OverageUsageChargeEntryData>
		extends OverageUsageChargeEntryPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);
		target.setId(source.getId());
		target.setModifiedTime(source.getModifiedtime());
	}
}
