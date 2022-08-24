/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.timeperiod;

import de.hybris.platform.b2ctelcofacades.data.TmaTimePeriodData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriodType;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for end date time attribute between {@link TmaTimePeriodData} and {@link TimePeriodType}
 *
 * @since 1907
 */
public class TimePeriodEndDateTimeAttributeMapper extends TmaAttributeMapper<TmaTimePeriodData, TimePeriodType>
{
	@Override
	public void populateTargetAttributeFromSource(TmaTimePeriodData source, TimePeriodType target,
			MappingContext context)
	{
		if (StringUtils.isEmpty(source.getEndDateMonth()) || StringUtils.isEmpty(source.getEndDateYear()))
		{
			return;
		}

		Calendar endDateCalendar = new GregorianCalendar(Integer.parseInt(source.getEndDateYear()),
				Integer.parseInt(source.getEndDateMonth()) - 1, 1);
		endDateCalendar.set(Calendar.DAY_OF_MONTH, endDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = endDateCalendar.getTime();
		target.setEndDateTime(endDate);
	}
}
