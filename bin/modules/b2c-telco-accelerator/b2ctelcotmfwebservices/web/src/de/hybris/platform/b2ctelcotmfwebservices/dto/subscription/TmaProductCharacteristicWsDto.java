/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Characteristics of the product to instantiate or to modify.
 */
@ApiModel(description = "Characteristics of the product to instantiate or to modify.")
@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TmaProductCharacteristicWsDto implements Serializable
{
	private static final long serialVersionUID = 8449967402779341L;

	private String name = null;

	private String value = null;

	private String type = null;

	private String schemaLocation = null;

	public TmaProductCharacteristicWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Name of the characteristic
	 * @return name
	 **/
	@ApiModelProperty(value = "Name of the characteristic")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaProductCharacteristicWsDto value(final String value)
	{
		this.value = value;
		return this;
	}

	/**
	 * Value of the characteristic
	 * @return value
	 **/
	@ApiModelProperty(value = "Value of the characteristic")


	public String getValue()
	{
		return value;
	}

	public void setValue(final String value)
	{
		this.value = value;
	}

	public TmaProductCharacteristicWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicates the (class) type of resource
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicates the (class) type of resource")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaProductCharacteristicWsDto schemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
		return this;
	}

	/**
	 * This field provided a link to the schema describing this REST resource.
	 * @return schemaLocation
	 **/
	@ApiModelProperty(value = "This field provided a link to the schema describing this REST resource.")


	public String getSchemaLocation()
	{
		return schemaLocation;
	}

	public void setSchemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
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
		final TmaProductCharacteristicWsDto productCharacteristic = (TmaProductCharacteristicWsDto) o;
		return Objects.equals(this.name, productCharacteristic.name) &&
				Objects.equals(this.value, productCharacteristic.value) &&
				Objects.equals(this.type, productCharacteristic.type) &&
				Objects.equals(this.schemaLocation, productCharacteristic.schemaLocation);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, value, type, schemaLocation);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("class ProductCharacteristic {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    value: ").append(toIndentedString(value)).append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    schemaLocation: ").append(toIndentedString(schemaLocation)).append("\n");
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

