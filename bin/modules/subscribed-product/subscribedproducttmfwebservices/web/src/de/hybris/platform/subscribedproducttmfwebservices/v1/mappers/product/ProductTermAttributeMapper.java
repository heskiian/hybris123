/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductTermModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductTerm;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productTerm attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductTermAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductTermAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductTerms()))
		{
			return;
		}

		final List<ProductTerm> productTerms = new ArrayList<>();
		source.getProductTerms().forEach(productTermModel -> {
			final ProductTerm productTerm = getMapperFacade().map(productTermModel, ProductTerm.class, context);
			productTerms.add(productTerm);
		});

		target.setProductTerm(productTerms);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getProductTerm()))
		{
			return;
		}

		source.setProductTerms(getAllProductTerms(target.getProductTerm(), context));
	}

	private Set<SpiProductTermModel> getAllProductTerms(final List<ProductTerm> productTerms, final MappingContext context)
	{
		final Set<SpiProductTermModel> result = new HashSet<>();
		productTerms.forEach(productTerm -> {
			final SpiProductTermModel productTermModel = (SpiProductTermModel) getSpiGenericService()
					.createItem(SpiProductTermModel.class);
			getMapperFacade().map(productTerm, productTermModel, context);
			result.add(productTermModel);
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
