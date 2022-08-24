/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage;

import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaProductRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;
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
 * The consumption counters detail for example the different kind of consumption done on the bucket
 * @since 1810
 */
@ApiModel(description = "The consumption counters detail for example the different kind of consumption done on the bucket")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaConsumptionCounterWsDto implements Serializable
{
	private static final long serialVersionUID = 8449967402779341L;

	private String counterType = null;

	private String level = null;

	private String unit = null;

	private Long value = null;

	private String valueLabel = null;

	private TmaTimePeriodWsDto validFor = null;

	private String type = null;

	private String schemaLocation = null;

	private TmaProductRefWsDto product = null;

	private TmaRelatedPartyWsDto user = null;

	public TmaConsumptionCounterWsDto counterType(final String counterType)
	{
		this.counterType = counterType;
		return this;
	}

	/**
	 * Type of the consumption counter. We can give for example a counter of the used value
	 * @return counterType
	 **/
	@ApiModelProperty(value = "Type of the consumption counter. We can give for example a counter of the used value")


	public String getCounterType()
	{
		return counterType;
	}

	public void setCounterType(final String counterType)
	{
		this.counterType = counterType;
	}

	public TmaConsumptionCounterWsDto level(final String level)
	{
		this.level = level;
		return this;
	}

	/**
	 * Counter level. The given counter can be given globally for the bucket or detailed by user or by product for
	 * example in case
	 * of shared bucket
	 * @return level
	 **/
	@ApiModelProperty(value = "Counter level. The given counter can be given globally for the bucket or detailed by user or by product for example in case of shared bucket")


	public String getLevel()
	{
		return level;
	}

	public void setLevel(final String level)
	{
		this.level = level;
	}

	public TmaConsumptionCounterWsDto unit(final String unit)
	{
		this.unit = unit;
		return this;
	}

	/**
	 * Unit of the counter
	 * @return unit
	 **/
	@ApiModelProperty(value = "Unit of the counter")


	public String getUnit()
	{
		return unit;
	}

	public void setUnit(final String unit)
	{
		this.unit = unit;
	}

	public TmaConsumptionCounterWsDto value(final Long value)
	{
		this.value = value;
		return this;
	}

	/**
	 * Numeric value of the bucket counter in the given unit
	 * @return value
	 **/
	@ApiModelProperty(value = "Numeric value of the bucket counter in the given unit")


	public Long getValue()
	{
		return value;
	}

	public void setValue(final Long value)
	{
		this.value = value;
	}

	public TmaConsumptionCounterWsDto valueLabel(final String valueLabel)
	{
		this.valueLabel = valueLabel;
		return this;
	}

	/**
	 * Value of the counter in a formatted string used for display needs for example
	 * @return valueLabel
	 **/
	@ApiModelProperty(value = "Value of the counter in a formatted string used for display needs for example")


	public String getValueLabel()
	{
		return valueLabel;
	}

	public void setValueLabel(final String valueLabel)
	{
		this.valueLabel = valueLabel;
	}

	public TmaConsumptionCounterWsDto validFor(final TmaTimePeriodWsDto validFor)
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

	public TmaConsumptionCounterWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicates the (class) type of consumption counter
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicates the (class) type of consumption counter")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaConsumptionCounterWsDto schemaLocation(final String schemaLocation)
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

	public TmaConsumptionCounterWsDto product(final TmaProductRefWsDto product)
	{
		this.product = product;
		return this;
	}

	/**
	 * Get product
	 * @return product
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaProductRefWsDto getProduct()
	{
		return product;
	}

	public void setProduct(final TmaProductRefWsDto product)
	{
		this.product = product;
	}

	public TmaConsumptionCounterWsDto user(final TmaRelatedPartyWsDto user)
	{
		this.user = user;
		return this;
	}

	/**
	 * Get user
	 * @return user
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaRelatedPartyWsDto getUser()
	{
		return user;
	}

	public void setUser(final TmaRelatedPartyWsDto user)
	{
		this.user = user;
	}


	@Override
	public boolean equals(final java.lang.Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		final TmaConsumptionCounterWsDto consumptionCounter = (TmaConsumptionCounterWsDto) o;
		return Objects.equals(this.counterType, consumptionCounter.counterType) &&
				Objects.equals(this.level, consumptionCounter.level) &&
				Objects.equals(this.unit, consumptionCounter.unit) &&
				Objects.equals(this.value, consumptionCounter.value) &&
				Objects.equals(this.valueLabel, consumptionCounter.valueLabel) &&
				Objects.equals(this.validFor, consumptionCounter.validFor) &&
				Objects.equals(this.type, consumptionCounter.type) &&
				Objects.equals(this.schemaLocation, consumptionCounter.schemaLocation) &&
				Objects.equals(this.product, consumptionCounter.product) &&
				Objects.equals(this.user, consumptionCounter.user);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(counterType, level, unit, value, valueLabel, validFor, type, schemaLocation, product, user);
	}

	@Override
	public String toString()
	{
		return "class ConsumptionCounter {\n"
				+ ("    counterType: ") + (toIndentedString(counterType)) + ("\n")
				+ ("    level: ") + (toIndentedString(level)) + ("\n")
				+ ("    unit: ") + (toIndentedString(unit)) + ("\n")
				+ ("    value: ") + (toIndentedString(value)) + ("\n")
				+ ("    valueLabel: ") + (toIndentedString(valueLabel)) + ("\n")
				+ ("    validFor: ") + (toIndentedString(validFor)) + ("\n")
				+ ("    type: ") + (toIndentedString(type)) + ("\n")
				+ ("    schemaLocation: ") + (toIndentedString(schemaLocation)) + ("\n")
				+ ("    product: ") + (toIndentedString(product)) + ("\n")
				+ ("    user: ") + (toIndentedString(user)) + ("\n")
				+ ("}");
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(final java.lang.Object o)
	{
		if (o == null)
		{
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
