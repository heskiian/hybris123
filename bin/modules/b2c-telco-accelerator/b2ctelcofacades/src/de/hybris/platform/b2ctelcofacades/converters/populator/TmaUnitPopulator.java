/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.data.UnitData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.UnitModel;


/**
 * Populates {@link UnitData} with basic attributes from {@link UnitModel}.
 *
 * @since 1810
 */
public class TmaUnitPopulator<SOURCE extends UnitModel, TARGET extends UnitData> implements Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);
		target.setUnitType(source.getUnitType());
	}
}
