/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populator for {@link TmaCartValidationData}
 *
 * @since 1911
 */
public class TmaCartValidationPopulator implements Populator<TmaCartValidationModel, TmaCartValidationData>
{
	@Override
	public void populate(final TmaCartValidationModel source, final TmaCartValidationData target)
			throws ConversionException
	{
		target.setCode(source.getCode());
		target.setMessage(source.getMessage());
	}
}
