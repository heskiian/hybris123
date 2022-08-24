/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferContextData;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.data.TmaPriceContextData;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceDataFactory;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceFacade;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcofacades.strategy.TmaProcessFlowStrategy;
import de.hybris.platform.b2ctelcofacades.strategy.impl.TmaProcessFlowStrategyMapping;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaProductOfferFacade}.
 *
 * @since 6.7
 */
public class DefaultTmaProductOfferFacade implements TmaProductOfferFacade
{
	static final TmaProcessType DEFAULT_PROCESS_TYPE = TmaProcessType.ACQUISITION;

	private TmaPoService tmaPoService;
	private TmaPriceFacade priceFacade;
	private TmaPriceDataFactory priceDataFactory;
	private Converter<ProductModel, ProductData> productConverter;
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;
	private TmaProcessFlowStrategyMapping strategyMapping;
	private Converter<TmaPriceContextData, TmaPriceContext> priceContextDataReverseConverter;
	private TmaCommercePriceService tmaCommercePriceService;
	private Converter<PriceRowModel, SubscriptionPricePlanData> tmaSubscriptionPricePlanConverter;

	@Override
	public ProductData getPoForCode(final String poCode, final Collection<ProductOption> productOptions)
	{
		final TmaProductOfferingModel po = getTmaPoService().getPoForCode(poCode);
		final ProductData productData = getProductConverter().convert(po);

		getProductConfiguredPopulator().populate(po, productData, productOptions);

		return productData;
	}

	@Override
	public ProductData getPoForCode(final String poCode, final Collection<ProductOption> productOptions,
			final TmaPriceContextData priceContextData)
	{
		validateParameterNotNullStandardMessage("priceContext", priceContextData);
		final TmaPriceContext priceContext = getPriceContextDataReverseConverter().convert(priceContextData);
		final TmaProductOfferingModel tmaProductOffering = getTmaPoService().getPoForCodeAndPriceContext(poCode, priceContext);
		final ProductData productData = getProductConverter().convert(tmaProductOffering);

		getProductConfiguredPopulator().populate(tmaProductOffering, productData, productOptions);

		if (CollectionUtils.isEmpty(tmaProductOffering.getParents()))
		{
			return productData;
		}

		setProductDataPrices(poCode, priceContextData, tmaProductOffering, productData);

		return productData;
	}

	@Override
	public List<ProductData> getParentsForCodeAndOptions(final String poCode, final Collection<ProductOption> productOptions)
	{
		final TmaProductOfferingModel po = getTmaPoService().getPoForCode(poCode);
		final Collection<TmaBundledProductOfferingModel> parentBpos = getTmaPoService().getAllParents(po);

		final List<ProductData> productDataList = new ArrayList<>();
		parentBpos.forEach(bpoModel ->
		{
			final ProductData bpoData = getProductConverter().convert(bpoModel);
			getProductConfiguredPopulator().populate(bpoModel, bpoData, productOptions);
			setPoPriceOverrideOnBpo(po, bpoModel, bpoData);
			productDataList.add(bpoData);
		});

		return productDataList;
	}

	@Override
	public boolean isValidParent(final String poCode, final String bpoCode)
	{
		final TmaProductOfferingModel productOfferingModel = getTmaPoService().getPoForCode(poCode);
		final TmaBundledProductOfferingModel bundledProductOfferingModel = getTmaPoService().getBpoForCode(bpoCode);
		return getTmaPoService().isValidParent(productOfferingModel, bundledProductOfferingModel);
	}

	@Override
	public List<TmaOfferData> getOffers(final String processType, final String affectedProductCode, final String bpoCode,
			final Set<String> requiredProductCodes)
	{
		final TmaProcessFlowStrategy strategy = strategyMapping.getStrategyMap().get(processType);
		if (strategy == null)
		{
			return Collections.emptyList();
		}
		return (StringUtils.isEmpty(bpoCode) || CollectionUtils.isEmpty(requiredProductCodes))
				? strategy.getOffersForDeviceOnly(affectedProductCode)
				: strategy.getOffersForDeviceInBpo(affectedProductCode, bpoCode, requiredProductCodes);

	}

	@Override
	public List<TmaOfferData> getOffers(final TmaOfferContextData offerContextData)
	{
		final TmaProcessFlowStrategy strategy = strategyMapping.getStrategyMap().get(offerContextData.getProcessType().getCode());
		if (strategy == null)
		{
			return Collections.emptyList();
		}
		return strategy.getOffers(offerContextData);
	}

	@Override
	public boolean isServiceRequestAction(final TmaSubscribedProductAction action)
	{
		return getTmaPoService().isServiceRequestAction(action);
	}

	@Override
	public boolean isBpo(ProductData productData)
	{
		return TmaBundledProductOfferingModel._TYPECODE.equalsIgnoreCase(productData.getItemType())
				|| TmaFixedBundledProductOfferingModel._TYPECODE.equalsIgnoreCase(productData.getItemType());
	}

