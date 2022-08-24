/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;


/**
 * Populates {@link TmaProductSpecCharacteristicValueUseData} from a {@link TmaProductSpecCharacteristicValueModel} entity.
 *
 * @since 1903
 */
public class TmaProductSpecCharValueUsePopulator
		implements Populator<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueUseData>
{

	/**
	 * Converter from {@link TmaProductSpecCharacteristicValueModel} to {@link TmaProductSpecCharacteristicValueData}
	 */
	private Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> pscvConverter;

	@Override
	public void populate(final TmaProductSpecCharacteristicValueModel source, final TmaProductSpecCharacteristicValueUseData target)
	{
		target.setDescription(source.getDescription());
		target.setValueType(source.getValueType() != null ? source.getValueType().getCode() : null);
		target.setName(source.getId());

		// set product spec characteristic values
		List<TmaProductSpecCharacteristicValueData> pscvList = new ArrayList<>();
		pscvList.add(getPscvConverter().convert(source));
		target.setProductSpecCharacteristicValues(pscvList);
	}

	protected Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> getPscvConverter()
	{
		return pscvConverter;
	}

	public void setPscvConverter(
			Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> pscvConverter)
	{
		this.pscvConverter = pscvConverter;
	}
}
