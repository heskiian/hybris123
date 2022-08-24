/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecificationcharacteristic;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristic;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link TmaProductSpecCharacteristicData} and
 * {@link ProductSpecCharacteristic}
 *
 * @since 2102
 */
public class ProdSpecCharAtTypeAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecCharacteristicData, ProductSpecCharacteristic>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecCharacteristicData source,
			final ProductSpecCharacteristic target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
