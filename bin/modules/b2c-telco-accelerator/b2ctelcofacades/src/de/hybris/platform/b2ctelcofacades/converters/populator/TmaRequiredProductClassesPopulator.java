/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.data.TmaProductPriceClassData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductPriceClassModel;
import de.hybris.platform.converters.Populator;


/**
 * Populates {@link TmaProductPriceClassData} with basic attributes from {@link TmaProductPriceClassModel}.
 *
 * @since 1810
 */
public class TmaRequiredProductClassesPopulator<SOURCE extends TmaProductPriceClassModel, TARGET extends TmaProductPriceClassData>
		implements Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);
		target.setId(source.getId());
	}
}
