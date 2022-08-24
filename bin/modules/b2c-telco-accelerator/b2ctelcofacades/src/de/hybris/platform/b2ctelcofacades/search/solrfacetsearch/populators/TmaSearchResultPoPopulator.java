/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.search.solrfacetsearch.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferingGroupData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceDataFactory;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.subscriptionfacades.data.BillingPlanData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceRenewalData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;


/**
 * Search Result Product Offering Populator for converting the Search Result into Product Data.
 *
 * @since 6.7
 * @deprecated since 2007, use {@link TmaSearchResultProductOfferingPopulator} and other dedicated populators instead.
 */
@Deprecated(since = "2007")
public class TmaSearchResultPoPopulator extends SearchResultProductPopulator
{

	private static final String VOLUME_PRICES_INDEXED_PROPERTY = "volumePrices";
	private static final String PRICE_VALUE_INDEXED_PROPERTY = "priceValue";
	private static final String BILLING_TIME_INDEXED_PROPERTY = "billingTimes";
	private static final String TERM_LIMIT_INDEXED_PROPERTY = "termLimits";
	private static final String TERM_RENEWAL_INDEXED_PROPERTY = "termRenewals";
	private static final String PARENT_BUNDLED_PRODUCT_OFFERING_INDEXED_PROPERTY = "parentBundledPo";
	private static final String PRODUCT_SPECIFICATION_INDEXED_PROPRTY = "productSpecification";
	private static final String APPROVAL_STATUS_INDEXED_PROPRTY = "approvalStatus";
	private static final String IS_BUNDLED_INDEXED_PROPERTY = "isBundled";
	private static final String PRODUCT_OFFERING_GROUP_INDEXED_PROPERTY = "productOfferingGroups";

	private TmaPriceDataFactory tmaPriceDataFactory;

	@Override
	protected void populatePrices(final SearchResultValueData source, final ProductData target)
	{
		setVolumePrices(source, target);
		setBasePrice(source, target);
		setParentBundledProductOffering(source, target);
		setProductSpecification(source, target);
		setItemType(source, target);
		setLifecycleStatus(source, target);
		setProductOfferingGroup(source, target);
	}

