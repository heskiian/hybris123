/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspeccharvalueuse;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProdSpecCharValueUse;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristicValue;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for pscv attribute between {@link TmaProductSpecCharacteristicValueUseData} and
 * {@link ProdSpecCharValueUse}
 *
 * @since 1903
 */
public class ProdSpecCharValueUsePscvsAttributeMapper extends TmaAttributeMapper<TmaProductSpecCharacteristicValueUseData, ProdSpecCharValueUse>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(TmaProductSpecCharacteristicValueUseData source, ProdSpecCharValueUse target, MappingContext context)
	{
		if (source.getProductSpecCharacteristicValues() == null)
		{
			return;
		}

		final List<ProductSpecCharacteristicValue> prodSpecCharacteristicValueList = new ArrayList<>();
		source.getProductSpecCharacteristicValues().forEach(productSpecCharacteristicValueData ->
		{
			final ProductSpecCharacteristicValue productSpecCharacteristicValue = getMapperFacade()
					.map(productSpecCharacteristicValueData, ProductSpecCharacteristicValue.class, context);
			prodSpecCharacteristicValueList.add(productSpecCharacteristicValue);
		});

		target.setProductSpecCharacteristicValue(prodSpecCharacteristicValueList);

	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
