/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.specification;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProdSpecCharValueUseWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecCharacteristicValueWsDTO;
import de.hybris.platform.commercefacades.product.data.FeatureData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * TmaPscvAttributeMapper populates value of productSpecCharacteristicValue attribute from {@link FeatureData} to
 * {@link ProdSpecCharValueUseWsDTO}
 *
 * @since 1907
 */
public class TmaPscvAttributeMapper extends TmaAttributeMapper<FeatureData, ProdSpecCharValueUseWsDTO>
{

	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final FeatureData source, final ProdSpecCharValueUseWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (CollectionUtils.isEmpty(source.getFeatureValues()))
		{
			return;
		}

		final List<ProductSpecCharacteristicValueWsDTO> prodSpecCharValueList = new ArrayList<>();

		source.getFeatureValues().forEach(featureValue ->
		{
			final ProductSpecCharacteristicValueWsDTO productSpecCharValue = getMapperFacade().map(featureValue,
					ProductSpecCharacteristicValueWsDTO.class, context);
			productSpecCharValue.setValueType(source.getType());
			if (source.getFeatureUnit() != null)
			{
				productSpecCharValue.setUnitOfMeasure(source.getFeatureUnit().getName());
			}
			prodSpecCharValueList.add(productSpecCharValue);
		});

		target.setProductSpecCharacteristicValue(prodSpecCharValueList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

}
