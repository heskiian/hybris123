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
 * This attribute Mapper class maps data for start date time attribute between {@link TmaTimePeriodData} and
 * {@link TimePeriodType}
 *
 * @since 1907
 */
public class TimePeriodStartDateTimeAttributeMapper extends TmaAttributeMapper<TmaTimePeriodData, TimePeriodType>
{
	@Override
	public void populateTargetAttributeFromSource(TmaTimePeriodData source, TimePeriodType target,
			MappingContext context)
	{
		if (StringUtils.isEmpty(source.getStartDateMonth()) || StringUtils.isEmpty(source.getStartDateYear()))
		{
			return;
		}

		Calendar startDateCalendar = new GregorianCalendar(Integer.parseInt(source.getStartDateYear()),
			Integer.parseInt(source.getStartDateMonth()) - 1, 1);
		startDateCalendar.set(Calendar.DAY_OF_MONTH, startDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date startDate = startDateCalendar.getTime();
		target.setStartDateTime(startDate);
	}
}
