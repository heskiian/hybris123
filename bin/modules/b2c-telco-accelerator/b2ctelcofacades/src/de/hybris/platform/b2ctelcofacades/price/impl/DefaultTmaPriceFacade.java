/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaComponentProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceDataFactory;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceFacade;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link TmaPriceFacade}
 *
 * @since 1903.
 */
public class DefaultTmaPriceFacade implements TmaPriceFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaPriceFacade.class.getName());

	private TmaCommercePriceService commercePriceService;
	private TmaPriceDataFactory tmaPriceDataFactory;

	private Converter<PriceRowModel, SubscriptionPricePlanData> subscriptionPricePlanConverter;
	private Converter<PriceRowModel, PriceData> priceDataConverter;

	public DefaultTmaPriceFacade()
	{
	}

	public DefaultTmaPriceFacade(final Converter<PriceRowModel, PriceData> priceDataConverter)
	{
		this.priceDataConverter = priceDataConverter;
	}

	@Override
	public PriceData getMinimumPrice(final TmaPriceContext priceContext)
	{
		final PriceRowModel priceRowModel = getCommercePriceService().getMinimumPrice(priceContext);
		return createPriceDataFromModel(priceContext, priceRowModel);
	}

	@Override
	public PriceData getBestApplicablePrice(final TmaPriceContext priceContext)
	{
		final PriceRowModel priceRowModel = getCommercePriceService().getBestApplicablePrice(priceContext);
		return createPriceDataFromModel(priceContext, priceRowModel);
	}

	/**
	 * {@inheritDoc}
	 * In case there is no price component of that type found in the structure of the product offering price given an empty list
	 * is returned.
	 */
	@Override
	public <T extends TmaComponentProdOfferPriceData> List<T> getListOfPriceComponents(
			final TmaProductOfferingPriceData productOfferingPrice,
			Class<T> componentPriceType)
	{
		List<T> result = new ArrayList<>();
		processProductOfferingPrice(productOfferingPrice, componentPriceType, result);

		return result;
	}

	protected ProductModel getProduct(final TmaPriceContext priceContext)
	{
		final ProductModel contextProduct = priceContext.getProduct();
		return contextProduct instanceof TmaBundledProductOfferingModel ? priceContext.getAffectedProduct() : contextProduct;
	}

	@SuppressWarnings({ "unchecked", "", "ConstantConditions" })
	private <T extends TmaComponentProdOfferPriceData> void processProductOfferingPrice(final TmaProductOfferingPriceData inputPop,
			Class<T> componentPriceType, List<T> componentPrices)
	{
		if (inputPop instanceof TmaCompositeProdOfferPriceData)
		{
			for (TmaProductOfferingPriceData child : ((TmaCompositeProdOfferPriceData) inputPop).getChildren())
			{
				processProductOfferingPrice(child, componentPriceType, componentPrices);
			}
		}

		if (componentPriceType.isInstance(inputPop))
		{
			componentPrices.add((T) inputPop);
		}
	}

	@Override
	public PriceDataType getPriceDataType(ProductModel product)
	{
		return CollectionUtils.isNotEmpty(product.getVariants()) ? PriceDataType.FROM : PriceDataType.BUY;
	}

	private PriceData createPriceDataFromModel(final TmaPriceContext priceContext,
			final PriceRowModel priceRowModel)
	{
		if (priceRowModel == null)
		{
			return null;
		}

		final PriceData priceData = getPriceDataConverter().convert(priceRowModel);

		if (priceData == null)
		{
			LOG.debug("Price data object cannot be created for price model with ID " + priceRowModel.getCode());
			return null;
		}

		priceData.setPriceType(getPriceDataType(getProduct(priceContext)));

		return priceData;
	}

	protected TmaCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	@Required
	public void setCommercePriceService(final TmaCommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}

	protected TmaPriceDataFactory getTmaPriceDataFactory()
	{
		return tmaPriceDataFactory;
	}

	@Required
	public void setTmaPriceDataFactory(final TmaPriceDataFactory tmaPriceDataFactory)
	{
		this.tmaPriceDataFactory = tmaPriceDataFactory;
	}

	protected Converter<PriceRowModel, SubscriptionPricePlanData> getSubscriptionPricePlanConverter()
	{
		return subscriptionPricePlanConverter;
	}

	@Required
	public void setSubscriptionPricePlanConverter(
			final Converter<PriceRowModel, SubscriptionPricePlanData> subscriptionPricePlanConverter)
	{
		this.subscriptionPricePlanConverter = subscriptionPricePlanConverter;
	}

	protected Converter<PriceRowModel, PriceData> getPriceDataConverter()
	{
		return priceDataConverter;
	}
}
