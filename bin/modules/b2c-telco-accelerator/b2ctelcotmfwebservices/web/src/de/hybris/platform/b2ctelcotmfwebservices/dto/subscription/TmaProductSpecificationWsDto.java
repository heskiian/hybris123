/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

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
 * A ProductSpecification is a detailed description of a tangible or intangible object made available externally in the
 * form of a
 * ProductOffering to customers or other parties playing a party role.
 * @since 1810
 */
@ApiModel(description = "A ProductSpecification is a detailed description of a tangible or intangible object made available externally in the form of a ProductOffering to customers or other parties playing a party role.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaProductSpecificationWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String href = null;

	private String version = null;

	private String name = null;

	private String referredType = null;

	private TmaTargetResourceSchemaWsDto describing = null;

	public TmaProductSpecificationWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the product specification
	 * @return id
	 **/
	@ApiModelProperty(required = true, value = "Unique identifier of the product specification")
	@NotNull


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaProductSpecificationWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Reference of the product specification
	 * @return href
	 **/
	@ApiModelProperty(value = "Reference of the product specification")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaProductSpecificationWsDto version(final String version)
	{
		this.version = version;
		return this;
	}

	/**
	 * Version of the product specification
	 * @return version
	 **/
	@ApiModelProperty(value = "Version of the product specification")


	public String getVersion()
	{
		return version;
	}

	public void setVersion(final String version)
	{
		this.version = version;
	}

	public TmaProductSpecificationWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Name of the product specification
	 * @return name
	 **/
	@ApiModelProperty(value = "Name of the product specification")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaProductSpecificationWsDto referredType(final String referredType)
	{
		this.referredType = referredType;
		return this;
	}

	/**
	 * Indicate the productSpecification type class
	 * @return referredType
	 **/
	@ApiModelProperty(value = "Indicate the productSpecification type class")


	public String getReferredType()
	{
		return referredType;
	}

	public void setReferredType(final String referredType)
	{
		this.referredType = referredType;
	}

	public TmaProductSpecificationWsDto describing(final TmaTargetResourceSchemaWsDto describing)
	{
		this.describing = describing;
		return this;
	}

	/**
	 * Get describing
	 * @return describing
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaTargetResourceSchemaWsDto getDescribing()
	{
		return describing;
	}

	public void setDescribing(final TmaTargetResourceSchemaWsDto describing)
	{
		this.describing = describing;
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
		final TmaProductSpecificationWsDto productSpecification = (TmaProductSpecificationWsDto) o;
		return Objects.equals(this.id, productSpecification.id) &&
				Objects.equals(this.href, productSpecification.href) &&
				Objects.equals(this.version, productSpecification.version) &&
				Objects.equals(this.name, productSpecification.name) &&
				Objects.equals(this.referredType, productSpecification.referredType) &&
				Objects.equals(this.describing, productSpecification.describing);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, version, name, referredType, describing);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("class ProductSpecification {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    href: ").append(toIndentedString(href)).append("\n");
		sb.append("    version: ").append(toIndentedString(version)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    referredType: ").append(toIndentedString(referredType)).append("\n");
		sb.append("    describing: ").append(toIndentedString(describing)).append("\n");
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

