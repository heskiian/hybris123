/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Provides all amounts (tax included, duty free, tax rate), used currency and percentage applied for price and price
 * alteration
 *
 * @since 1810
 */
@ApiModel(description = "Provides all amounts (tax included, duty free, tax rate), used currency and percentage applied for price and price alteration")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaPriceWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	@JsonProperty("taxIncludedAmount")
	private transient Money taxIncludedAmount = null;

	@JsonProperty("dutyFreeAmount")
	private transient Money dutyFreeAmount = null;

	@JsonProperty("percentage")
	private Float percentage = null;

	@JsonProperty("taxRate")
	private Float taxRate = null;

	@JsonProperty("@type")
	private String type = null;

	@JsonProperty("@schemaLocation")
	private String schemaLocation = null;

	public TmaPriceWsDto taxIncludedAmount(final Money taxIncludedAmount)
	{
		this.taxIncludedAmount = taxIncludedAmount;
		return this;
	}

	/**
	 * Get taxIncludedAmount
	 *
	 * @return taxIncludedAmount
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public Money getTaxIncludedAmount()
	{
		return taxIncludedAmount;
	}

	public void setTaxIncludedAmount(final Money taxIncludedAmount)
	{
		this.taxIncludedAmount = taxIncludedAmount;
	}

	public TmaPriceWsDto dutyFreeAmount(final Money dutyFreeAmount)
	{
		this.dutyFreeAmount = dutyFreeAmount;
		return this;
	}

	/**
	 * Get dutyFreeAmount
	 *
	 * @return dutyFreeAmount
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public Money getDutyFreeAmount()
	{
		return dutyFreeAmount;
	}

	public void setDutyFreeAmount(final Money dutyFreeAmount)
	{
		this.dutyFreeAmount = dutyFreeAmount;
	}

	public TmaPriceWsDto percentage(final Float percentage)
	{
		this.percentage = percentage;
		return this;
	}

	/**
	 * Percentage applied
	 *
	 * @return percentage
	 **/
	@ApiModelProperty(value = "Percentage applied")


	public Float getPercentage()
	{
		return percentage;
	}

	public void setPercentage(final Float percentage)
	{
		this.percentage = percentage;
	}

	public TmaPriceWsDto taxRate(final Float taxRate)
	{
		this.taxRate = taxRate;
		return this;
	}

	/**
	 * Applied tax rate on amount
	 *
	 * @return taxRate
	 **/
	@ApiModelProperty(value = "Applied tax rate on amount")


	public Float getTaxRate()
	{
		return taxRate;
	}

	public void setTaxRate(final Float taxRate)
	{
		this.taxRate = taxRate;
	}

	public TmaPriceWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicates the (class) type of the price
	 *
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicates the (class) type of the price")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaPriceWsDto schemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
		return this;
	}

	/**
	 * Link to the schema describing this REST resource.
	 *
	 * @return schemaLocation
	 **/
	@ApiModelProperty(value = "Link to the schema describing this REST resource.")


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
		final TmaPriceWsDto price = (TmaPriceWsDto) o;
		return Objects.equals(this.taxIncludedAmount, price.taxIncludedAmount) &&
				Objects.equals(this.dutyFreeAmount, price.dutyFreeAmount) &&
				Objects.equals(this.percentage, price.percentage) &&
				Objects.equals(this.taxRate, price.taxRate) &&
				Objects.equals(this.type, price.type) &&
				Objects.equals(this.schemaLocation, price.schemaLocation);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(taxIncludedAmount, dutyFreeAmount, percentage, taxRate, type, schemaLocation);
	}

	@Override
	public String toString()
	{
		return " Price {\n"
				+ ("    taxIncludedAmount: ") + (toIndentedString(taxIncludedAmount)) + ("\n")
				+ ("    dutyFreeAmount: ") + (toIndentedString(dutyFreeAmount)) + ("\n")
				+ ("    percentage: ") + (toIndentedString(percentage)) + ("\n")
				+ ("    taxRate: ") + (toIndentedString(taxRate)) + ("\n")
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

