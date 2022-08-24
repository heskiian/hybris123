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
 * Place defines the places where the products is installed or has a footprint (a place could be used for geo fencing
 * @since 1810.
 */
@ApiModel(description = "Place defines the places where the products is installed or has a footprint (a place could be used for geo fencing")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaPlaceWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String href = null;

	private String name = null;

	private String role = null;

	private String referredType = null;

	private String schemaLocation = null;

	public TmaPlaceWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the place
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique identifier of the place")


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaPlaceWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Reference of the place
	 * @return href
	 **/
	@ApiModelProperty(value = "Reference of the place")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaPlaceWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * A user-friendly name for the place
	 * @return name
	 **/
	@ApiModelProperty(value = "A user-friendly name for the place")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaPlaceWsDto role(final String role)
	{
		this.role = role;
		return this;
	}

	/**
	 * Role of the place (for instance: 'installation site', 'A-extremity site', 'Supervised Perimeter' etc...)
	 * @return role
	 **/
	@ApiModelProperty(required = true, value = "Role of the place (for instance: 'installation site', 'A-extremity site', 'Supervised Perimeter' etc...)")
	@NotNull


	public String getRole()
	{
		return role;
	}

	public void setRole(final String role)
	{
		this.role = role;
	}

	public TmaPlaceWsDto referredType(final String referredType)
	{
		this.referredType = referredType;
		return this;
	}

	/**
	 * Indicate the type of Place class (example: 'geographicAddress')
	 * @return referredType
	 **/
	@ApiModelProperty(value = "Indicate the type of Place class (example: 'geographicAddress')")


	public String getReferredType()
	{
		return referredType;
	}

	public void setReferredType(final String referredType)
	{
		this.referredType = referredType;
	}

	public TmaPlaceWsDto schemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
		return this;
	}

	/**
	 * A link to the schema describing this REST resource
	 * @return schemaLocation
	 **/
	@ApiModelProperty(value = "A link to the schema describing this REST resource")


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
		final TmaPlaceWsDto place = (TmaPlaceWsDto) o;
		return Objects.equals(this.id, place.id) &&
				Objects.equals(this.href, place.href) &&
				Objects.equals(this.name, place.name) &&
				Objects.equals(this.role, place.role) &&
				Objects.equals(this.referredType, place.referredType) &&
				Objects.equals(this.schemaLocation, place.schemaLocation);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, name, role, referredType, schemaLocation);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("class Place {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    href: ").append(toIndentedString(href)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    role: ").append(toIndentedString(role)).append("\n");
		sb.append("    referredType: ").append(toIndentedString(referredType)).append("\n");
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
