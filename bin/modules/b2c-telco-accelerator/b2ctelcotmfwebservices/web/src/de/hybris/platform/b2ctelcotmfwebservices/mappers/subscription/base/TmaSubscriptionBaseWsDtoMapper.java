/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription.base;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.base.TmaSubscriptionBaseDetailRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.base.TmaSubscriptionBaseWsDto;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between{@link TmaSubscriptionBaseData} and {@link TmaSubscriptionBaseWsDto}
 *
 * @since 1810
 */
public class TmaSubscriptionBaseWsDtoMapper extends AbstractCustomMapper<TmaSubscriptionBaseData, TmaSubscriptionBaseWsDto>
{
	private static final String SUBSCRIBER_IDENTITY = "subscriberIdentity";
	private static final String SUBSCRIPTION_BASE = "subscriptionBase";
	private static final String SUBSCRIPTION_WS_DTO_HREF = Config.getString(B2ctelcotmfwebservicesConstants.API_BASE_URL,
			StringUtils.EMPTY) + Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCT_API_VERSION, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_SUBSCRIPTION_API_HREF, StringUtils.EMPTY);

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Override
	public void mapAtoB(final TmaSubscriptionBaseData a, final TmaSubscriptionBaseWsDto b,
			final MappingContext context)
	{
		mapDefaultAtoB(a, b, context);
		mapSubscriptionBaseDetailRefAtoB(a, b, context);
	}

	protected void mapDefaultAtoB(final TmaSubscriptionBaseData a, final TmaSubscriptionBaseWsDto b,
			final MappingContext context)
	{
		context.beginMappingField("id", getAType(), a, "id", getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				b.setId(getMessageSource().getMessage(B2ctelcotmfwebservicesConstants.TMA_SUBSCRIPTION_API_ID, null, Locale.ENGLISH));
				b.setName(
						getMessageSource().getMessage(B2ctelcotmfwebservicesConstants.TMA_SUBSCRIPTION_API_NAME, null, Locale.ENGLISH));
				b.setDescription(
						getMessageSource().getMessage(B2ctelcotmfwebservicesConstants.TMA_SUBSCRIPTION_API_DESCRIPTION, null,
								Locale.ENGLISH));
				b.setEffectiveDate(new Date(System.currentTimeMillis()));
				b.setHref(SUBSCRIPTION_WS_DTO_HREF + b.getId());
				b.setBaseType(Config.getString(B2ctelcotmfwebservicesConstants.TMA_SUBSCRIPTION_API_BASETYPE, StringUtils.EMPTY));
				b.setType(b.getClass().getSimpleName());
				b.setSchemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapSubscriptionBaseDetailRefAtoB(final TmaSubscriptionBaseData a, final TmaSubscriptionBaseWsDto b,
			final MappingContext context)
	{
		context.beginMappingField(SUBSCRIBER_IDENTITY, getAType(), a, SUBSCRIPTION_BASE, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getSubscriberIdentity()))
			{
				final TmaSubscriptionBaseDetailRefWsDto subscriptionBaseDetailRefWsDto = mapperFacade.map(a,
						TmaSubscriptionBaseDetailRefWsDto.class, context);
				b.setSubscriptionBase(subscriptionBaseDetailRefWsDto);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaSubscriptionBaseWsDto b, final TmaSubscriptionBaseData a, final MappingContext context)
	{
		context.beginMappingField(SUBSCRIPTION_BASE, getBType(), b, SUBSCRIBER_IDENTITY, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && StringUtils.isNotEmpty(b.getSubscriptionBase().getId()))
			{
				a.setSubscriberIdentity(b.getSubscriptionBase().getId());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
}