	/**
	 * Sets the prices on the product data object as SPP data objects.
	 *
	 * @param poCode the product offering code
	 * @param priceContext the price context
	 * @param productOffering the PO model
	 * @param productData the product data ot be populated with prices
	 */
	protected void setProductDataPrices(final String poCode, final TmaPriceContextData priceContext,
			final TmaProductOfferingModel productOffering,
			final ProductData productData)
	{
		final List<PriceRowModel> priceRowModelList = getPriceRowModels(poCode, priceContext, productOffering);

		if (productData.getProductOfferingPrices() == null)
		{
			productData.setProductOfferingPrices(new ArrayList<>());
		}

		productData.getProductOfferingPrices().addAll(getTmaSubscriptionPricePlanConverter().convertAll(priceRowModelList));
	}

	protected List<PriceRowModel> getPriceRowModels(final String poCode, final TmaPriceContextData priceContext,
			final TmaProductOfferingModel productOffering)
	{
		final List<PriceRowModel> priceRowModelList = new ArrayList<>();
		priceContext.setAffectedProductCode(poCode);
		getTmaPoService().getAllParents(productOffering).stream()
				.filter(parent -> !(parent instanceof TmaFixedBundledProductOfferingModel))
				.forEach((final TmaProductOfferingModel poParent) ->
				{
					priceContext.setProductCode(poParent.getCode());
					priceRowModelList.addAll(getTmaCommercePriceService()
							.filterPricesbyPriceContext(getPriceContextDataReverseConverter().convert(priceContext)));
				});
		return priceRowModelList;
	}

	/**
	 * Sets the price override of the {@link TmaProductOfferingModel} on the BPO provided, price representing the
	 * starting price of the {@param po} if bought as part of the {@param bpoData}
	 *
	 * @param po
	 *           product offering which is the affected product of the price override
	 * @param bpoModel
	 *           bpo as part of which the product will be bought, to retrieve information from
	 * @param bpoData
	 *           bpo to be populated with the price of the {@param po} if bought as part of the {@param bpoData}
	 */
	protected void setPoPriceOverrideOnBpo(final TmaProductOfferingModel po, final TmaBundledProductOfferingModel bpoModel,
			final ProductData bpoData)
	{
		if (!(po instanceof TmaSimpleProductOfferingModel))
		{
			return;
		}
		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(bpoModel)
				.withProcessTypes(new HashSet<>(Arrays.asList(DEFAULT_PROCESS_TYPE)))
				.withtAffectedProduct(po)
				.build();
		final PriceData priceForBpo = getPriceFacade().getMinimumPrice(priceContext);
		if (priceForBpo == null)
		{
			return;
		}
		final SubscriptionPricePlanData computedPrice = getPriceDataFactory().create(
				getPriceDataFactory().getPriceDataTypeForProduct(po), getPriceDataFactory().getValueForPrice(priceForBpo),
				priceForBpo.getCurrencyIso());
		bpoData.setPrice(computedPrice);
	}



	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	@Required
	public void setTmaPoService(final TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	protected ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator()
	{
		return productConfiguredPopulator;
	}

	@Required
	public void setProductConfiguredPopulator(
			final ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	{
		this.productConfiguredPopulator = productConfiguredPopulator;
	}

	protected TmaPriceFacade getPriceFacade()
	{
		return priceFacade;
	}

	@Required
	public void setPriceFacade(final TmaPriceFacade priceFacade)
	{
		this.priceFacade = priceFacade;
	}

	protected TmaPriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final TmaPriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	protected TmaProcessFlowStrategyMapping getStrategyMapping()
	{
		return strategyMapping;
	}

	@Required
	public void setStrategyMapping(final TmaProcessFlowStrategyMapping strategyMapping)
	{
		this.strategyMapping = strategyMapping;
	}

	protected Converter<TmaPriceContextData, TmaPriceContext> getPriceContextDataReverseConverter()
	{
		return priceContextDataReverseConverter;
	}

	@Required
	public void setPriceContextDataReverseConverter(
			final Converter<TmaPriceContextData, TmaPriceContext> priceContextDataReverseConverter)
	{
		this.priceContextDataReverseConverter = priceContextDataReverseConverter;
	}

	protected TmaCommercePriceService getTmaCommercePriceService()
	{
		return tmaCommercePriceService;
	}

	@Required
	public void setTmaCommercePriceService(final TmaCommercePriceService tmaCommercePriceService)
	{
		this.tmaCommercePriceService = tmaCommercePriceService;
	}

	protected Converter<PriceRowModel, SubscriptionPricePlanData> getTmaSubscriptionPricePlanConverter()
	{
		return tmaSubscriptionPricePlanConverter;
	}

	@Required
	public void setTmaSubscriptionPricePlanConverter(
			final Converter<PriceRowModel, SubscriptionPricePlanData> tmaSubscriptionPricePlanConverter)
	{
		this.tmaSubscriptionPricePlanConverter = tmaSubscriptionPricePlanConverter;
	}
}
