/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription.base;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.data.TmaSubscriptionBaseDetailRefWrapperData;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.base.TmaSubscriptionBasesWsDto;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between{@link TmaSubscriptionBaseDetailRefWrapperData} and
 * {@link TmaSubscriptionBasesWsDto}
 *
 * @since 1810
 */
public class TmaSubscriptionBasesWsDtoMapper
		extends AbstractCustomMapper<TmaSubscriptionBaseDetailRefWrapperData, TmaSubscriptionBasesWsDto>
{
	private static final String SUBSCRIPTION_WS_DTO_HREF = Config.getString(B2ctelcotmfwebservicesConstants.API_BASE_URL,
			StringUtils.EMPTY) + Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCT_API_VERSION, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_SUBSCRIPTION_API_HREF, StringUtils.EMPTY);

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Override
	public void mapAtoB(final TmaSubscriptionBaseDetailRefWrapperData a, final TmaSubscriptionBasesWsDto b,
			final MappingContext context)
	{
		mapDefaultAtoB(a, b, context);
	}

	protected void mapDefaultAtoB(final TmaSubscriptionBaseDetailRefWrapperData a, final TmaSubscriptionBasesWsDto b,
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
				b.setDescription(getMessageSource().getMessage(B2ctelcotmfwebservicesConstants.TMA_SUBSCRIPTION_API_DESCRIPTION, null,
						Locale.ENGLISH));
				b.setEffectiveDate(new Date(System.currentTimeMillis()));
				b.setHref(SUBSCRIPTION_WS_DTO_HREF + b.getId());
				b.setBaseType(Config.getString(B2ctelcotmfwebservicesConstants.TMA_SUBSCRIPTION_API_BASETYPE, StringUtils.EMPTY));
				b.setSchemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
				b.setType(b.getClass().getSimpleName());
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
