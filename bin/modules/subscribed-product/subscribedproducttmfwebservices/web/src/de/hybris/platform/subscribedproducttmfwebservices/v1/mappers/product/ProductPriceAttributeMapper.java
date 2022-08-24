/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiCompositeProdPriceModel;
import de.hybris.platform.subscribedproductservices.model.SpiPriceAlterationModel;
import de.hybris.platform.subscribedproductservices.model.SpiProdPriceChargeModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductPriceModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.PriceAlteration;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductPrice;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productPrice attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductPriceAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductPriceAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductPrices()))
		{
			return;
		}
		final List<ProductPrice> productPrices = new ArrayList<>();
		source.getProductPrices().forEach(productPriceModel -> {
			if (productPriceModel instanceof SpiProdPriceChargeModel)
			{
				productPrices.add(getMapperFacade().map(productPriceModel, ProductPrice.class, context));
				return;
			}
			if (productPriceModel instanceof SpiCompositeProdPriceModel)
			{
				if (CollectionUtils.isEmpty(((SpiCompositeProdPriceModel) productPriceModel).getChildPrices()))
				{
					return;
				}
				productPrices.addAll(getProductPricesFromComposite((SpiCompositeProdPriceModel) productPriceModel, context));
			}
		});

		target.setProductPrice(productPrices);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getProductPrice()))
		{
			return;
		}

		final Set<SpiProductPriceModel> productPriceModels = new HashSet<>();

		target.getProductPrice().forEach(productPrice -> {
			if (CollectionUtils.isEmpty(productPrice.getProductPriceAlteration()))
			{
				final SpiProdPriceChargeModel prodPriceChargeModel = (SpiProdPriceChargeModel) getSpiGenericService()
						.createItem(SpiProdPriceChargeModel.class);
				getMapperFacade().map(productPrice, prodPriceChargeModel, context);
				productPriceModels.add(prodPriceChargeModel);
			}
			else
			{
				final SpiCompositeProdPriceModel compositeProdPriceModel = (SpiCompositeProdPriceModel) getSpiGenericService()
						.createItem(SpiCompositeProdPriceModel.class);
				getMapperFacade().map(productPrice, compositeProdPriceModel, context);
				compositeProdPriceModel.setChildPrices(getAlterationsFromDto(productPrice.getProductPriceAlteration(), context));
				productPriceModels.add(compositeProdPriceModel);
			}
		});

		source.setProductPrices(productPriceModels);
	}

	private List<ProductPrice> getProductPricesFromComposite(final SpiCompositeProdPriceModel parent, final MappingContext context)
	{
		final List<ProductPrice> productPrices = new ArrayList<>();
		final List<PriceAlteration> priceAlterations = new ArrayList<>();
		parent.getChildPrices().forEach(spiProductPriceModel -> {
			if (spiProductPriceModel instanceof SpiProdPriceChargeModel)
			{
				productPrices.add(getMapperFacade().map(spiProductPriceModel, ProductPrice.class, context));
				return;
			}

			if (spiProductPriceModel instanceof SpiPriceAlterationModel)
			{
				priceAlterations.add(getMapperFacade().map(spiProductPriceModel, PriceAlteration.class, context));
				return;
			}
			if (spiProductPriceModel instanceof SpiCompositeProdPriceModel)
			{
				productPrices
						.addAll(getProductPricesFromComposite((SpiCompositeProdPriceModel) spiProductPriceModel, context));
			}
		});
		if (CollectionUtils.isNotEmpty(priceAlterations))
		{
			final ProductPrice compositeProductPrice = getMapperFacade().map(parent, ProductPrice.class, context);
			compositeProductPrice.setProductPriceAlteration(priceAlterations);
			productPrices.add(compositeProductPrice);
		}
		return productPrices;
	}

	private Set<SpiProductPriceModel> getAlterationsFromDto(final List<PriceAlteration> priceAlterations,
			final MappingContext context)
	{
		final Set<SpiProductPriceModel> priceAlterationModels = new HashSet<>();
		priceAlterations.forEach(priceAlteration ->
				{
					final SpiPriceAlterationModel priceAlterationModel = (SpiPriceAlterationModel) getSpiGenericService()
							.createItem(SpiPriceAlterationModel.class);
					getMapperFacade().map(priceAlteration, priceAlterationModel, context);
					priceAlterationModels.add(priceAlterationModel);
				}
		);

		return priceAlterationModels;
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
