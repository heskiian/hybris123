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
 * ProductOffering reference. A product offering represents entities that are orderable from the provider of the
 * catalog, this
 * resource includes pricing information.
 * @since 1810
 */
@ApiModel(description = "ProductOffering reference. A product offering represents entities that are orderable from the provider of the catalog, this resource includes pricing information.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaProductOfferingRefWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String href = null;

	private String name = null;

	private String referredType = null;

	public TmaProductOfferingRefWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the product offering
	 * @return id
	 **/
	@ApiModelProperty(required = true, value = "Unique identifier of the product offering")
	@NotNull


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaProductOfferingRefWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Reference of the product offering
	 * @return href
	 **/
	@ApiModelProperty(value = "Reference of the product offering")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaProductOfferingRefWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Name of the product offering
	 * @return name
	 **/
	@ApiModelProperty(value = "Name of the product offering")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaProductOfferingRefWsDto referredType(final String referredType)
	{
		this.referredType = referredType;
		return this;
	}

	/**
	 * Type (class) of the product offering
	 * @return referredType
	 **/
	@ApiModelProperty(value = "Type (class) of the product offering")


	public String getReferredType()
	{
		return referredType;
	}

	public void setReferredType(final String referredType)
	{
		this.referredType = referredType;
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
		final TmaProductOfferingRefWsDto productOfferingRef = (TmaProductOfferingRefWsDto) o;
		return Objects.equals(this.id, productOfferingRef.id) &&
				Objects.equals(this.href, productOfferingRef.href) &&
				Objects.equals(this.name, productOfferingRef.name) &&
				Objects.equals(this.referredType, productOfferingRef.referredType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, name, referredType);
	}

	@Override
	public String toString()
	{
		return ("class ProductOfferingRef {\n")
				+ ("    id: ") + (toIndentedString(id)) + ("\n")
				+ ("    href: ") + (toIndentedString(href)) + ("\n")
				+ ("    name: ") + (toIndentedString(name)) + ("\n")
				+ ("    referredType: ") + (toIndentedString(referredType)) + ("\n")
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

