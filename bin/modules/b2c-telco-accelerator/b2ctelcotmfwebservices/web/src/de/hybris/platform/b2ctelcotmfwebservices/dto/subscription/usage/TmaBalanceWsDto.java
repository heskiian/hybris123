/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage;

import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaTimePeriodWsDto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * The balance defines the remaining allowed product usage quantity in terms of volume, time, currency or events. It
 * corresponds to the initial allowed usage quantity minus the usage consumed on the bucket.
 * @since 1810
 */
@ApiModel(description = "The balance defines the remaining allowed product usage quantity in terms of volume, time, currency or events. It corresponds to the initial allowed usage quantity minus the usage consumed on the bucket.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaBalanceWsDto implements Serializable
{
	private static final long serialVersionUID = 8449967402779341L;

	private String unit = null;

	private Long remainingValue = null;

	private String remainingValueLabel = null;

	private TmaTimePeriodWsDto validFor = null;

	private String type = null;

	private String schemaLocation = null;

	public TmaBalanceWsDto unit(final String unit)
	{
		this.unit = unit;
		return this;
	}

	/**
	 * Code of the unit used to specify the given value of the balance. It could be different from the bucket unit
	 * @return unit
	 **/
	@ApiModelProperty(value = "Code of the unit used to specify the given value of the balance. It could be different from the bucket unit")


	public String getUnit()
	{
		return unit;
	}

	public void setUnit(final String unit)
	{
		this.unit = unit;
	}

	public TmaBalanceWsDto remainingValue(final Long remainingValue)
	{
		this.remainingValue = remainingValue;
		return this;
	}

	/**
	 * Numeric remaining value for the bucket given in the balance unit (for example 1.9). This numeric value could be
	 * used for
	 * calculation for example
	 * @return remainingValue
	 **/
	@ApiModelProperty(value = "Numeric remaining value for the bucket given in the balance unit (for example 1.9). This numeric value could be used for calculation for example")


	public Long getRemainingValue()
	{
		return remainingValue;
	}

	public void setRemainingValue(final Long remainingValue)
	{
		this.remainingValue = remainingValue;
	}

	public TmaBalanceWsDto remainingValueLabel(final String remainingValueLabel)
	{
		this.remainingValueLabel = remainingValueLabel;
		return this;
	}

	/**
	 * Remaining value in a formatted string for the bucket given in the balance unit (for example 1.9 Gb). This
	 * formatted string
	 * could be used for display needs for example
	 * @return remainingValueLabel
	 **/
	@ApiModelProperty(value = "Remaining value in a formatted string for the bucket given in the balance unit (for example 1.9 Gb). This formatted string could be used for display needs for example")


	public String getRemainingValueLabel()
	{
		return remainingValueLabel;
	}

	public void setRemainingValueLabel(final String remainingValueLabel)
	{
		this.remainingValueLabel = remainingValueLabel;
	}

	public TmaBalanceWsDto validFor(final TmaTimePeriodWsDto validFor)
	{
		this.validFor = validFor;
		return this;
	}

	/**
	 * Get validFor
	 * @return validFor
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaTimePeriodWsDto getValidFor()
	{
		return validFor;
	}

	public void setValidFor(final TmaTimePeriodWsDto validFor)
	{
		this.validFor = validFor;
	}

	public TmaBalanceWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicates the (class) type of the bucket balance
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicates the (class) type of the bucket balance")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaBalanceWsDto schemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
		return this;
	}

	/**
	 * Link to the schema describing the REST resource
	 * @return schemaLocation
	 **/
	@ApiModelProperty(value = "Link to the schema describing the REST resource")


	public String getSchemaLocation()
	{
		return schemaLocation;
	}

	public void setSchemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
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
		final TmaBalanceWsDto balance = (TmaBalanceWsDto) o;
		return Objects.equals(this.unit, balance.unit) &&
				Objects.equals(this.remainingValue, balance.remainingValue) &&
				Objects.equals(this.remainingValueLabel, balance.remainingValueLabel) &&
				Objects.equals(this.validFor, balance.validFor) &&
				Objects.equals(this.type, balance.type) &&
				Objects.equals(this.schemaLocation, balance.schemaLocation);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(unit, remainingValue, remainingValueLabel, validFor, type, schemaLocation);
	}

	@Override
	public String toString()
	{
		return " Balance {\n"
				+ ("    unit: ") + (toIndentedString(unit)) + ("\n")
				+ ("    remainingValue: ") + (toIndentedString(remainingValue)) + ("\n")
				+ ("    remainingValueLabel: ") + (toIndentedString(remainingValueLabel)) + ("\n")
				+ ("    validFor: ") + (toIndentedString(validFor)) + ("\n")
				+ ("    type: ") + (toIndentedString(type)) + ("\n")
				+ ("    schemaLocation: ") + (toIndentedString(schemaLocation)) + ("\n")
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
}
