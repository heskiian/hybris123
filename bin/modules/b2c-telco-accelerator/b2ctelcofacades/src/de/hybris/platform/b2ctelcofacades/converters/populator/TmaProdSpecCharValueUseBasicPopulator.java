/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;


/**
 * Populates {@link TmaProductSpecCharacteristicValueUseData} from a {@link TmaProductSpecCharValueUseModel} entity.
 *
 * @since 2105
 */
public class TmaProdSpecCharValueUseBasicPopulator
		implements Populator<TmaProductSpecCharValueUseModel, TmaProductSpecCharacteristicValueUseData>
{

	/**
	 * Converter from {@link TmaProductSpecCharacteristicValueModel} to {@link TmaProductSpecCharacteristicValueData}
	 */
	private Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> pscvConverter;

	public TmaProdSpecCharValueUseBasicPopulator(
			final Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> pscvConverter)
	{
		this.pscvConverter = pscvConverter;
	}

	@Override
	public void populate(final TmaProductSpecCharValueUseModel source, final TmaProductSpecCharacteristicValueUseData target)
	{
		target.setId(source.getId());
		target.setDescription(source.getDescription());
		target.setValueType(source.getValueType() != null ? source.getValueType().getCode() : null);
		target.setName(source.getName());
		target.setMaxCardinality(source.getMaxCardinality());
		target.setMinCardinality(source.getMinCardinality());
		List<TmaProductSpecCharacteristicValueData> pscvList = new ArrayList<>();
		pscvList.addAll(getPscvConverter().convertAll(source.getProductSpecCharacteristicValues()));
		target.setProductSpecCharacteristicValues(pscvList);
	}

	protected Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> getPscvConverter()
	{
		return pscvConverter;
	}
}
