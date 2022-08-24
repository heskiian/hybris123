/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.recommendation;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * ShoppingCart (ShoppingCartRef) . The shopping cart which the recommendation is related with.
 *
 * @since 1903
 */
@ApiModel(description = "ShoppingCart (ShoppingCartRef) . The shopping cart which the recommendation is related with.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaShoppingCartRefWsDto implements Serializable
{
	private static final long serialVersionUID = 8449967402779341L;

	@JsonProperty("href")
	private String href = null;

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("@referredType")
	private String referredType = null;

	public TmaShoppingCartRefWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Hypertext Reference of the shopping cart.
	 *
	 * @return href
	 **/
	@ApiModelProperty(value = "Hypertext Reference of the shopping cart.")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaShoppingCartRefWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of shopping cart
	 *
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique identifier of shopping cart")


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaShoppingCartRefWsDto referredType(final String referredType)
	{
		this.referredType = referredType;
		return this;
	}

	/**
	 * The actual type of the target instance when needed for disambiguation.
	 *
	 * @return referredType
	 **/
	@ApiModelProperty(value = "The actual type of the target instance when needed for disambiguation.")


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
		final TmaShoppingCartRefWsDto shoppingCartRef = (TmaShoppingCartRefWsDto) o;
		return Objects.equals(this.href, shoppingCartRef.href) && Objects.equals(this.id, shoppingCartRef.id)
				&& Objects.equals(this.referredType, shoppingCartRef.referredType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(href, id, referredType);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("class ShoppingCartRef {\n");

		sb.append("    href: ").append(toIndentedString(href)).append("\n");
		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    referredType: ").append(toIndentedString(referredType)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces (except the first line).
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

