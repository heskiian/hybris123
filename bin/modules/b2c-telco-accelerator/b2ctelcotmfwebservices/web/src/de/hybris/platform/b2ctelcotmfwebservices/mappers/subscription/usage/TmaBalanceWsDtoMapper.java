/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription.usage;

import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaTimePeriodWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage.TmaBalanceWsDto;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {TmaAverageServiceUsageData} and {@link TmaBalanceWsDto}
 *
 * @since 1810
 */
public class TmaBalanceWsDtoMapper extends AbstractCustomMapper<TmaAverageServiceUsageData, TmaBalanceWsDto>
{
	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Override
	public void mapAtoB(final TmaAverageServiceUsageData a, final TmaBalanceWsDto b, final MappingContext context)
	{
		// other fields are mapped automatically
		mapDefaultAtoB(a, b, context);
	}

	protected void mapDefaultAtoB(final TmaAverageServiceUsageData a, final TmaBalanceWsDto b,
			final MappingContext context)
	{
		context.beginMappingField("unitOfMeasure", getAType(), a, "unit", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getUnitOfMeasure()))
			{
				b.setUnit(a.getUnitOfMeasure());
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField("startDate", getAType(), a, "validFor", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getStartDate() != null)
			{
				b.setValidFor(new TmaTimePeriodWsDto(a.getStartDate(), a.getEndDate()));
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField("unitOfMeasure", getAType(), a, "remainingValueLabel", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getUnitOfMeasure()))
			{
				final String remainingValueLabel = getMessageSource()
						.getMessage(B2ctelcotmfwebservicesConstants.TMA_BALANCE_WSDTO_REMAINING_VALUE_LABEL, null, Locale.ENGLISH);
				final String str = StringUtils.isNotEmpty(a.getUnitOfMeasureName()) ? a.getUnitOfMeasureName() : a.getUnitOfMeasure();
				b.setRemainingValueLabel(remainingValueLabel + " " + a.getRemainingValue() + " " + str);
			}
		}
		finally
		{
			context.endMappingField();
		}

	}

	@Override
	public void mapBtoA(final TmaBalanceWsDto b, final TmaAverageServiceUsageData a, final MappingContext context)
	{
		// other fields are mapped automatically
		try
		{
			if (shouldMap(b, a, context) && StringUtils.isNotEmpty(b.getUnit()))
			{
				a.setUnitOfMeasure(b.getUnit());
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