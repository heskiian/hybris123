/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * A date time period between a start date and an end date
 * @since 1810
 */
@ApiModel(description = "A date time period between a start date and an end date")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaTimePeriodWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date startDateTime = null;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date endDateTime = null;

	public TmaTimePeriodWsDto startDateTime(final java.util.Date startDateTime)
	{
		this.startDateTime = startDateTime;
		return this;
	}

	/**
	 * Start date time of the period
	 * @return startDateTime
	 **/
	@ApiModelProperty(value = "Start date time of the period")

	@Valid

	public java.util.Date getStartDateTime()
	{
		return startDateTime;
	}

	public void setStartDateTime(final java.util.Date startDateTime)
	{
		this.startDateTime = startDateTime;
	}

	public TmaTimePeriodWsDto endDateTime(final java.util.Date endDateTime)
	{
		this.endDateTime = endDateTime;
		return this;
	}

	/**
	 * End date time of the period
	 * @return endDateTime
	 **/
	@ApiModelProperty(value = "End date time of the period")

	@Valid

	public java.util.Date getEndDateTime()
	{
		return endDateTime;
	}

	public void setEndDateTime(final java.util.Date endDateTime)
	{
		this.endDateTime = endDateTime;
	}


	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		final TmaTimePeriodWsDto timePeriod = (TmaTimePeriodWsDto) o;
		return Objects.equals(this.startDateTime, timePeriod.startDateTime)
				&& Objects.equals(this.endDateTime, timePeriod.endDateTime);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(startDateTime, endDateTime);
	}

	@Override
	public String toString()
	{
		return (" TimePeriod {\n")
				+ ("    startDateTime: ") + (toIndentedString(startDateTime)) + ("\n")
				+ ("    endDateTime: ") + (toIndentedString(endDateTime)) + ("\n")
				+ ("}");
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(final Object o)
	{
		if (o == null)
		{
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	public TmaTimePeriodWsDto(final Date startDate, final Date endDate)
	{
		this.startDateTime = startDate;
		this.endDateTime = endDate;
	}

	public TmaTimePeriodWsDto()
	{
		//Default Constructor
	}
}

