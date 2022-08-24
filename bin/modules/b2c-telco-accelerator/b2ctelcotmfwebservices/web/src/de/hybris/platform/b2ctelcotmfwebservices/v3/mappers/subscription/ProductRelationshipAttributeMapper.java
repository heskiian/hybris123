/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductRelationship;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productRelationship attribute between {@link TmaSubscriptionBaseData} and
 * {@link Product}
 *
 * @since 2102
 */
public class ProductRelationshipAttributeMapper extends TmaAttributeMapper<TmaSubscriptionBaseData, Product>
{
	private final MapperFacade mapperFacade;

	public ProductRelationshipAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaSubscriptionBaseData source, final Product target,
			final MappingContext context)
	{

		if (CollectionUtils.isEmpty(source.getTmaSubscribedProductData()))
		{
			return;
		}

		final List<ProductRelationship> productRelationship = new ArrayList<>();
		source.getTmaSubscribedProductData().forEach(subscribedProduct -> {
			final ProductRelationship relationship = getMapperFacade().map(subscribedProduct, ProductRelationship.class, context);
			productRelationship.add(relationship);

		});

		target.setProductRelationship(productRelationship);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
