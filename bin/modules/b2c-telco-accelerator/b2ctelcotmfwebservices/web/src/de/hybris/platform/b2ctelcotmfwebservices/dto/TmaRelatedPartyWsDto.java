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
 * The party which the product is related with.
 *
 * @since 1810
 */
@ApiModel(description = "The party which the product is related with.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaRelatedPartyWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;


	private String id = null;


	private String href = null;


	private String name = null;

	private String role = null;

	private TmaTimePeriodWsDto validFor = null;

	private String referredType = null;

	public TmaRelatedPartyWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the related party
	 *
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique identifier of the related party")


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaRelatedPartyWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Reference of the related party
	 *
	 * @return href
	 **/
	@ApiModelProperty(value = "Reference of the related party")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaRelatedPartyWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Name of the related party
	 *
	 * @return name
	 **/
	@ApiModelProperty(value = "Name of the related party")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaRelatedPartyWsDto role(final String role)
	{
		this.role = role;
		return this;
	}

	/**
	 * Role played by the related party
	 *
	 * @return role
	 **/
	@ApiModelProperty(value = "Role played by the related party")


	public String getRole()
	{
		return role;
	}

	public void setRole(final String role)
	{
		this.role = role;
	}

	public TmaRelatedPartyWsDto validFor(final TmaTimePeriodWsDto validFor)
	{
		this.validFor = validFor;
		return this;
	}

	/**
	 * Get validFor
	 *
	 * @return validFor
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaTimePeriodWsDto getValidFor()
	{
		return validFor;
	}

	public void setValidFor(final TmaTimePeriodWsDto validFor)
	{
		this.validFor = validFor;
	}

	public TmaRelatedPartyWsDto referredType(final String referredType)
	{
		this.referredType = referredType;
		return this;
	}

	/**
	 * Indicates the (class) type of party
	 *
	 * @return referredType
	 **/
	@ApiModelProperty(value = "Indicates the (class) type of party")


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
		final TmaRelatedPartyWsDto relatedParty = (TmaRelatedPartyWsDto) o;
		return Objects.equals(this.id, relatedParty.id) &&
				Objects.equals(this.href, relatedParty.href) &&
				Objects.equals(this.name, relatedParty.name) &&
				Objects.equals(this.role, relatedParty.role) &&
				Objects.equals(this.validFor, relatedParty.validFor) &&
				Objects.equals(this.referredType, relatedParty.referredType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, name, role, validFor, referredType);
	}

	@Override
	public String toString()
	{
		return " RelatedParty {\n"
				+ ("    id: ") + (toIndentedString(id)) + ("\n")
				+ ("    href: ") + (toIndentedString(href)) + ("\n")
				+ ("    name: ") + (toIndentedString(name)) + ("\n")
				+ ("    role: ") + (toIndentedString(role)) + ("\n")
				+ ("    validFor: ") + (toIndentedString(validFor)) + ("\n")
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
