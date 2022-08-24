/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecificationcharacteristic;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristic;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristicValue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productSpecCharacteristicValue attribute between {@link TmaProductSpecCharacteristicData} and
 * {@link ProductSpecCharacteristic}
 *
 * @since 2102
 */
public class ProdSpecCharPscvsAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecCharacteristicData, ProductSpecCharacteristic>
{
	private MapperFacade mapperFacade;

	public ProdSpecCharPscvsAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecCharacteristicData source,
			final ProductSpecCharacteristic target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductSpecCharacteristicValues()))
		{
			return;
		}

		final List<ProductSpecCharacteristicValue> prodSpecCharacteristicValueList = new ArrayList<>();
		source.getProductSpecCharacteristicValues().forEach(productSpecCharacteristicValueData ->
		{
			final ProductSpecCharacteristicValue productSpecCharacteristicValue =
					getMapperFacade().map(productSpecCharacteristicValueData, ProductSpecCharacteristicValue.class, context);
			prodSpecCharacteristicValueList.add(productSpecCharacteristicValue);
		});

		target.setProductSpecCharacteristicValue(prodSpecCharacteristicValueList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
