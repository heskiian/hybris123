/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductBundleModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductComponentModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductRefOrValue;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for product attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductChildrenAttributeMapper extends SpiAttributeMapper<SpiProductBundleModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductChildrenAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductBundleModel source, final Product target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProducts()))
		{
			return;
		}

		final List<ProductRefOrValue> productRefOrValues = new ArrayList<>();
		source.getProducts().forEach(spiProductModel -> {
			final ProductRefOrValue productRefOrValue = new ProductRefOrValue();
			productRefOrValue.setId(spiProductModel.getId());
			productRefOrValue.setName(spiProductModel.getName());
			productRefOrValue.setHref(SubscribedproducttmfwebservicesConstants.PRODUCT_API_URL + spiProductModel.getId());
			productRefOrValues.add(productRefOrValue);
		});

		target.setProduct(productRefOrValues);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductBundleModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getProduct()))
		{
			return;
		}

		source.setProducts(getAllProducts(target.getProduct(), context));
	}

	private Set<SpiProductModel> getAllProducts(final List<ProductRefOrValue> productRefOrValues, final MappingContext context)
	{
		final Set<SpiProductModel> result = new HashSet<>();
		productRefOrValues.forEach(productRefOrValue -> {
			SpiProductModel spiProductModel = (SpiProductModel) getSpiGenericService()
					.getItem(SpiProductModel._TYPECODE, productRefOrValue.getId());
			if (spiProductModel == null)
			{
				if (Boolean.TRUE.equals(productRefOrValue.isIsBundle()))
				{
					spiProductModel = (SpiProductModel) getSpiGenericService().createItem(SpiProductBundleModel.class);
				}
				else
				{
					spiProductModel = (SpiProductModel) getSpiGenericService().createItem(SpiProductComponentModel.class);
				}
			}
			getMapperFacade().map(productRefOrValue, spiProductModel, context);
			getSpiGenericService().saveItem(spiProductModel);
			result.add(spiProductModel);
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
