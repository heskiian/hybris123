/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage;

import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaProductRefWsDto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * A bucket represents a quantity of usage, as 2 hours national calls or 50 sms for example. It could be either a
 * quantity or an amount in a currency (i.e. It could represent a fixed number of SMS, MMS, minutes of calls, quantity
 * of data, number of events as well as a specific amount in a given currency). It requires one or more network products
 * from which usages will debit the bucket.
 * @since 1810
 */
@ApiModel(description = "A bucket represents a quantity of usage, as 2 hours national calls or 50 sms for example. It could be either a quantity or an amount in a currency (i.e. It could represent a fixed number of SMS, MMS, minutes of calls, quantity of data, number of events as well as a specific amount in a given currency). It requires one or more network products from which usages will debit the bucket.")
@Validated

@JsonInclude(Include.NON_NULL)
public class TmaBucketWsDto implements Serializable
{
	private static final long serialVersionUID = 8449967402779341L;

	private String id = null;

	private String name = null;

	private String usageType = null;

	private Boolean isShared = null;

	private String type = null;

	private String schemaLocation = null;

	private TmaProductRefWsDto product = null;

	@Valid
	private List<TmaBalanceWsDto> bucketBalance = null;

	@Valid
	private List<TmaConsumptionCounterWsDto> bucketCounter = null;

	public TmaBucketWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the bucket
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique identifier of the bucket")


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaBucketWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * Bucket name
	 * @return name
	 **/
	@ApiModelProperty(value = "Bucket name")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaBucketWsDto usageType(final String usageType)
	{
		this.usageType = usageType;
		return this;
	}

	/**
	 * Type of usage concerned by the bucket (voice, sms, data,…)
	 * @return usageType
	 **/
	@ApiModelProperty(value = "Type of usage concerned by the bucket (voice, sms, data,…)")


	public String getUsageType()
	{
		return usageType;
	}

	public void setUsageType(final String usageType)
	{
		this.usageType = usageType;
	}

	public TmaBucketWsDto isShared(final Boolean isShared)
	{
		this.isShared = isShared;
		return this;
	}

	/**
	 * True if the bucket is shared between several devices or users
	 * @return isShared
	 **/
	@ApiModelProperty(value = "True if the bucket is shared between several devices or users")


	public Boolean isIsShared()
	{
		return isShared;
	}

	public void setIsShared(final Boolean isShared)
	{
		this.isShared = isShared;
	}

	public TmaBucketWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicates the (class) type of bucket
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicates the (class) type of bucket")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaBucketWsDto schemaLocation(final String schemaLocation)
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

	public TmaBucketWsDto product(final TmaProductRefWsDto product)
	{
		this.product = product;
		return this;
	}

	/**
	 * Get product
	 * @return product
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaProductRefWsDto getProduct()
	{
		return product;
	}

	public void setProduct(final TmaProductRefWsDto product)
	{
		this.product = product;
	}

	public TmaBucketWsDto bucketBalance(final List<TmaBalanceWsDto> bucketBalance)
	{
		this.bucketBalance = bucketBalance;
		return this;
	}

	public TmaBucketWsDto addBucketBalanceItem(final TmaBalanceWsDto bucketBalanceItem)
	{
		if (this.bucketBalance == null)
		{
			this.bucketBalance = new java.util.ArrayList<>();
		}
		this.bucketBalance.add(bucketBalanceItem);
		return this;
	}

	/**
	 * Get bucketBalance
	 * @return bucketBalance
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaBalanceWsDto> getBucketBalance()
	{
		return bucketBalance;
	}

	public void setBucketBalance(final List<TmaBalanceWsDto> bucketBalance)
	{
		this.bucketBalance = bucketBalance;
	}

	public TmaBucketWsDto bucketCounter(final List<TmaConsumptionCounterWsDto> bucketCounter)
	{
		this.bucketCounter = bucketCounter;
		return this;
	}

	public TmaBucketWsDto addBucketCounterItem(final TmaConsumptionCounterWsDto bucketCounterItem)
	{
		if (this.bucketCounter == null)
		{
			this.bucketCounter = new java.util.ArrayList<>();
		}
		this.bucketCounter.add(bucketCounterItem);
		return this;
	}

	/**
	 * Get bucketCounter
	 * @return bucketCounter
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaConsumptionCounterWsDto> getBucketCounter()
	{
		return bucketCounter;
	}

	public void setBucketCounter(final List<TmaConsumptionCounterWsDto> bucketCounter)
	{
		this.bucketCounter = bucketCounter;
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
		final TmaBucketWsDto bucket = (TmaBucketWsDto) o;
		return Objects.equals(this.id, bucket.id) &&
				Objects.equals(this.name, bucket.name) &&
				Objects.equals(this.usageType, bucket.usageType) &&
				Objects.equals(this.isShared, bucket.isShared) &&
				Objects.equals(this.type, bucket.type) &&
				Objects.equals(this.schemaLocation, bucket.schemaLocation) &&
				Objects.equals(this.product, bucket.product) &&
				Objects.equals(this.bucketBalance, bucket.bucketBalance) &&
				Objects.equals(this.bucketCounter, bucket.bucketCounter);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name, usageType, isShared, type, schemaLocation, product, bucketBalance, bucketCounter);
	}

	@Override
	public String toString()
	{
		return " Bucket {\n"
				+ ("    id: ") + (toIndentedString(id)) + ("\n")
				+ ("    name: ") + (toIndentedString(name)) + ("\n")
				+ ("    usageType: ") + (toIndentedString(usageType)) + ("\n")
				+ ("    isShared: ") + (toIndentedString(isShared)) + ("\n")
				+ ("    type: ") + (toIndentedString(type)) + ("\n")
				+ ("    schemaLocation: ") + (toIndentedString(schemaLocation)) + ("\n")
				+ ("    product: ") + (toIndentedString(product)) + ("\n")
				+ ("    bucketBalance: ") + (toIndentedString(bucketBalance)) + ("\n")
				+ ("    bucketCounter: ") + (toIndentedString(bucketCounter)) + ("\n")
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
