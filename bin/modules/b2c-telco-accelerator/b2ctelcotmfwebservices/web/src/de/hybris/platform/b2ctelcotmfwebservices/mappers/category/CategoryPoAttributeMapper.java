/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.category;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Category;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for productOffering attribute between {@link CategoryHierarchyData} and {@link Category}
 *
 * @since 1903
 */
public class CategoryPoAttributeMapper extends TmaAttributeMapper<CategoryHierarchyData, Category>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CategoryHierarchyData source, Category target, MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProducts()))
		{
			return;
		}

		final List<ProductOfferingRef> productOfferingRefs = new ArrayList<>();
		source.getProducts().forEach(productData ->
		{
			final ProductOfferingRef productOfferingRef = getMapperFacade().map(productData, ProductOfferingRef.class, context);
			productOfferingRefs.add(productOfferingRef);
		});

		target.setProductOffering(productOfferingRefs);
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
