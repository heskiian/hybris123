/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import de.hybris.platform.upilintegrationservices.data.C_UtilitiesProductType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdBillingAttributeType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgOneTimeType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgRecurringType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgUsageType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdSalesAttributeType;
import de.hybris.platform.upilintegrationservices.enums.UpilAttributeType;
import de.hybris.platform.upilintegrationservices.model.UpilAdditionalAttributesModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;


/**
 * Populates {@link SubscriptionPricePlanModel} to {@link C_UtilsProductType}
 * 
 * @since 1911
 */
public class UpilUtilitiesProductPopulator implements Populator<SubscriptionPricePlanModel, C_UtilitiesProductType>
{

	private Converter<UsageChargeEntryModel, I_UtilsProdChgUsageType> upilUsageChargeEntryConverter;
	private Converter<RecurringChargeEntryModel, I_UtilsProdChgRecurringType> upilRecurringChargesConverter;
	private Converter<OneTimeChargeEntryModel, I_UtilsProdChgOneTimeType> upilOneTimeChargesConverter;
	private Converter<UpilAdditionalAttributesModel, I_UtilsProdBillingAttributeType> upilBillingAttributeConverter;
	private Converter<UpilAdditionalAttributesModel, I_UtilsProdSalesAttributeType> upilSalesAttributeConverter;

	@Override
	public void populate(final SubscriptionPricePlanModel source, final C_UtilitiesProductType target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setUtilitiesProduct(source.getCode());
		target.setValidityStartDate(source.getStartTime());
		target.setValidityStartDate(source.getEndTime());
		populateUpilAdditionalAttributes(source, target);
		if (!ObjectUtils.isEmpty(source.getProductSpecType()))
		{
			target.setUtilitiesReferenceProduct(source.getProductSpecType().getCode());
		}
		if (CollectionUtils.isNotEmpty(source.getUsageCharges()))
		{
			target.setTo_UsageCharge(getUpilUtilsUsageChargesData(source.getUsageCharges()));
		}
		if (CollectionUtils.isNotEmpty(source.getRecurringChargeEntries()))
		{
			target.setTo_RecurringCharge(getUpilRecurringChargesConverter().convertAll(source.getRecurringChargeEntries()));
		}
		if (CollectionUtils.isNotEmpty(source.getOneTimeChargeEntries()))
		{
			target.setTo_OneTimeCharge(getUpilOneTimeChargesConverter().convertAll(source.getOneTimeChargeEntries()));
		}
	}

	private void populateUpilAdditionalAttributes(final SubscriptionPricePlanModel source, final C_UtilitiesProductType target)
	{
		if (CollectionUtils.isNotEmpty(source.getUpilAdditionalAttributes()))
		{
			final List<UpilAdditionalAttributesModel> billingAttributeList = source.getUpilAdditionalAttributes().stream().filter(
					upilAdditionalAttribute -> !ObjectUtils.isEmpty(upilAdditionalAttribute.getAttributeType())
							&& UpilAttributeType.BILLING.getCode()
									.equalsIgnoreCase(upilAdditionalAttribute.getAttributeType().getCode()))
					.collect(Collectors.toList());

			final List<UpilAdditionalAttributesModel> salesAttributeList = source.getUpilAdditionalAttributes().stream().filter(
					upilAdditionalAttribute -> !ObjectUtils.isEmpty(upilAdditionalAttribute.getAttributeType())
							&& UpilAttributeType.SALES.getCode().equalsIgnoreCase(upilAdditionalAttribute.getAttributeType().getCode()))
					.collect(Collectors.toList());

			target.setTo_BillingAttribute(getUpilBillingAttributeConverter().convertAll(billingAttributeList));
			target.setTo_SalesAttribute(getUpilSalesAttributeConverter().convertAll(salesAttributeList));
		}
	}

	private List<I_UtilsProdChgUsageType> getUpilUtilsUsageChargesData(final Collection<UsageChargeModel> usageChargeModels)
	{
		final List<I_UtilsProdChgUsageType> utilsUsageChargesDataList = new ArrayList<>();
		usageChargeModels.forEach(usageChargeModel ->
		{
			if (CollectionUtils.isNotEmpty(usageChargeModel.getUsageChargeEntries()))
			{
				utilsUsageChargesDataList
						.addAll(getUpilUsageChargeEntryConverter().convertAll(usageChargeModel.getUsageChargeEntries()));
			}
		});
		return utilsUsageChargesDataList;
	}

	protected Converter<UsageChargeEntryModel, I_UtilsProdChgUsageType> getUpilUsageChargeEntryConverter()
	{
		return upilUsageChargeEntryConverter;
	}

	@Required
	public void setUpilUsageChargeEntryConverter(
			final Converter<UsageChargeEntryModel, I_UtilsProdChgUsageType> upilUsageChargeEntryConverter)
	{
		this.upilUsageChargeEntryConverter = upilUsageChargeEntryConverter;
	}

	protected Converter<RecurringChargeEntryModel, I_UtilsProdChgRecurringType> getUpilRecurringChargesConverter()
	{
		return upilRecurringChargesConverter;
	}

	@Required
	public void setUpilRecurringChargesConverter(
			final Converter<RecurringChargeEntryModel, I_UtilsProdChgRecurringType> upilRecurringChargesConverter)
	{
		this.upilRecurringChargesConverter = upilRecurringChargesConverter;
	}

	protected Converter<OneTimeChargeEntryModel, I_UtilsProdChgOneTimeType> getUpilOneTimeChargesConverter()
	{
		return upilOneTimeChargesConverter;
	}

	@Required
	public void setUpilOneTimeChargesConverter(
			final Converter<OneTimeChargeEntryModel, I_UtilsProdChgOneTimeType> upilOneTimeChargesConverter)
	{
		this.upilOneTimeChargesConverter = upilOneTimeChargesConverter;
	}

	protected Converter<UpilAdditionalAttributesModel, I_UtilsProdBillingAttributeType> getUpilBillingAttributeConverter()
	{
		return upilBillingAttributeConverter;
	}

	@Required
	public void setUpilBillingAttributeConverter(
			final Converter<UpilAdditionalAttributesModel, I_UtilsProdBillingAttributeType> upilBillingAttributeConverter)
	{
		this.upilBillingAttributeConverter = upilBillingAttributeConverter;
	}

	protected Converter<UpilAdditionalAttributesModel, I_UtilsProdSalesAttributeType> getUpilSalesAttributeConverter()
	{
		return upilSalesAttributeConverter;
	}

	@Required
	public void setUpilSalesAttributeConverter(
			final Converter<UpilAdditionalAttributesModel, I_UtilsProdSalesAttributeType> upilSalesAttributeConverter)
	{
		this.upilSalesAttributeConverter = upilSalesAttributeConverter;
	}
}
