/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderEntryPscvData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderEntryPscvModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populates {@link TmaAbstractOrderEntryPscvData} based on {@link TmaAbstractOrderEntryPscvModel}.
 *
 * @since 1911
 */
public class TmaAbstractOrderEntryPscvPopulator
		implements Populator<TmaAbstractOrderEntryPscvModel, TmaAbstractOrderEntryPscvData>
{
	@Override
	public void populate(final TmaAbstractOrderEntryPscvModel source, final TmaAbstractOrderEntryPscvData target)
			throws ConversionException
	{
		target.setName(source.getName());
		target.setValue(source.getValue());
	}
}
