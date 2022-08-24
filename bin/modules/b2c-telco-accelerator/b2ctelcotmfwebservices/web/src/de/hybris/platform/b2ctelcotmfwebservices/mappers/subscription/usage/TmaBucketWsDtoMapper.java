/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription.usage;

import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage.TmaBucketWsDto;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {TmaAverageServiceUsageData} and {@link TmaBucketWsDto}
 *
 * @since 1810
 */
public class TmaBucketWsDtoMapper extends AbstractCustomMapper<TmaAverageServiceUsageData, TmaBucketWsDto>
{

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Override
	public void mapAtoB(final TmaAverageServiceUsageData a, final TmaBucketWsDto b, final MappingContext context)
	{
		// other fields are mapped automatically
		mapDefaultAtoB(a, b, context);
	}

	protected void mapDefaultAtoB(final TmaAverageServiceUsageData a, final TmaBucketWsDto b,
			final MappingContext context)
	{
		context.beginMappingField("id", getAType(), a, "isShared", getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				final Boolean isSharedBucket = Boolean
						.valueOf(
								getMessageSource().getMessage(B2ctelcotmfwebservicesConstants.TMA_BUCKET_LIST_WSDTO_ISSHARED_PROPERTY, null,
										Locale.ENGLISH));
				b.setIsShared(isSharedBucket);
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