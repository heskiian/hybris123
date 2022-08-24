/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaProductRefWsDto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Type of the product relationship. it can be &#39;bundled&#39; if the product is a bundle and you want to describe the
 * &#39;bundled&#39; products inside this bundle, &#39;reliesOn&#39; if the product needs another already owned product
 * to rely on
 * (e.g. an option on an already owned mobile access product) or &#39;targets&#39; or &#39;isTargeted&#39; (depending on
 * the way
 * of expressing the link) for any other kind of links that may be useful
 * @since 1810
 */
@ApiModel(description = "Type of the product relationship. it can be 'bundled' if the product is a bundle and you want to describe the 'bundled' products inside this bundle, 'reliesOn' if the product needs another already owned product to rely on  (e.g. an option on an already owned mobile access product) or 'targets' or 'isTargeted' (depending on the way of expressing the link) for any other kind of links that may be useful")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaProductRelationshipWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String type = null;

	private TmaProductRefWsDto product = null;

	public TmaProductRelationshipWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Type of the product relationship
	 * @return type
	 **/
	@ApiModelProperty(value = "Type of the product relationship")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaProductRelationshipWsDto product(final TmaProductRefWsDto product)
	{
		this.product = product;
		return this;
	}

	/**
	 * Get product
	 * @return product
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid

	public TmaProductRefWsDto getProduct()
	{
		return product;
	}

	public void setProduct(final TmaProductRefWsDto product)
	{
		this.product = product;
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
		final TmaProductRelationshipWsDto productRelationship = (TmaProductRelationshipWsDto) o;
		return Objects.equals(this.type, productRelationship.type) &&
				Objects.equals(this.product, productRelationship.product);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, product);
	}

	@Override
	public String toString()
	{
		return (" ProductRelationship {\n")
				+ ("    type: ") + (toIndentedString(type)) + ("\n")
				+ ("    product: ") + (toIndentedString(product)) + ("\n")
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
