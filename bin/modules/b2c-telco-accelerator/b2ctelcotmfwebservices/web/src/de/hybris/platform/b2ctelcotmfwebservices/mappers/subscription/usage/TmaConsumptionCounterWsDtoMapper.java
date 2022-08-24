/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription.usage;

import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaTimePeriodWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage.TmaConsumptionCounterWsDto;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {TmaAverageServiceUsageData} and {@link TmaConsumptionCounterWsDto}
 *
 * @since 1810
 */
public class TmaConsumptionCounterWsDtoMapper
		extends AbstractCustomMapper<TmaAverageServiceUsageData, TmaConsumptionCounterWsDto>
{
	private static final String UNITOFMEASURE = "unitOfMeasure";
	private static final String UNIT = "unit";

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Override
	public void mapAtoB(final TmaAverageServiceUsageData a, final TmaConsumptionCounterWsDto b, final MappingContext context)
	{
		// other fields are mapped automatically
		mapDefaultAtoB(a, b, context);
		mapUnitOfMeasureAtoB(a, b, context);
	}

	protected void mapUnitOfMeasureAtoB(final TmaAverageServiceUsageData a, final TmaConsumptionCounterWsDto b,
			final MappingContext context)
	{
		context.beginMappingField(UNITOFMEASURE, getAType(), a, UNIT, getBType(), b);
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
	}

	protected void mapDefaultAtoB(final TmaAverageServiceUsageData a, final TmaConsumptionCounterWsDto b,
			final MappingContext context)
	{
		context.beginMappingField(UNITOFMEASURE, getAType(), a, UNIT, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getUnitOfMeasure()))
			{
				b.setCounterType(getMessageSource().getMessage(B2ctelcotmfwebservicesConstants.TMA_CONSUMPTION_COUNTER_COUNTERTYPE,
						null, Locale.ENGLISH));
				b.setLevel(getMessageSource()
						.getMessage(B2ctelcotmfwebservicesConstants.TMA_CONSUMPTION_COUNTER_LEVEL, null, Locale.ENGLISH));
				b.setUnit(a.getUnitOfMeasure());
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField(UNITOFMEASURE, getAType(), a, "UnitOfMeasureName", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getUnitOfMeasure()))
			{
				final String valueLabelMessage = getMessageSource()
						.getMessage(B2ctelcotmfwebservicesConstants.TMA_USAGE_CONSUMPTION_REPORT_WSDTO_VALUELABEL, null,
								Locale.ENGLISH);
				b.setValueLabel(a.getValue() + " " + a.getUnitOfMeasureName() + " " + valueLabelMessage);
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
	}

	@Override
	public void mapBtoA(final TmaConsumptionCounterWsDto b, final TmaAverageServiceUsageData a, final MappingContext context)
	{
		// other fields are mapped automatically
		context.beginMappingField(UNIT, getBType(), b, UNITOFMEASURE, getAType(), a);
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