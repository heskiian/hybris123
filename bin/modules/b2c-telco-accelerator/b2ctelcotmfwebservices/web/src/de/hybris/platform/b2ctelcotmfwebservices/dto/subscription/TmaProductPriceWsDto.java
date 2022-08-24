/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * An amount, usually of money, that represents the actual price paid by a Customer for a purchase, a rent or a lease of
 * a
 * Product. The price is valid for a defined period of time.
 * @since 1810
 */
@ApiModel(description = "An amount, usually of money, that represents the actual price paid by a Customer for a purchase, a rent or a lease of a Product. The price is valid for a defined period of time.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaProductPriceWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String name = null;

	private String description = null;

	private String priceType = null;

	private String recurringChargePeriod = null;

	private String unitOfMeasure = null;

	private String type = null;

	private String schemaLocation = null;

	private TmaPriceWsDto price = null;

	private TmaPriceAlterationWsDto prodPriceAlteration = null;

	private TmaBillingAccountRefWsDto billingAccount = null;

	public TmaProductPriceWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of a productOfferingPrice
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique identifier of a productOfferingPrice")


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaProductPriceWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * A short descriptive name such as \"Subscription price\"
	 * @return name
	 **/
	@ApiModelProperty(value = "A short descriptive name such as \"Subscription price\"")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaProductPriceWsDto description(final String description)
	{
		this.description = description;
		return this;
	}

	/**
	 * A narrative that explains in detail the semantics of this product price
	 * @return description
	 **/
	@ApiModelProperty(value = "A narrative that explains in detail the semantics of this product price")


	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public TmaProductPriceWsDto priceType(final String priceType)
	{
		this.priceType = priceType;
		return this;
	}

	/**
	 * A category that describes the price, such as recurring, discount, allowance, penalty, and so forth
	 * @return priceType
	 **/
	@ApiModelProperty(value = "A category that describes the price, such as recurring, discount, allowance, penalty, and so forth")


	public String getPriceType()
	{
		return priceType;
	}

	public void setPriceType(final String priceType)
	{
		this.priceType = priceType;
	}

	public TmaProductPriceWsDto recurringChargePeriod(final String recurringChargePeriod)
	{
		this.recurringChargePeriod = recurringChargePeriod;
		return this;
	}

	/**
	 * Could be month, week...
	 * @return recurringChargePeriod
	 **/
	@ApiModelProperty(value = "Could be month, week...")


	public String getRecurringChargePeriod()
	{
		return recurringChargePeriod;
	}

	public void setRecurringChargePeriod(final String recurringChargePeriod)
	{
		this.recurringChargePeriod = recurringChargePeriod;
	}

	public TmaProductPriceWsDto unitOfMeasure(final String unitOfMeasure)
	{
		this.unitOfMeasure = unitOfMeasure;
		return this;
	}

	/**
	 * Could be minutes, GB...
	 * @return unitOfMeasure
	 **/
	@ApiModelProperty(value = "Could be minutes, GB...")


	public String getUnitOfMeasure()
	{
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(final String unitOfMeasure)
	{
		this.unitOfMeasure = unitOfMeasure;
	}

	public TmaProductPriceWsDto type(final String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * Indicates the type of ProductPrice class
	 * @return type
	 **/
	@ApiModelProperty(value = "Indicates the type of ProductPrice class")


	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public TmaProductPriceWsDto schemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
		return this;
	}

	/**
	 * A link to the schema describing this REST resource
	 * @return schemaLocation
	 **/
	@ApiModelProperty(value = "A link to the schema describing this REST resource")


	public String getSchemaLocation()
	{
		return schemaLocation;
	}

	public void setSchemaLocation(final String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
	}

	public TmaProductPriceWsDto price(final TmaPriceWsDto price)
	{
		this.price = price;
		return this;
	}

	/**
	 * Get price
	 * @return price
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaPriceWsDto getPrice()
	{
		return price;
	}

	public void setPrice(final TmaPriceWsDto price)
	{
		this.price = price;
	}

	public TmaProductPriceWsDto prodPriceAlteration(final TmaPriceAlterationWsDto prodPriceAlteration)
	{
		this.prodPriceAlteration = prodPriceAlteration;
		return this;
	}

	/**
	 * Get prodPriceAlteration
	 * @return prodPriceAlteration
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaPriceAlterationWsDto getProdPriceAlteration()
	{
		return prodPriceAlteration;
	}

	public void setProdPriceAlteration(final TmaPriceAlterationWsDto prodPriceAlteration)
	{
		this.prodPriceAlteration = prodPriceAlteration;
	}

	public TmaProductPriceWsDto billingAccount(final TmaBillingAccountRefWsDto billingAccount)
	{
		this.billingAccount = billingAccount;
		return this;
	}

	/**
	 * Get billingAccount
	 * @return billingAccount
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaBillingAccountRefWsDto getBillingAccount()
	{
		return billingAccount;
	}

	public void setBillingAccount(final TmaBillingAccountRefWsDto billingAccount)
	{
		this.billingAccount = billingAccount;
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
		final TmaProductPriceWsDto productPrice = (TmaProductPriceWsDto) o;
		return Objects.equals(this.id, productPrice.id) &&
				Objects.equals(this.name, productPrice.name) &&
				Objects.equals(this.description, productPrice.description) &&
				Objects.equals(this.priceType, productPrice.priceType) &&
				Objects.equals(this.recurringChargePeriod, productPrice.recurringChargePeriod) &&
				Objects.equals(this.unitOfMeasure, productPrice.unitOfMeasure) &&
				Objects.equals(this.type, productPrice.type) &&
				Objects.equals(this.schemaLocation, productPrice.schemaLocation) &&
				Objects.equals(this.price, productPrice.price) &&
				Objects.equals(this.prodPriceAlteration, productPrice.prodPriceAlteration) &&
				Objects.equals(this.billingAccount, productPrice.billingAccount);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name, description, priceType, recurringChargePeriod, unitOfMeasure, type, schemaLocation, price,
				prodPriceAlteration, billingAccount);
	}

	@Override
	public String toString()
	{
		return " ProductPrice {\n"
				+ ("    id: ") + (toIndentedString(id)) + ("\n")
				+ ("    name: ") + (toIndentedString(name)) + ("\n")
				+ ("    description: ") + (toIndentedString(description)) + ("\n")
				+ ("    priceType: ") + (toIndentedString(priceType)) + ("\n")
				+ ("    recurringChargePeriod: ") + (toIndentedString(recurringChargePeriod)) + ("\n")
				+ ("    unitOfMeasure: ") + (toIndentedString(unitOfMeasure)) + ("\n")
				+ ("    type: ") + (toIndentedString(type)) + ("\n")
				+ ("    schemaLocation: ") + (toIndentedString(schemaLocation)) + ("\n")
				+ ("    price: ") + (toIndentedString(price)) + ("\n")
				+ ("    prodPriceAlteration: ") + (toIndentedString(prodPriceAlteration)) + ("\n")
				+ ("    billingAccount: ") + (toIndentedString(billingAccount)) + ("\n")
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
