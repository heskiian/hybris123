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
 * A BillingAccount is a detailed description of a bill structure
 * @since 1810
 */
@ApiModel(description = "A BillingAccount is a detailed description of a bill structure")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaBillingAccountRefWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String href = null;

	private String name = null;

	private String referredType = null;

	public TmaBillingAccountRefWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identier of the billing account
	 * @return id
	 **/
	@ApiModelProperty(required = true, value = "Unique identier of the billing account")
	@NotNull


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaBillingAccountRefWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Reference of the billing account
	 * @return href
	 **/
	@ApiModelProperty(value = "Reference of the billing account")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaBillingAccountRefWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Name of the billing account
	 * @return name
	 **/
	@ApiModelProperty(value = "Name of the billing account")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaBillingAccountRefWsDto referredType(final String referredType)
	{
		this.referredType = referredType;
		return this;
	}

	/**
	 * Indicates the (class) type of the billing account
	 * @return referredType
	 **/
	@ApiModelProperty(value = "Indicates the (class) type of the billing account")


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
		final TmaBillingAccountRefWsDto billingAccountRef = (TmaBillingAccountRefWsDto) o;
		return Objects.equals(this.id, billingAccountRef.id) &&
				Objects.equals(this.href, billingAccountRef.href) &&
				Objects.equals(this.name, billingAccountRef.name) &&
				Objects.equals(this.referredType, billingAccountRef.referredType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, name, referredType);
	}

	@Override
	public String toString()
	{
		return " BillingAccountRef {\n"
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
