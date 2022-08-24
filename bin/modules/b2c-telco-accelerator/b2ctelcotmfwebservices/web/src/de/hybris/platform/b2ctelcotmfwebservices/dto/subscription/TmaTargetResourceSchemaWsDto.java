/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Target to the schema describing the product spec resource
 * @since 1810
 */
@ApiModel(description = "Target to the schema describing the product spec resource")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaTargetResourceSchemaWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String type = null;

	private String schemaLocation = null;

	public TmaTargetResourceSchemaWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicated the type of the productSpec described there - for example an E-Line Spec, a CPE spec
	 * @return type
	 **/
	@ApiModelProperty(required = true, value = "Indicated the type of the productSpec described there - for example an E-Line Spec, a CPE spec")
	@NotNull


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaTargetResourceSchemaWsDto schemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
		return this;
	}

	/**
	 * A link to the schema describing the product spec
	 * @return schemaLocation
	 **/
	@ApiModelProperty(value = "A link to the schema describing the product spec")


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
		final TmaTargetResourceSchemaWsDto targetResourceSchema = (TmaTargetResourceSchemaWsDto) o;
		return Objects.equals(this.type, targetResourceSchema.type) &&
				Objects.equals(this.schemaLocation, targetResourceSchema.schemaLocation);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, schemaLocation);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("class TargetResourceSchema {\n");

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

