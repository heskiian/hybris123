/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.strategy.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferContextData;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
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
import org.apache.commons.lang.StringUtils;


/**
 * Strategy implementation for RETENTION flow.
 *
 * @since 6.7
 */
public class TmaRetentionStrategy extends TmaAbstractProcessFlowStrategy
{
	public TmaRetentionStrategy(UserService userService)
	{
		super(userService);
	}

	@Override
	public List<TmaOfferData> getOffersForDeviceOnly(final String productCode)
	{
		return Collections.emptyList();
	}

	@Override
	public List<TmaOfferData> getOffersForDeviceInBpo(final String productCode, final String bpoCode,
			final Set<String> requiredProductCodes)
	{
		final ProductData productData = getProduct(productCode);
		final ProductData bpo = getBpo(bpoCode);

		setProductPriceFromBpo(productCode, bpoCode, requiredProductCodes, productData);

		final TmaOfferData offerData = getTmaOfferConverter().convert(productData);
		offerData.setParentBpo(bpo);

		return Arrays.asList(offerData);
	}

	@Override
	public List<TmaOfferData> getOffers(final TmaOfferContextData offerContextData)
	{
		if (StringUtils.isEmpty(offerContextData.getBpoCode()))
		{
			return Collections.emptyList();
		}

		final ProductData productData = getProduct(offerContextData.getAffectedProductCode());
		final ProductData bpo = getBpo(offerContextData.getBpoCode());

		setProductPriceFromBpo(offerContextData, productData);

		if (CollectionUtils.isEmpty(productData.getPrices()))
		{
			return Collections.emptyList();
		}

		final TmaProductOfferingModel currentProduct = getPoService().getPoForCode(offerContextData.getAffectedProductCode());
		final Collection<TmaBundledProductOfferingModel> allParents = getPoService().getAllParents(currentProduct);

		boolean hasSubscriptionTerm = allParents.stream().anyMatch((TmaBundledProductOfferingModel parent) ->
				hasSubscriptionTerm(currentProduct, parent, offerContextData.getProcessType(),
						offerContextData.getSubscriptionTermId()));

		final List<TmaOfferData> offers = new ArrayList<>();
		int priority = 1;
		for (TmaBundledProductOfferingModel parent : allParents)
		{
			if (hasSubscriptionTerm && isEligibleForRetention(parent, currentProduct, offerContextData.getBpoCode()))
			{
				final TmaOfferData offerData = getTmaOfferConverter().convert(productData);
				offerData.setParentBpo(getBpo(bpo.getCode()));
				offerData.setProcessType(offerContextData.getProcessType());
				offerData.setPriority(priority);
				offers.add(offerData);
				priority++;
			}

		}

		return offers;
	}

	@Override
	public Set<TmaProductOfferingModel> getRequiredProducts(final String requiredProductCode)
	{
		return new HashSet(Arrays.asList(getPoService().getPoForCode(requiredProductCode)));
	}

	@Override
	protected Set<TmaProductOfferingModel> getRequiredProducts(final Set<String> requiredProductCodes)
	{
		final Set<TmaProductOfferingModel> productOfferings = new HashSet<>();
		requiredProductCodes.stream()
				.forEach(requiredProductCode -> productOfferings.add(getPoService().getPoForCode(requiredProductCode)));
		return productOfferings;
	}

	private boolean isEligibleForRetention(final TmaBundledProductOfferingModel bpo,
			final TmaProductOfferingModel currentProduct,
			final String bpoCodeToExclude)
	{
		boolean isRetention = bpoCodeToExclude != null && bpoCodeToExclude.equals(bpo.getCode());
		return isRetention && getPoService().isPoEligibleForProcessType(TmaProcessType.RETENTION, currentProduct, bpo);
	}
}
