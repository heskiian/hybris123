/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Service class used for generating IDs for product offering prices.
 *
 * @since 1903
 * @deprecated since 2007. Id is present on priceRow, no longer necessary to generate it.
 */
@Deprecated(since= "2007")
public class PoPriceIdGenerator
{
	private static final String HYPEN = "-";
	private static final String UNDERLINE = "_";

	/**
	 * Generates the ID of the price by considering the whole list of attributes defined for price.
	 *
	 * @param sppData
	 * 		the subscription price plan data object
	 * @return the ID of the PO price generated
	 */
	@SuppressWarnings("WeakerAccess")
	public String generateIdForPrice(SubscriptionPricePlanData sppData)
	{
		final List<String> idComponents = new ArrayList<>();
		if (!ObjectUtils.isEmpty(sppData.getProduct()))
		{
			idComponents.add(sppData.getProduct().getCode());
		}

		if (StringUtils.isNotEmpty(sppData.getCurrencyIso()))
		{
			idComponents.add(sppData.getCurrencyIso());
		}

		updateIdComponentsWithProcessTypes(idComponents, sppData);
		updateIdComponentsWithRequiredProducts(idComponents, sppData);
		updateIdComponentsWithRequiredProductClasses(idComponents, sppData);
		updateIdComponentsWithSubscriptionTerms(idComponents, sppData);
		updateIdComponentsWithDistributionChannels(idComponents, sppData);

		if (!ObjectUtils.isEmpty(sppData.getUser()))
		{
			idComponents.add(sppData.getUser().getUid());
		}

		return String.join(HYPEN, idComponents).toLowerCase();
	}

	private void updateIdComponentsWithProcessTypes(final List<String> idComponents, final SubscriptionPricePlanData source)
	{
		if (CollectionUtils.isEmpty(source.getProcessTypes()))
		{
			return;
		}

		final List<String> processTypeIds = new ArrayList<>();
		source.getProcessTypes().forEach(processType -> processTypeIds.add(processType.getCode()));
		appendToPriceId(processTypeIds, idComponents);
	}

	private void appendToPriceId(final List<String> ids, final List<String> idComponents)
	{
		if (!ObjectUtils.isEmpty(ids))
		{
			idComponents.add(String.join(UNDERLINE, ids));
		}
	}

	private void updateIdComponentsWithRequiredProducts(final List<String> idComponents, final SubscriptionPricePlanData a)
	{
		if (CollectionUtils.isEmpty(a.getRequiredProductOfferings()))
		{
			return;
		}

		final List<String> requiredProductIds = new ArrayList<>();
		a.getRequiredProductOfferings().forEach(requiredProduct -> requiredProductIds.add(requiredProduct.getCode()));
		appendToPriceId(requiredProductIds, idComponents);
	}

	private void updateIdComponentsWithRequiredProductClasses(final List<String> idComponents, final SubscriptionPricePlanData a)
	{
		if (CollectionUtils.isEmpty(a.getRequiredProductClasses()))
		{
			return;
		}

		final List<String> requiredProductClassIds = new ArrayList<>();
		a.getRequiredProductClasses().forEach(requiredProductClass -> requiredProductClassIds.add(requiredProductClass.getId()));
		appendToPriceId(requiredProductClassIds, idComponents);
	}

	private void updateIdComponentsWithSubscriptionTerms(final List<String> idComponents, final SubscriptionPricePlanData a)
	{
		if (CollectionUtils.isEmpty(a.getSubscriptionTerms()))
		{
			return;
		}

		final List<String> subscriptionTermIds = new ArrayList<>();
		a.getSubscriptionTerms().forEach(subscriptionTerm -> subscriptionTermIds.add(subscriptionTerm.getId()));
		appendToPriceId(subscriptionTermIds, idComponents);
	}

	private void updateIdComponentsWithDistributionChannels(final List<String> idComponents, final SubscriptionPricePlanData a)
	{
		if (CollectionUtils.isEmpty(a.getDistributionChannels()))
		{
			return;
		}

		final List<String> distributionChannelIds = new ArrayList<>();
		a.getDistributionChannels().forEach(distributionChannel -> distributionChannelIds.add(distributionChannel.getCode()));
		appendToPriceId(distributionChannelIds, idComponents);
	}
}
