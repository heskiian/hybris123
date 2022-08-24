/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProdSpecCharValueUse;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for classificationAttributeValueUse attribute between {@link ProductData} and
 * {@link ProductOffering}
 *
 * @since 2007
 */
public class PoCavuAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	private MapperFacade mapperFacade;

	public PoCavuAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getClassifications()))
		{
			return;
		}

		final List<ProdSpecCharValueUse> classificationValueUseList = new ArrayList<>();
		source.getClassifications().forEach(classificationData ->
		{
			if (classificationData.getFeatures() != null)
			{
				classificationData.getFeatures().forEach(featureData -> {
					final ProdSpecCharValueUse prodSpecCharValueUseItem = getMapperFacade().map(featureData,
							ProdSpecCharValueUse.class, context);

					classificationValueUseList.add(prodSpecCharValueUseItem);
				});
			}
		});

		target.setClassificationAttributeValueUse(classificationValueUseList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
