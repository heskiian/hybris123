/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductRelationshipModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductRelationship;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productRelationship attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductRelationshipAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductRelationshipAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductReferences()))
		{
			return;
		}

		final List<ProductRelationship> productRelationships = new ArrayList<>();
		source.getProductReferences().forEach(spiProductRelationshipModel -> {
			final ProductRelationship productRelationship = getMapperFacade()
					.map(spiProductRelationshipModel, ProductRelationship.class, context);
			productRelationships.add(productRelationship);
		});

		target.setProductRelationship(productRelationships);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getProductRelationship()))
		{
			return;
		}

		source.setProductReferences(getAllProductRelationships(source, target.getProductRelationship(), context));
	}

	private Set<SpiProductRelationshipModel> getAllProductRelationships(final SpiProductModel sourceProduct,
			final List<ProductRelationship> productRelationships, final MappingContext context)
	{
		final Set<SpiProductRelationshipModel> result = new HashSet<>();
		productRelationships.forEach(productRelationship -> {
			final SpiProductRelationshipModel spiProductRelationshipModel = (SpiProductRelationshipModel) getSpiGenericService()
					.createItem(SpiProductRelationshipModel.class);
			getMapperFacade().map(productRelationship, spiProductRelationshipModel, context);
			spiProductRelationshipModel.setProduct(sourceProduct);
			result.add(spiProductRelationshipModel);
		});
		return result;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}
}
