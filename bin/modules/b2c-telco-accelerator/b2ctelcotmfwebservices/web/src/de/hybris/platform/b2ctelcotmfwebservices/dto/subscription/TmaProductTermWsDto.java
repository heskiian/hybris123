/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Quantity;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaTimePeriodWsDto;

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
 * This represent a commitment with a duration
 *
 * @since 1810
 */
@ApiModel(description = "This represent a commitment with a duration")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaProductTermWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("duration")
	private transient Quantity duration = null;

	@JsonProperty("validFor")
	private TmaTimePeriodWsDto validFor = null;

	@JsonProperty("@type")
	private String type = null;

	public TmaProductTermWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Name of the commitment term
	 *
	 * @return name
	 **/
	@ApiModelProperty(value = "Name of the commitment term")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaProductTermWsDto description(final String description)
	{
		this.description = description;
		return this;
	}

	/**
	 * Description of the commitment term
	 *
	 * @return description
	 **/
	@ApiModelProperty(value = "Description of the commitment term")


	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public TmaProductTermWsDto duration(final Quantity duration)
	{
		this.duration = duration;
		return this;
	}

	/**
	 * Get duration
	 *
	 * @return duration
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public Quantity getDuration()
	{
		return duration;
	}

	public void setDuration(final Quantity duration)
	{
		this.duration = duration;
	}

	public TmaProductTermWsDto validFor(final TmaTimePeriodWsDto validFor)
	{
		this.validFor = validFor;
		return this;
	}

	/**
	 * Get validFor
	 *
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

	public TmaProductTermWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicate the class (type) of the product term
	 *
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicate the class (type) of the product term")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
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
		final TmaProductTermWsDto productTerm = (TmaProductTermWsDto) o;
		return Objects.equals(this.name, productTerm.name) &&
				Objects.equals(this.description, productTerm.description) &&
				Objects.equals(this.duration, productTerm.duration) &&
				Objects.equals(this.validFor, productTerm.validFor) &&
				Objects.equals(this.type, productTerm.type);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, description, duration, validFor, type);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("class ProductTerm {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
		sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

