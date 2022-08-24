/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto;

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
 * Reference of a product
 *
 * @since 1810
 */
@ApiModel(description = "Reference of a product")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaProductRefWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;


	private String id = null;


	private String name = null;


	private String href = null;


	private String publicIdentifier = null;


	private TmaRelatedPartyWsDto user = null;

	public TmaProductRefWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the product
	 *
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique identifier of the product")


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaProductRefWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Reference of the product
	 *
	 * @return href
	 **/
	@ApiModelProperty(value = "Reference of the product")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaProductRefWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Subscribed Product name
	 *
	 * @return name
	 **/
	@ApiModelProperty(value = "Subscribed Product name")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaProductRefWsDto publicIdentifier(final String publicIdentifier)
	{
		this.publicIdentifier = publicIdentifier;
		return this;
	}


	/**
	 * Public number associated to the product (msisdn number for mobile line for example)
	 *
	 * @return publicIdentifier
	 **/
	@ApiModelProperty(value = "Public number associated to the product (msisdn number for mobile line for example)")


	public String getPublicIdentifier()
	{
		return publicIdentifier;
	}

	public void setPublicIdentifier(final String publicIdentifier)
	{
		this.publicIdentifier = publicIdentifier;
	}

	public TmaProductRefWsDto user(final TmaRelatedPartyWsDto user)
	{
		this.user = user;
		return this;
	}

	/**
	 * Get user
	 *
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
		final TmaProductRefWsDto productRef = (TmaProductRefWsDto) o;
		return Objects.equals(this.id, productRef.id) &&
				Objects.equals(this.publicIdentifier, productRef.publicIdentifier) &&
				Objects.equals(this.user, productRef.user) && Objects.equals(this.name, productRef.name)
				&& Objects.equals(this.href, productRef.href);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, publicIdentifier, user, href, name);
	}

	@Override
	public String toString()
	{
		return (" ProductRef {\n")
				+ ("    id: ") + toIndentedString(id) + ("\n")
				+ ("    publicIdentifier: ") + (toIndentedString(publicIdentifier)) + ("\n")
				+ ("    user: ") + (toIndentedString(user)) + ("\n")
				+ ("    href: ") + (toIndentedString(href)) + ("\n")
				+ ("    name: ") + (toIndentedString(name)) + ("\n")
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