	protected void setBasePrice(final SearchResultValueData source, final ProductData target)
	{
		final Double priceValue = getValue(source, PRICE_VALUE_INDEXED_PROPERTY);
		if (priceValue == null)
		{
			return;
		}
		final SubscriptionPricePlanData price = getTmaPriceDataFactory()
				.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), getCommonI18NService().getCurrentCurrency());
		price.setSubscriptionTerms(generateSubscriptionTermsFromSource(source));

		target.setPrice(price);
	}

	protected void setVolumePrices(final SearchResultValueData source, final ProductData target)
	{
		final Boolean volumePrices = getValue(source, VOLUME_PRICES_INDEXED_PROPERTY);
		target.setVolumePricesFlag(volumePrices == null ? Boolean.FALSE : volumePrices);
	}

	protected void setParentBundledProductOffering(SearchResultValueData source, ProductData target)
	{
		List<ProductData> parentBundledProductOfferings = new ArrayList<>();
		List<String> parentCodes = getValue(source,
				PARENT_BUNDLED_PRODUCT_OFFERING_INDEXED_PROPERTY);
		if (parentCodes != null)
		{
			for (String code : parentCodes)
			{
				ProductData productData = new ProductData();
				productData.setCode(code);
				parentBundledProductOfferings.add(productData);
			}
			target.setParents(parentBundledProductOfferings);
		}
	}

	protected void setProductSpecification(SearchResultValueData source, ProductData target)
	{
		String productSpecificationCode = getValue(source, PRODUCT_SPECIFICATION_INDEXED_PROPRTY);
		if (StringUtils.isEmpty(productSpecificationCode))
		{
			return;
		}

		TmaProductSpecificationData productSpecification = new TmaProductSpecificationData();
		productSpecification.setId(productSpecificationCode);
		target.setProductSpecification(productSpecification);
	}

	protected void setLifecycleStatus(SearchResultValueData source, ProductData target)
	{
		String approvalStatus = getValue(source, APPROVAL_STATUS_INDEXED_PROPRTY);
		target.setApprovalStatus(approvalStatus);
	}

	protected void setItemType(SearchResultValueData source, ProductData target)
	{
		boolean isBundled = getValue(source, IS_BUNDLED_INDEXED_PROPERTY);
		String itemType = isBundled ? TmaBundledProductOfferingModel.ITEMTYPE : TmaSimpleProductOfferingModel.ITEMTYPE;
		target.setItemType(itemType);
	}

	private void setProductOfferingGroup(SearchResultValueData source, ProductData target)
	{
		List<TmaOfferingGroupData> productOfferingGroups = new ArrayList<>();
		List<String> productOfferingGroupCodes = getValue(source,
				PRODUCT_OFFERING_GROUP_INDEXED_PROPERTY);
		if (productOfferingGroupCodes != null)
		{
			for (String code : productOfferingGroupCodes)
			{
				TmaOfferingGroupData offeringGroupData = new TmaOfferingGroupData();
				offeringGroupData.setId(code);
				productOfferingGroups.add(offeringGroupData);
			}
			target.setOfferingGroups(productOfferingGroups);
		}
	}

	/**
	 * Generates a list of {@link SubscriptionTermData} by considering indexed fields provided by the {@param source}.
	 *
	 * @param source
	 *           source to retrieve the data from
	 * @return newly created {@link SubscriptionTermData}
	 */
	protected List<SubscriptionTermData> generateSubscriptionTermsFromSource(final SearchResultValueData source)
	{
		final List<SubscriptionTermData> subscriptionTerms = new ArrayList<>();
		final List<String> billingTimes = getCollectionValue(source, BILLING_TIME_INDEXED_PROPERTY);
		final List<String> termsOfServiceRenewals = getCollectionValue(source, TERM_RENEWAL_INDEXED_PROPERTY);
		final List<String> termFrequencies = getCollectionValue(source, TERM_LIMIT_INDEXED_PROPERTY);

		if (ObjectUtils.isEmpty(billingTimes) && ObjectUtils.isEmpty(termsOfServiceRenewals)
				&& ObjectUtils.isEmpty(termFrequencies))
		{
			return subscriptionTerms;
		}

		final int resultSize = Math.min(Math.min(billingTimes.size(), termFrequencies.size()), termsOfServiceRenewals.size());

		for (int index = 0; index < resultSize; index++)
		{
			final SubscriptionTermData subscriptionTerm = new SubscriptionTermData();
			final BillingTimeData billingTime = new BillingTimeData();
			billingTime.setName(billingTimes.get(index));
			final BillingPlanData billingPlan = new BillingPlanData();
			billingPlan.setBillingTime(billingTime);
			subscriptionTerm.setBillingPlan(billingPlan);

			final TermOfServiceRenewalData termOfServiceRenewalData = new TermOfServiceRenewalData();
			termOfServiceRenewalData.setName(termsOfServiceRenewals.get(index));
			subscriptionTerm.setTermOfServiceRenewal(termOfServiceRenewalData);

			final String termOfServiceFrequencyNumberAsString = termFrequencies.get(index);
			if (StringUtils.isNotBlank(termOfServiceFrequencyNumberAsString))
			{
				final String[] termOfServiceFrequencyNumber = termOfServiceFrequencyNumberAsString.split("/");
				subscriptionTerm.setTermOfServiceNumber(
						StringUtils.isNotEmpty(termOfServiceFrequencyNumber[0]) ? Integer.valueOf(termOfServiceFrequencyNumber[0]) : 0);
				final TermOfServiceFrequencyData serviceFrequencyData = new TermOfServiceFrequencyData();
				serviceFrequencyData.setName(termOfServiceFrequencyNumber[1]);
				subscriptionTerm.setTermOfServiceFrequency(serviceFrequencyData);
			}
			subscriptionTerms.add(subscriptionTerm);
		}
		return subscriptionTerms;
	}

	private List<String> getCollectionValue(final SearchResultValueData source, final String propertyName)
	{
		return getValue(source, propertyName) == null ? new ArrayList<>() : getValue(source, propertyName);
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
}
