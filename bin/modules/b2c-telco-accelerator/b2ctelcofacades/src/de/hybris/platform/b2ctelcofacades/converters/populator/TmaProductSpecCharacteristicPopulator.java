/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Populates {@link TmaProductSpecCharacteristicData} from a {@link TmaProductSpecCharacteristicModel} entity.
 *
 * @since 2102
 */
public class TmaProductSpecCharacteristicPopulator
		implements Populator<TmaProductSpecCharacteristicModel, TmaProductSpecCharacteristicData>
{
	private Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> tmaProductSpecCharacteristicValueConverter;

	public TmaProductSpecCharacteristicPopulator(
			final Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> defaultTmaProductSpecCharacteristicValueConverter)
	{
		this.tmaProductSpecCharacteristicValueConverter = defaultTmaProductSpecCharacteristicValueConverter;
	}

	@Override
	public void populate(final TmaProductSpecCharacteristicModel source, final TmaProductSpecCharacteristicData target)
	{
		target.setId(source.getId());
		target.setDescription(source.getDescription());
		target.setName(source.getName());
		target.setConfigurable(source.getConfigurable());

		if (CollectionUtils.isNotEmpty(source.getProductSpecCharacteristicValues()))
		{
			final Set<TmaProductSpecCharacteristicValueData> productSpecCharacteristicValueData = new HashSet<>();
			productSpecCharacteristicValueData
					.addAll(getProductSpecCharacteristicValueConverter().convertAll(source.getProductSpecCharacteristicValues()));
			target.setProductSpecCharacteristicValues(productSpecCharacteristicValueData);
		}
	}

	protected Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> getProductSpecCharacteristicValueConverter()
	{
		return tmaProductSpecCharacteristicValueConverter;
	}
}
