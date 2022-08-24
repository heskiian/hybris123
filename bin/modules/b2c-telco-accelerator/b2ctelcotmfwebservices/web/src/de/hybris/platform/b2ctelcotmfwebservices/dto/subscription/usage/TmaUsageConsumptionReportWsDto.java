/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage;

import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * An usage consumption report enables to know at a given point the balances and the consumption counters related to
 * various
 * buckets (SMS, Voice, Data for example). It could be calculated for a device identified by a public key (msisdn number
 * for a
 * mobile device for example or PSTN or VOIP number for a fix device), for a subscribed offer or option or for an user.
 * @since 1810
 */
@ApiModel(description = "An usage consumption report enables to know at a given point the balances and the consumption counters related to various buckets (SMS, Voice, Data for example). It could be calculated for a device identified by a public key (msisdn number for a mobile device for example or PSTN or VOIP number for a fix device), for a subscribed offer or option or for an user.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaUsageConsumptionReportWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String href = null;

	private String name = null;

	private String description = null;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date effectiveDate = null;

	private String baseType = null;

	private String type = null;

	private String schemaLocation = null;

	private TmaRelatedPartyWsDto relatedParty = null;

	@Valid
	private List<TmaBucketWsDto> bucket = null;

	public TmaUsageConsumptionReportWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the usage consumption report given by the server
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique identifier of the usage consumption report given by the server")


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaUsageConsumptionReportWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Hyperlink to access the usage consumption report
	 * @return href
	 **/
	@ApiModelProperty(value = "Hyperlink to access the usage consumption report")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaUsageConsumptionReportWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Usage consumption report name
	 * @return name
	 **/
	@ApiModelProperty(value = "Usage consumption report name")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaUsageConsumptionReportWsDto description(final String description)
	{
		this.description = description;
		return this;
	}

	/**
	 * Free short text describing the usage consumption report content
	 * @return description
	 **/
	@ApiModelProperty(value = "Free short text describing the usage consumption report content")


	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public TmaUsageConsumptionReportWsDto effectiveDate(final Date effectiveDate)
	{
		this.effectiveDate = effectiveDate;
		return this;
	}

	/**
	 * Date and time when the usage consumption report was calculated and generated
	 * @return effectiveDate
	 **/
	@ApiModelProperty(value = "Date and time when the usage consumption report was calculated and generated")
	@Valid
	public Date getEffectiveDate()
	{
		return effectiveDate;
	}

	public void setEffectiveDate(final Date effectiveDate)
	{
		this.effectiveDate = effectiveDate;
	}

	public TmaUsageConsumptionReportWsDto baseType(final String baseType)
	{
		this.baseType = baseType;
		return this;
	}

	/**
	 * Indicates the base (class) type of the usage consumption report
	 * @return baseType
	 **/
	@ApiModelProperty(value = "Indicates the base (class) type of the usage consumption report")


	public String getBaseType()
	{
		return baseType;
	}

	public void setBaseType(final String baseType)
	{
		this.baseType = baseType;
	}

	public TmaUsageConsumptionReportWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicates the (class) type of the usage consumption report
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicates the (class) type of the usage consumption report")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaUsageConsumptionReportWsDto schemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
		return this;
	}

	/**
	 * Link to the schema describing the REST resource
	 * @return schemaLocation
	 **/
	@ApiModelProperty(value = "Link to the schema describing the REST resource")


	public String getSchemaLocation()
	{
		return schemaLocation;
	}

	public void setSchemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
	}

	public TmaUsageConsumptionReportWsDto relatedParty(final TmaRelatedPartyWsDto relatedParty)
	{
		this.relatedParty = relatedParty;
		return this;
	}

	/**
	 * Get relatedParty
	 * @return relatedParty
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaRelatedPartyWsDto getRelatedParty()
	{
		return relatedParty;
	}

	public void setRelatedParty(final TmaRelatedPartyWsDto relatedParty)
	{
		this.relatedParty = relatedParty;
	}

	public TmaUsageConsumptionReportWsDto bucket(final java.util.List<TmaBucketWsDto> bucket)
	{
		this.bucket = bucket;
		return this;
	}

	public TmaUsageConsumptionReportWsDto addBucketItem(final TmaBucketWsDto bucketItem)
	{
		if (this.bucket == null)
		{
			this.bucket = new java.util.ArrayList<>();
		}
		this.bucket.add(bucketItem);
		return this;
	}

	/**
	 * Get bucket
	 * @return bucket
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public java.util.List<TmaBucketWsDto> getBucket()
	{
		return bucket;
	}

	public void setBucket(final java.util.List<TmaBucketWsDto> bucket)
	{
		this.bucket = bucket;
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
		final TmaUsageConsumptionReportWsDto usageConsumptionReport = (TmaUsageConsumptionReportWsDto) o;
		return Objects.equals(this.id, usageConsumptionReport.id) &&
				Objects.equals(this.href, usageConsumptionReport.href) &&
				Objects.equals(this.name, usageConsumptionReport.name) &&
				Objects.equals(this.description, usageConsumptionReport.description) &&
				Objects.equals(this.effectiveDate, usageConsumptionReport.effectiveDate) &&
				Objects.equals(this.baseType, usageConsumptionReport.baseType) &&
				Objects.equals(this.type, usageConsumptionReport.type) &&
				Objects.equals(this.schemaLocation, usageConsumptionReport.schemaLocation) &&
				Objects.equals(this.relatedParty, usageConsumptionReport.relatedParty) &&
				Objects.equals(this.bucket, usageConsumptionReport.bucket);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, name, description, effectiveDate, baseType, type, schemaLocation, relatedParty, bucket);
	}

	@Override
	public String toString()
	{
		return "class UsageConsumptionReport {\n"
				+ ("    id: ") + (toIndentedString(id)) + ("\n")
				+ ("    href: ") + (toIndentedString(href)) + ("\n")
				+ ("    name: ") + (toIndentedString(name)) + ("\n")
				+ ("    description: ") + (toIndentedString(description)) + ("\n")
				+ ("    effectiveDate: ") + (toIndentedString(effectiveDate)) + ("\n")
				+ ("    baseType: ") + (toIndentedString(baseType)) + ("\n")
				+ ("    type: ") + (toIndentedString(type)) + ("\n")
				+ ("    schemaLocation: ") + (toIndentedString(schemaLocation)) + ("\n")
				+ ("    relatedParty: ") + (toIndentedString(relatedParty)) + ("\n")
				+ ("    bucket: ") + (toIndentedString(bucket)) + ("\n")
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
