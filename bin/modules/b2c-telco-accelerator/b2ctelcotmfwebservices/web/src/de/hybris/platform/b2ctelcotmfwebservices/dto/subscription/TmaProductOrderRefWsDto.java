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
 * The product order(s) related to this product inventory
 * @since 1810
 */
@ApiModel(description = "The product order(s) related to this product inventory")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaProductOrderRefWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String href = null;

	private String referredType = null;

	private String orderItemId = null;

	private String orderItemAction = null;

	public TmaProductOrderRefWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of product order
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique identifier of product order")


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaProductOrderRefWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Hypertext Reference of the product order
	 * @return href
	 **/
	@ApiModelProperty(value = "Hypertext Reference of the product order")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaProductOrderRefWsDto referredType(final String referredType)
	{
		this.referredType = referredType;
		return this;
	}

	/**
	 * Indicate the class (type) of productOrder
	 * @return referredType
	 **/
	@ApiModelProperty(value = "Indicate the class (type) of productOrder")


	public String getReferredType()
	{
		return referredType;
	}

	public void setReferredType(final String referredType)
	{
		this.referredType = referredType;
	}

	public TmaProductOrderRefWsDto orderItemId(final String orderItemId)
	{
		this.orderItemId = orderItemId;
		return this;
	}

	/**
	 * Identifier of the order item where the product was managed
	 * @return orderItemId
	 **/
	@ApiModelProperty(required = true, value = "Identifier of the order item where the product was managed")
	@NotNull


	public String getOrderItemId()
	{
		return orderItemId;
	}

	public void setOrderItemId(final String orderItemId)
	{
		this.orderItemId = orderItemId;
	}

	public TmaProductOrderRefWsDto orderItemAction(final String orderItemAction)
	{
		this.orderItemAction = orderItemAction;
		return this;
	}

	/**
	 * Action of the order item for this product
	 * @return orderItemAction
	 **/
	@ApiModelProperty(value = "Action of the order item for this product")


	public String getOrderItemAction()
	{
		return orderItemAction;
	}

	public void setOrderItemAction(final String orderItemAction)
	{
		this.orderItemAction = orderItemAction;
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
		final TmaProductOrderRefWsDto productOrderRef = (TmaProductOrderRefWsDto) o;
		return Objects.equals(this.id, productOrderRef.id) &&
				Objects.equals(this.href, productOrderRef.href) &&
				Objects.equals(this.referredType, productOrderRef.referredType) &&
				Objects.equals(this.orderItemId, productOrderRef.orderItemId) &&
				Objects.equals(this.orderItemAction, productOrderRef.orderItemAction);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, referredType, orderItemId, orderItemAction);
	}

	@Override
	public String toString()
	{
		return ("class ProductOrderRef {\n")
				+ ("    id: ") + (toIndentedString(id)) + ("\n")
				+ ("    href: ") + (toIndentedString(href)) + ("\n")
				+ ("    referredType: ") + (toIndentedString(referredType)) + ("\n")
				+ ("    orderItemId: ") + (toIndentedString(orderItemId)) + ("\n")
				+ ("    orderItemAction: ") + (toIndentedString(orderItemAction)) + ("\n");
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

