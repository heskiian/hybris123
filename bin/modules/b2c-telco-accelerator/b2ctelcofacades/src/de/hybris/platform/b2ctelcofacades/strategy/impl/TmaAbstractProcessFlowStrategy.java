/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.strategy.impl;


import de.hybris.platform.b2ctelcofacades.data.TmaOfferContextData;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceFacade;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcofacades.strategy.TmaProcessFlowStrategy;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.b2ctelcoservices.constants.B2ctelcoservicesConstants.SELECTED_PROCESS_TYPE;


/**
 * Base class for particular strategies' implementations.
 *
 * @since 6.7
 */
public abstract class TmaAbstractProcessFlowStrategy implements TmaProcessFlowStrategy
{
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.STOCK, ProductOption.SPO_BUNDLE_TABS,
			ProductOption.PRODUCT_OFFERING_PRICES);

	protected static final List<ProductOption> BPO_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	private TmaProductOfferFacade productOfferFacade;
	private TmaPoService poService;
	private TmaSubscriptionTermService subscriptionTermService;
	private SessionService sessionService;
	private final UserService userService;

	private TmaPriceFacade tmaPriceFacade;
	private Converter<ProductData, TmaOfferData> tmaOfferConverter;

	public TmaAbstractProcessFlowStrategy(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * Creates a {@link Set} of {@link TmaProductOfferingModel} required products, used for obtaining price override.
	 *
	 * @param requiredProductCode
	 * 		code of the required product
	 * @return a {@link Set} of {@link TmaProductOfferingModel}
	 */
	protected abstract Set<TmaProductOfferingModel> getRequiredProducts(final String requiredProductCode);

	/**
	 * Creates a {@link Set} of {@link TmaProductOfferingModel} required products, used for obtaining price override.
	 *
	 * @param requiredProductCodes
	 * 		codes of the required products
	 * @return a {@link Set} of {@link TmaProductOfferingModel}
	 */
	protected abstract Set<TmaProductOfferingModel> getRequiredProducts(final Set<String> requiredProductCodes);

	protected ProductData getProduct(final String productCode)
	{
		return getProductOfferFacade().getPoForCode(productCode, PRODUCT_OPTIONS);
	}

	protected ProductData getBpo(final String bpoCode)
	{
		return getProductOfferFacade().getPoForCode(bpoCode, BPO_OPTIONS);
	}

	protected void setProductPriceFromBpo(final String productCode, final String bpoCode, final Set<String> requiredProductCode,
			final ProductData productData)
	{
		final TmaProductOfferingModel parentBpo = getPoService().getPoForCode(bpoCode);
		final TmaProductOfferingModel mainPo = getPoService().getPoForCode(productCode);
		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(parentBpo)
				.withProcessTypes(new HashSet<>(Arrays.asList(getProcessTypeFromSession())))
				.withtAffectedProduct(mainPo)
				.withRequiredProducts(getRequiredProducts(requiredProductCode))
				.build();

		final PriceData bestApplicablePrice = getTmaPriceFacade().getBestApplicablePrice(priceContext);
		productData.setPrices(Arrays.asList(bestApplicablePrice));
		productData.setMainSpoPriceInBpo(bestApplicablePrice);
	}

	/**
	 * Sets the mainSpoPriceInBpo for the productData provided to the lowestPrice applicable.
	 *
	 * @param offerContextData
	 * 		contains the parameters for the offer
	 * @param productData
	 * 		the productData for which the price will be set
	 */
	protected void setProductPriceFromBpo(final TmaOfferContextData offerContextData, final ProductData productData)
	{
		final TmaProductOfferingModel parentBpo = getPoService().getPoForCode(offerContextData.getBpoCode());
		final TmaProductOfferingModel mainPo = getPoService().getPoForCode(offerContextData.getAffectedProductCode());

		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(parentBpo)
				.withProcessTypes(new HashSet<>(Arrays.asList(offerContextData.getProcessType())))
				.withtAffectedProduct(mainPo)
				.withRequiredProducts(getRequiredProducts(offerContextData.getRequiredProductCodes()))
				.withUser(getUserService().getUserForUID(offerContextData.getUser()))
				.build();

		final PriceData bestApplicablePrice = getTmaPriceFacade().getBestApplicablePrice(priceContext);

		if (bestApplicablePrice == null)
		{
			productData.setPrices(Collections.emptyList());
			return;
		}

		productData.setPrices(Arrays.asList(bestApplicablePrice));
		productData.setMainSpoPriceInBpo(bestApplicablePrice);
	}

	/**
	 * Checks if the provided subscription term is in the prices of the productOffering, parentBpo and processType
	 * provided.
	 *
	 * @param product
	 * 		the product
	 * @param parentBpo
	 * 		the parent of the product
	 * @param processType
	 * 		the process type
	 * @param subscriptionTermId
	 * 		the identifier of the subscription term
	 * @return True if provided subscription term is in the prices of the product and its parent, otherwise false.
	 */
	protected boolean hasSubscriptionTerm(final TmaProductOfferingModel product, final TmaBundledProductOfferingModel parentBpo,
			final TmaProcessType processType, final String subscriptionTermId)
	{
		final Set<SubscriptionTermModel> subscriptionTermModelSet = getSubscriptionTermService()
				.getApplicableSubscriptionTerms(product, parentBpo, processType);

		return StringUtils.isEmpty(subscriptionTermId) || (CollectionUtils.isNotEmpty(subscriptionTermModelSet)
				&& subscriptionTermModelSet.stream()
				.anyMatch((final SubscriptionTermModel subscriptionTerm) -> subscriptionTerm.getId().equals(subscriptionTermId)));
	}

	protected TmaProcessType getProcessTypeFromSession()
	{
		final String processType = getSessionService().getAttribute(SELECTED_PROCESS_TYPE);
		return processType == null ? null : TmaProcessType.valueOf(processType);
	}

	protected TmaProductOfferFacade getProductOfferFacade()
	{
		return productOfferFacade;
	}

	@Required
	public void setProductOfferFacade(final TmaProductOfferFacade productOfferFacade)
	{
		this.productOfferFacade = productOfferFacade;
	}

	protected TmaPoService getPoService()
	{
		return poService;
	}

	@Required
	public void setPoService(final TmaPoService poService)
	{
		this.poService = poService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected TmaPriceFacade getTmaPriceFacade()
	{
		return tmaPriceFacade;
	}

	@Required
	public void setTmaPriceFacade(final TmaPriceFacade tmaPriceFacade)
	{
		this.tmaPriceFacade = tmaPriceFacade;
	}

	protected Converter<ProductData, TmaOfferData> getTmaOfferConverter()
	{
		return tmaOfferConverter;
	}

	@Required
	public void setTmaOfferConverter(final Converter<ProductData, TmaOfferData> tmaOfferConverter)
	{
		this.tmaOfferConverter = tmaOfferConverter;
	}

	protected TmaSubscriptionTermService getSubscriptionTermService()
	{
		return subscriptionTermService;
	}

	@Required
	public void setSubscriptionTermService(final TmaSubscriptionTermService subscriptionTermService)
	{
		this.subscriptionTermService = subscriptionTermService;
	}

	public UserService getUserService()
	{
		return userService;
	}
}
