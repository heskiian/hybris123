/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaTimePeriodWsDto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Is an amount, usually of money, that modifies the price charged for a Product
 * @since 1810
 */
@ApiModel(description = "Is an amount, usually of money, that modifies the price charged for a Product")
@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TmaPriceAlterationWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String name = null;

	private String description = null;

	private String priceType = null;

	private String unitOfMeasure = null;

	private String recurringChargePeriod = null;

	private TmaTimePeriodWsDto validFor = null;

	private Integer priority = null;

	private String type = null;

	private String schemaLocation = null;

	private TmaPriceWsDto price = null;

	public TmaPriceAlterationWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Link to the schema describing this REST resource.
	 * @return id
	 **/
	@ApiModelProperty(value = "Link to the schema describing this REST resource.")


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaPriceAlterationWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * A short descriptive name such as \"Monthly discount\"
	 * @return name
	 **/
	@ApiModelProperty(value = "A short descriptive name such as \"Monthly discount\"")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaPriceAlterationWsDto description(final String description)
	{
		this.description = description;
		return this;
	}

	/**
	 * A narrative that explains in detail the semantics of this ProdPriceAlteration
	 * @return description
	 **/
	@ApiModelProperty(value = "A narrative that explains in detail the semantics of this ProdPriceAlteration")


	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public TmaPriceAlterationWsDto priceType(final String priceType)
	{
		this.priceType = priceType;
		return this;
	}

	/**
	 * A category that describes the price such as recurring, one shot and so forth
	 * @return priceType
	 **/
	@ApiModelProperty(value = "A category that describes the price such as recurring, one shot and so forth")


	public String getPriceType()
	{
		return priceType;
	}

	public void setPriceType(final String priceType)
	{
		this.priceType = priceType;
	}

	public TmaPriceAlterationWsDto unitOfMeasure(final String unitOfMeasure)
	{
		this.unitOfMeasure = unitOfMeasure;
		return this;
	}

	/**
	 * Could be minutes, GB...
	 * @return unitOfMeasure
	 **/
	@ApiModelProperty(value = "Could be minutes, GB...")


	public String getUnitOfMeasure()
	{
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(final String unitOfMeasure)
	{
		this.unitOfMeasure = unitOfMeasure;
	}

	public TmaPriceAlterationWsDto recurringChargePeriod(final String recurringChargePeriod)
	{
		this.recurringChargePeriod = recurringChargePeriod;
		return this;
	}

	/**
	 * Could be month, week...
	 * @return recurringChargePeriod
	 **/
	@ApiModelProperty(value = "Could be month, week...")


	public String getRecurringChargePeriod()
	{
		return recurringChargePeriod;
	}

	public void setRecurringChargePeriod(final String recurringChargePeriod)
	{
		this.recurringChargePeriod = recurringChargePeriod;
	}

	public TmaPriceAlterationWsDto validFor(final TmaTimePeriodWsDto validFor)
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

	public TmaPriceAlterationWsDto priority(final Integer priority)
	{
		this.priority = priority;
		return this;
	}

	/**
	 * Priority level for applying this alteration among all the defined alterations
	 * @return priority
	 **/
	@ApiModelProperty(value = "Priority level for applying this alteration among all the defined alterations")


	public Integer getPriority()
	{
		return priority;
	}

	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

	public TmaPriceAlterationWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicated the class (type) of the price alteration.
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicated the class (type) of the price alteration.")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaPriceAlterationWsDto schemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
		return this;
	}

	/**
	 * A link to the schema describing the price alteration.
	 * @return schemaLocation
	 **/
	@ApiModelProperty(value = "A link to the schema describing the price alteration.")


	public String getSchemaLocation()
	{
		return schemaLocation;
	}

	public void setSchemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
	}

	public TmaPriceAlterationWsDto price(final TmaPriceWsDto price)
	{
		this.price = price;
		return this;
	}

	/**
	 * Get price
	 * @return price
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaPriceWsDto getPrice()
	{
		return price;
	}

	public void setPrice(final TmaPriceWsDto price)
	{
		this.price = price;
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
		final TmaPriceAlterationWsDto priceAlteration = (TmaPriceAlterationWsDto) o;
		return Objects.equals(this.id, priceAlteration.id) &&
				Objects.equals(this.name, priceAlteration.name) &&
				Objects.equals(this.description, priceAlteration.description) &&
				Objects.equals(this.priceType, priceAlteration.priceType) &&
				Objects.equals(this.unitOfMeasure, priceAlteration.unitOfMeasure) &&
				Objects.equals(this.recurringChargePeriod, priceAlteration.recurringChargePeriod) &&
				Objects.equals(this.validFor, priceAlteration.validFor) &&
				Objects.equals(this.priority, priceAlteration.priority) &&
				Objects.equals(this.type, priceAlteration.type) &&
				Objects.equals(this.schemaLocation, priceAlteration.schemaLocation) &&
				Objects.equals(this.price, priceAlteration.price);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name, description, priceType, unitOfMeasure, recurringChargePeriod, validFor, priority, type,
				schemaLocation, price);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("class PriceAlteration {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    priceType: ").append(toIndentedString(priceType)).append("\n");
		sb.append("    unitOfMeasure: ").append(toIndentedString(unitOfMeasure)).append("\n");
		sb.append("    recurringChargePeriod: ").append(toIndentedString(recurringChargePeriod)).append("\n");
		sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
		sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    schemaLocation: ").append(toIndentedString(schemaLocation)).append("\n");
		sb.append("    price: ").append(toIndentedString(price)).append("\n");
		sb.append("}");
		return sb.toString();
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

