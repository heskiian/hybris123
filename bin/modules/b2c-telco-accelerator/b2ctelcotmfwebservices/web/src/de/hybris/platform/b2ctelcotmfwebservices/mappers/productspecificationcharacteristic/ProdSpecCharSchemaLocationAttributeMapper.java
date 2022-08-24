/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecificationcharacteristic;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristic;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link TmaProductSpecCharacteristicData} and
 * {@link ProductSpecCharacteristic}
 *
 * @since 2102
 */
public class ProdSpecCharSchemaLocationAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecCharacteristicData, ProductSpecCharacteristic>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecCharacteristicData source,
			final ProductSpecCharacteristic target, final MappingContext context)
	{
		target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
	}
}
