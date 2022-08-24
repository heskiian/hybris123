/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.base;

import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaProductRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * A subscriptionBaseDetail is a detailed description of Subscription base.
 * @since 1810
 */
@ApiModel(description = "A subscriptionBaseDetail is a detailed description of Subscription base.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaSubscriptionBaseDetailRefWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String accessType = null;

	@Valid
	private List<TmaProductRefWsDto> product = null;

	@Valid
	private List<TmaRelatedPartyWsDto> relatedPartyRef = null;

	public TmaSubscriptionBaseDetailRefWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the Subscription base
	 * @return id
	 **/
	@ApiModelProperty(required = true, value = "Unique identifier of the Subscription base")
	@NotNull


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaSubscriptionBaseDetailRefWsDto accessType(final String accessType)
	{
		this.accessType = accessType;
		return this;
	}

	/**
	 * Reference of the Customer Access to the Subscription
	 * @return accessType
	 **/
	@ApiModelProperty(value = "Reference of the Customer Access to the Subscription")


	public String getAccessType()
	{
		return accessType;
	}

	public void setAccessType(final String accessType)
	{
		this.accessType = accessType;
	}

	public TmaSubscriptionBaseDetailRefWsDto product(final java.util.List<TmaProductRefWsDto> product)
	{
		this.product = product;
		return this;
	}

	public TmaSubscriptionBaseDetailRefWsDto addProductItem(final TmaProductRefWsDto productItem)
	{
		if (this.product == null)
		{
			this.product = new java.util.ArrayList<>();
		}
		this.product.add(productItem);
		return this;
	}

	/**
	 * Get product
	 * @return product
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public java.util.List<TmaProductRefWsDto> getProduct()
	{
		return product;
	}

	public void setProduct(final List<TmaProductRefWsDto> product)
	{
		this.product = product;
	}

	public TmaSubscriptionBaseDetailRefWsDto relatedPartyRef(final List<TmaRelatedPartyWsDto> relatedPartyRef)
	{
		this.relatedPartyRef = relatedPartyRef;
		return this;
	}

	public TmaSubscriptionBaseDetailRefWsDto addRelatedPartyRefItem(final TmaRelatedPartyWsDto relatedPartyRefItem)
	{
		if (this.relatedPartyRef == null)
		{
			this.relatedPartyRef = new java.util.ArrayList<>();
		}
		this.relatedPartyRef.add(relatedPartyRefItem);
		return this;
	}

	/**
	 * Get relatedPartyRef
	 * @return relatedPartyRef
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaRelatedPartyWsDto> getRelatedPartyRef()
	{
		return relatedPartyRef;
	}

	public void setRelatedPartyRef(final List<TmaRelatedPartyWsDto> relatedPartyRef)
	{
		this.relatedPartyRef = relatedPartyRef;
	}


	@Override
	public int hashCode()
	{
		return Objects.hash(id, product, relatedPartyRef);
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
		final TmaSubscriptionBaseDetailRefWsDto subscriptionBaseDetailRef = (TmaSubscriptionBaseDetailRefWsDto) o;
		return Objects.equals(this.id, subscriptionBaseDetailRef.id) &&
				Objects.equals(this.accessType, subscriptionBaseDetailRef.accessType) &&
				Objects.equals(this.product, subscriptionBaseDetailRef.product) &&
				Objects.equals(this.relatedPartyRef, subscriptionBaseDetailRef.relatedPartyRef);
	}


	@Override
	public String toString()
	{
		return " SubscriptionBaseDetailRef {\n"

				+ ("    id: ") + (toIndentedString(id)) + ("\n")
				+ ("    product: ") + (toIndentedString(product)) + ("\n")
				+ ("    relatedPartyRef: ") + (toIndentedString(relatedPartyRef)) + ("\n")
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
