/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates product spec characteristics information from {@link TmaProductSpecificationModel} to
 * {@link TmaProductSpecificationData}.
 *
 * @since 2102
 */
public class TmaProductSpecPscPopulator implements Populator<TmaProductSpecificationModel, TmaProductSpecificationData>
{
	private Converter<TmaProductSpecCharacteristicModel, TmaProductSpecCharacteristicData> tmaProductSpecCharacteristicConverter;

	public TmaProductSpecPscPopulator(
			final Converter<TmaProductSpecCharacteristicModel, TmaProductSpecCharacteristicData> tmaProductSpecCharacteristicConverter)
	{
		this.tmaProductSpecCharacteristicConverter = tmaProductSpecCharacteristicConverter;
	}

	@Override
	public void populate(final TmaProductSpecificationModel source, final TmaProductSpecificationData target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		if (CollectionUtils.isNotEmpty(source.getProductSpecCharacteristics()))
		{
			final Set<TmaProductSpecCharacteristicData> productSpecCharacteristicData = new HashSet<>();
			productSpecCharacteristicData
					.addAll(getTmaProductSpecCharacteristicConverter().convertAll(source.getProductSpecCharacteristics()));
			target.setProductSpecCharacteristic(productSpecCharacteristicData);
		}
	}

	protected Converter<TmaProductSpecCharacteristicModel, TmaProductSpecCharacteristicData> getTmaProductSpecCharacteristicConverter()
	{
		return tmaProductSpecCharacteristicConverter;
	}
}
