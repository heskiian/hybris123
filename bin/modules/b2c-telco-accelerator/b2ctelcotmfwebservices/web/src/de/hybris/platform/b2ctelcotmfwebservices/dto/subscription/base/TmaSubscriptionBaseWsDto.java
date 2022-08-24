/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.base;

import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * A Subscription base represents the subscription base for subscription for a Customer.
 * @since 1810.
 */
@ApiModel(description = "A Subscription base represents the subscription base for subscription fo a Customer.  ")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaSubscriptionBaseWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String href = null;

	private String name = null;

	private String description = null;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")

	private Date effectiveDate = null;

	private String baseType = null;

	private String type = null;

	private String schemaLocation = null;

	private TmaRelatedPartyWsDto user = null;

	private TmaSubscriptionBaseDetailRefWsDto subscriptionBase = null;

	public TmaSubscriptionBaseWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the response
	 * @return id
	 **/
	@ApiModelProperty(required = true, value = "Unique identifier of the response")
	@NotNull


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaSubscriptionBaseWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Reference of the API
	 * @return href
	 **/
	@ApiModelProperty(value = "Reference of the API")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaSubscriptionBaseWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Name of the API
	 * @return name
	 **/
	@ApiModelProperty(value = "Name of the API")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaSubscriptionBaseWsDto description(final String description)
	{
		this.description = description;
		return this;
	}

	/**
	 * The description of the API
	 * @return description
	 **/
	@ApiModelProperty(value = "The description of the API")


	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public TmaSubscriptionBaseWsDto effectiveDate(final Date effectiveDate)
	{
		this.effectiveDate = effectiveDate;
		return this;
	}

	/**
	 * Current date
	 * @return effectiveDate
	 **/
	@ApiModelProperty(value = "Current date ")

	@Valid

	public Date getEffectiveDate()
	{
		return effectiveDate;
	}

	public void setEffectiveDate(final Date effectiveDate)
	{
		this.effectiveDate = effectiveDate;
	}

	public TmaSubscriptionBaseWsDto baseType(final String baseType)
	{
		this.baseType = baseType;
		return this;
	}

	/**
	 * Indicates the base type of the resource. Here can be 'Product'
	 * @return baseType
	 **/
	@ApiModelProperty(value = "Indicates the base type of the resource. Here can be 'Product'")


	public String getBaseType()
	{
		return baseType;
	}

	public void setBaseType(final String baseType)
	{
		this.baseType = baseType;
	}

	public TmaSubscriptionBaseWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicated the type of resource.
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicated the type of resource.")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaSubscriptionBaseWsDto schemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
		return this;
	}

	/**
	 * A link to the schema describing this REST resource.
	 * @return schemaLocation
	 **/
	@ApiModelProperty(value = "A link to the schema describing this REST resource.")


	public String getSchemaLocation()
	{
		return schemaLocation;
	}

	public void setSchemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
	}

	public TmaSubscriptionBaseWsDto user(final TmaRelatedPartyWsDto user)
	{
		this.user = user;
		return this;
	}

	/**
	 * Get user
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

	public TmaSubscriptionBaseWsDto subscriptionBase(final TmaSubscriptionBaseDetailRefWsDto subscriptionBase)
	{
		this.subscriptionBase = subscriptionBase;
		return this;
	}

	/**
	 * Get subscriptionBase
	 * @return subscriptionBase
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaSubscriptionBaseDetailRefWsDto getSubscriptionBase()
	{
		return subscriptionBase;
	}

	public void setSubscriptionBase(final TmaSubscriptionBaseDetailRefWsDto subscriptionBase)
	{
		this.subscriptionBase = subscriptionBase;
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
		final TmaSubscriptionBaseWsDto subscriptionBase = (TmaSubscriptionBaseWsDto) o;
		return Objects.equals(this.id, subscriptionBase.id) &&
				Objects.equals(this.href, subscriptionBase.href) &&
				Objects.equals(this.name, subscriptionBase.name) &&
				Objects.equals(this.description, subscriptionBase.description) &&
				Objects.equals(this.effectiveDate, subscriptionBase.effectiveDate) &&
				Objects.equals(this.baseType, subscriptionBase.baseType) &&
				Objects.equals(this.type, subscriptionBase.type) &&
				Objects.equals(this.schemaLocation, subscriptionBase.schemaLocation) &&
				Objects.equals(this.user, subscriptionBase.user) &&
				Objects.equals(this.subscriptionBase, subscriptionBase.subscriptionBase);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, name, description, effectiveDate, baseType, type, schemaLocation, user, subscriptionBase);
	}

	@Override
	public String toString()
	{
		return " SubscriptionBase {\n"

				+ ("    id: ") + (toIndentedString(id)) + ("\n")
				+ ("    href: ") + (toIndentedString(href)) + ("\n")
				+ ("    name: ") + (toIndentedString(name)) + ("\n")
				+ ("    description: ") + (toIndentedString(description)) + ("\n")
				+ ("    effectiveDate: ") + (toIndentedString(effectiveDate)) + ("\n")
				+ ("    baseType: ") + (toIndentedString(baseType)) + ("\n")
				+ ("    type: ") + (toIndentedString(type)) + ("\n")
				+ ("    schemaLocation: ") + (toIndentedString(schemaLocation)) + ("\n")
				+ ("    user: ") + (toIndentedString(user)) + ("\n")
				+ ("    subscriptionBase: ") + (toIndentedString(subscriptionBase)) + ("\n")
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

