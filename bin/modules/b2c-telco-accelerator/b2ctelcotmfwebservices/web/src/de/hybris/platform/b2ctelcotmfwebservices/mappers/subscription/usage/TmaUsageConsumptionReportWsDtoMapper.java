/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription.usage;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage.TmaUsageConsumptionReportWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.mappers.TmaTmfAbstractRelatedPartyDataMapper;
import de.hybris.platform.util.Config;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {TmaSubscribedProductData} and {@link TmaUsageConsumptionReportWsDto}
 *
 * @since 1810
 */

public class TmaUsageConsumptionReportWsDtoMapper
		extends TmaTmfAbstractRelatedPartyDataMapper<TmaSubscribedProductData, TmaUsageConsumptionReportWsDto>
{

	private static final String CUSTOMER_TYPE_OWNER = "OWNER";
	private static final String USAGE_REPORT_HREF = Config.getString(B2ctelcotmfwebservicesConstants.API_BASE_URL,
			StringUtils.EMPTY) + Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCT_API_VERSION, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_USAGE_CONSUMPTION_REPORT_WSDTO_HREF, StringUtils.EMPTY);
	private static final String SUBSCRIPTION_BASE_ID = "subscriptionBaseId";

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Override
	public void mapAtoB(final TmaSubscribedProductData a,
			final TmaUsageConsumptionReportWsDto b, final MappingContext context)
	{
		// other fields are mapped automatically
		mapRelatedPartyNameAtoB(a, b, context);
		mapDefaultAtoB(a, b, context);
	}


	protected void mapRelatedPartyNameAtoB(final TmaSubscribedProductData a, final TmaUsageConsumptionReportWsDto b,
			final MappingContext context)
	{
		context.beginMappingField(SUBSCRIPTION_BASE_ID, getAType(), a, "relatedParty", getBType(), b);
		try
		{
			final List<TmaSubscriptionAccessData> subscriptionAccessDatas = getSubscriptionAccessFacade()
					.getSubscriptionAccessesBySubscriberIdentity(a.getSubscriptionBaseId(), a.getBillingsystemId());
			if (CollectionUtils.isNotEmpty(subscriptionAccessDatas))
			{
				getUsageReportDescription(subscriptionAccessDatas, b, context);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void getUsageReportDescription(final List<TmaSubscriptionAccessData> subscriptionAccessDatas,
			final TmaUsageConsumptionReportWsDto b, final MappingContext context)
	{
		final String description = getMessageSource().getMessage(
				B2ctelcotmfwebservicesConstants.TMA_USAGE_CONSUMPTION_REPORT_WSDTO_DESCRIPTION, null, Locale.ENGLISH);
		for (final TmaSubscriptionAccessData subscriptionAccessData : subscriptionAccessDatas)
		{
			if (CUSTOMER_TYPE_OWNER.equalsIgnoreCase(subscriptionAccessData.getAccessType().name()))
			{
				b.setDescription(description + " " + getCustomerName(subscriptionAccessData.getPrincipalUid(), context));
				break;
			}
		}
		if (StringUtils.isEmpty(b.getDescription()))
		{
			b.setDescription(description + " " + getCustomerName(subscriptionAccessDatas.get(0).getPrincipalUid(), context));
		}
	}

	protected String getCustomerName(final String customerId, final MappingContext context)
	{
		final TmaRelatedPartyWsDto relatedPartyWsDto = getRelatedPartyRefDetailsForCustomerId(customerId, context);
		return relatedPartyWsDto.getName();
	}

	protected void mapDefaultAtoB(final TmaSubscribedProductData a,
			final TmaUsageConsumptionReportWsDto b,
			final MappingContext context)
	{
		context.beginMappingField("id", getAType(), a, "name", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getId()))
			{
				final String usageReportName = getMessageSource()
						.getMessage(B2ctelcotmfwebservicesConstants.TMA_USAGE_CONSUMPTION_REPORT_WSDTO_NAME, null, Locale.ENGLISH);
				b.setSchemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
				b.setId(getMessageSource().getMessage(B2ctelcotmfwebservicesConstants.TMA_USAGE_CONSUMPTION_REPORT_WSDTO_ID, null,
						Locale.ENGLISH));
				b.setName(usageReportName + " "
						+ getMessageSource().getMessage(B2ctelcotmfwebservicesConstants.TMA_USAGE_CONSUMPTION_REPORT_WSDTO_ID, null,
								Locale.ENGLISH));
				b.setEffectiveDate(new Date(System.currentTimeMillis()));
				b.setBaseType(Config.getString(B2ctelcotmfwebservicesConstants.TMA_USAGE_CONSUMPTION_REPORT_WSDTO_BASE_TYPE,
						StringUtils.EMPTY));
				b.setType(b.getClass().getSimpleName());
				b.setHref(USAGE_REPORT_HREF);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	//mapDefaultBtoA() is not required

	protected MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
}
