/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.strategy.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferContextData;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Strategy implementation for TARIFF_CHANGE flow.
 *
 * @since 6.7
 */
public class TmaTariffChangeStrategy extends TmaAbstractProcessFlowStrategy
{
	public TmaTariffChangeStrategy(UserService userService)
	{
		super(userService);
	}

	@Override
	public List<TmaOfferData> getOffersForDeviceOnly(final String productCode)
	{
		return getOffers(productCode, null);
	}

	@Override
	public List<TmaOfferData> getOffersForDeviceInBpo(String productCode, String bpoCode,
			Set<String> requiredProductCodes)
	{
		return getOffers(productCode, bpoCode);
	}

	@Override
	public Set<TmaProductOfferingModel> getRequiredProducts(final String requiredProductCode)
	{
		return new HashSet<>();
	}

	@Override
	protected Set<TmaProductOfferingModel> getRequiredProducts(Set<String> requiredProductCodes)
	{
		return new HashSet<>();
	}

	@Override
	public List<TmaOfferData> getOffers(final TmaOfferContextData offerContextData)
	{
		final List<TmaOfferData> offers = new ArrayList<>();

		final TmaProductOfferingModel currentProduct = getPoService().getPoForCode(offerContextData.getAffectedProductCode());
		final Collection<TmaBundledProductOfferingModel> allParents = getPoService().getAllParents(currentProduct);

		final String bpoToExclude = offerContextData.getBpoCode();
		allParents.forEach((TmaBundledProductOfferingModel bpo) -> {
			if (!isEligibleForTariffChange(bpo, currentProduct, bpoToExclude) || !hasSubscriptionTerm(currentProduct, bpo,
					offerContextData.getProcessType(), offerContextData.getSubscriptionTermId()))
			{
				return;
			}

			final ProductData productData = getProduct(offerContextData.getAffectedProductCode());
			offerContextData.setBpoCode(bpo.getCode());
			setProductPriceFromBpo(offerContextData, productData);

			if (CollectionUtils.isEmpty(productData.getPrices()))
			{
				return;
			}

			final TmaOfferData offerData = getTmaOfferConverter().convert(productData);
			offerData.setParentBpo(getBpo(bpo.getCode()));
			offerData.setProcessType(offerContextData.getProcessType());
			offerData.setPriority(offers.size() + 1);
			offers.add(offerData);

		});

		return offers;
	}

	@Override
	protected void setProductPriceFromBpo(final TmaOfferContextData offerContextData, final ProductData productData)
	{
		final TmaProductOfferingModel parentBpo = getPoService().getPoForCode(offerContextData.getBpoCode());
		final TmaProductOfferingModel mainPo = getPoService().getPoForCode(offerContextData.getAffectedProductCode());

		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(parentBpo)
				.withProcessTypes(new HashSet<>(Arrays.asList(offerContextData.getProcessType())))
				.withtAffectedProduct(mainPo)
				.withRequiredProducts(null)
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

	@Override
	protected void setProductPriceFromBpo(final String productCode, final String bpoCode, final Set<String> requiredProductCode,
			final ProductData productData)
	{
		final TmaProductOfferingModel parentBpo = getPoService().getPoForCode(bpoCode);
		final TmaProductOfferingModel mainPo = getPoService().getPoForCode(productCode);
		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(parentBpo)
				.withProcessTypes(new HashSet<>(Arrays.asList(getProcessTypeFromSession())))
				.withtAffectedProduct(mainPo)
				.withRequiredProducts(null)
				.build();

		final PriceData bestApplicablePrice = getTmaPriceFacade().getBestApplicablePrice(priceContext);
		productData.setPrices(Arrays.asList(bestApplicablePrice));
		productData.setMainSpoPriceInBpo(bestApplicablePrice);
	}

	private List<TmaOfferData> getOffers(final String productCode, final String bpoCodeToExclude)
	{
		final List<TmaOfferData> offers = new ArrayList<>();

		final TmaProductOfferingModel currentProduct = getPoService().getPoForCode(productCode);
		final Collection<TmaBundledProductOfferingModel> allParents = getPoService().getAllParents(currentProduct);

		allParents.forEach(bpo ->
		{
			if (isEligibleForTariffChange(bpo, currentProduct, bpoCodeToExclude))
			{
				final ProductData productData = getProduct(productCode);
				setProductPriceFromBpo(productCode, bpo.getCode(), new HashSet<>(), productData);
				final TmaOfferData offerData = getTmaOfferConverter().convert(productData);
				offerData.setParentBpo(getBpo(bpo.getCode()));
				offers.add(offerData);
			}
		});

		return offers;
	}

	private boolean isEligibleForTariffChange(final TmaBundledProductOfferingModel bpo,
			final TmaProductOfferingModel currentProduct, final String bpoCodeToExclude)
	{
		boolean isRetention = bpoCodeToExclude != null && bpoCodeToExclude.equals(bpo.getCode());
		return !isRetention && getPoService().isPoEligibleForProcessType(TmaProcessType.TARIFF_CHANGE, currentProduct, bpo);
	}
}
