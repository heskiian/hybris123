/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspeccharvalue;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristicValue;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link TmaProductSpecCharacteristicValueData} and
 * {@link ProductSpecCharacteristicValue}
 *
 * @since 1903
 */
public class PscvSchemaLocationAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecCharacteristicValueData, ProductSpecCharacteristicValue>
{

	@Override
	public void populateTargetAttributeFromSource(TmaProductSpecCharacteristicValueData productSpecCharacteristicValueData,
			ProductSpecCharacteristicValue productSpecCharacteristicValue, MappingContext context)
	{
		productSpecCharacteristicValue.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
	}
}
