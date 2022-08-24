/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspeccharvalueuse;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProdSpecCharValueUse;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristicValue;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for pscv attribute between {@link FeatureData} and
 * {@link ProdSpecCharValueUse}
 *
 * @since 1903
 */
public class ClassificationValueUsePscvsAttributeMapper extends TmaAttributeMapper<FeatureData, ProdSpecCharValueUse>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(FeatureData source, ProdSpecCharValueUse target, MappingContext context)
	{
		if (source.getFeatureValues() == null)
		{
			return;
		}

		final List<ProductSpecCharacteristicValue> prodSpecCharValueList = new ArrayList<>();
		source.getFeatureValues().forEach(featureValue -> {
			final ProductSpecCharacteristicValue productSpecCharValue = getMapperFacade()
					.map(featureValue, ProductSpecCharacteristicValue.class, context);
			productSpecCharValue.setValueType(source.getType());
			if (source.getFeatureUnit() != null)
			{
				productSpecCharValue.setUnitOfMeasure(source.getFeatureUnit().getName());
			}

			prodSpecCharValueList.add(productSpecCharValue);
		});

		target.setProductSpecCharacteristicValue(prodSpecCharValueList);
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
