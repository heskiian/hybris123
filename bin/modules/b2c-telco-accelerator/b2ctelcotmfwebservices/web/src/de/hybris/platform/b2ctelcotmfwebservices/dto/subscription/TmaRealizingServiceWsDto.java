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
 * RealizingService reference. RealizingService is a link to the service that realizes the product.
 * @since 1810
 */
@ApiModel(description = "RealizingService reference. RealizingService is a link to the service that realizes the product.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaRealizingServiceWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String href = null;

	private String name = null;

	private String role = null;

	private String referredType = null;

	public TmaRealizingServiceWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the service
	 * @return id
	 **/
	@ApiModelProperty(required = true, value = "Unique identifier of the service")
	@NotNull


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaRealizingServiceWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Reference of the service
	 * @return href
	 **/
	@ApiModelProperty(value = "Reference of the service")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaRealizingServiceWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Name of the service
	 * @return name
	 **/
	@ApiModelProperty(value = "Name of the service")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaRealizingServiceWsDto role(final String role)
	{
		this.role = role;
		return this;
	}

	/**
	 * Role of the realizing service
	 * @return role
	 **/
	@ApiModelProperty(value = "Role of the realizing service")


	public String getRole()
	{
		return role;
	}

	public void setRole(final String role)
	{
		this.role = role;
	}

	public TmaRealizingServiceWsDto referredType(final String referredType)
	{
		this.referredType = referredType;
		return this;
	}

	/**
	 * Indicate the class (type) of the realizing service.
	 * @return referredType
	 **/
	@ApiModelProperty(value = "Indicate the class (type) of the realizing service.")


	public String getReferredType()
	{
		return referredType;
	}

	public void setReferredType(final String referredType)
	{
		this.referredType = referredType;
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
		final TmaRealizingServiceWsDto realizingService = (TmaRealizingServiceWsDto) o;
		return Objects.equals(this.id, realizingService.id) &&
				Objects.equals(this.href, realizingService.href) &&
				Objects.equals(this.name, realizingService.name) &&
				Objects.equals(this.role, realizingService.role) &&
				Objects.equals(this.referredType, realizingService.referredType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, name, role, referredType);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("class RealizingService {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    href: ").append(toIndentedString(href)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    role: ").append(toIndentedString(role)).append("\n");
		sb.append("    referredType: ").append(toIndentedString(referredType)).append("\n");
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
