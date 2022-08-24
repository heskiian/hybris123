/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import de.hybris.platform.subscriptionfacades.product.converters.populator.AbstractUsageChargePopulator;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;


/**
 * Populates {@link UsageChargeData} with basic attributes from {@link UsageChargeModel}.
 *
 * @since 1903
 */
public class TmaUsageChargePopulator<SOURCE extends UsageChargeModel, TARGET extends UsageChargeData>
		extends AbstractUsageChargePopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		target.setId(source.getId());
		target.setModifiedTime(source.getModifiedtime());
		target.setItemType(source.getItemtype());
	}
}
