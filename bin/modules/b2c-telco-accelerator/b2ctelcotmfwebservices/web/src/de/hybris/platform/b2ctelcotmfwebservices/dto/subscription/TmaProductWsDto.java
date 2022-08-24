/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
 * A Product represents the subscription of a ProductOffering by a Party playing a PartyRole, such as a Customer. For
 * example,
 * Jean has subscribed to company ABC’s internet ProductOffering. The association between ProductSpecification and
 * Product allows
 * ProductSpecification, to be instantiated as Product and related to customers or other involved parties.
 * @since 1810
 */
@ApiModel(description = "A Product represents the subscription of a ProductOffering by a Party playing a PartyRole, such as a Customer.  For example, Jean has subscribed to company ABC’s internet ProductOffering. The association between ProductSpecification and Product allows ProductSpecification, to be instantiated as Product and related to customers or other involved parties.")
@Validated
@JsonInclude(Include.NON_NULL)
public class TmaProductWsDto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id = null;

	private String href = null;

	private String description = null;

	private Boolean isBundle = null;

	private Boolean isCustomerVisible = null;

	private String name = null;

	private String productSerialNumber = null;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date startDate = null;

	private TmaStatusTypeWsDto status = null;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date terminationDate = null;

	private String baseType = null;

	private String type = null;

	private String schemaLocation = null;

	@Valid
	private List<TmaPlaceWsDto> place = null;

	private TmaProductOfferingRefWsDto productOffering = null;

	private TmaProductSpecificationWsDto productSpecification = null;

	@Valid
	private List<TmaProductCharacteristicWsDto> characteristic = null;

	@Valid
	private List<TmaProductRelationshipWsDto> productRelationship = null;

	@Valid
	private List<TmaBillingAccountRefWsDto> billingAccount = null;

	@Valid
	private List<TmaRelatedPartyWsDto> relatedParty = null;

	@Valid
	private List<TmaRealizingResourceWsDto> realizingResource = null;

	@Valid
	private List<TmaRealizingServiceWsDto> realizingService = null;

	@Valid
	private List<TmaProductPriceWsDto> productPrice = null;

	@Valid
	private List<TmaProductOrderRefWsDto> productOrder = null;

	@Valid
	private List<TmaProductTermWsDto> productTerm = null;

	private String publicIdentifier = null;

	private TmaRelatedPartyWsDto user = null;


	public TmaProductWsDto id(final String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the product
	 * @return id
	 **/
	@ApiModelProperty(required = true, value = "Unique identifier of the product")
	@NotNull


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public TmaProductWsDto href(final String href)
	{
		this.href = href;
		return this;
	}

	/**
	 * Reference of the product
	 * @return href
	 **/
	@ApiModelProperty(value = "Reference of the product")


	public String getHref()
	{
		return href;
	}

	public void setHref(final String href)
	{
		this.href = href;
	}

	public TmaProductWsDto description(final String description)
	{
		this.description = description;
		return this;
	}

	/**
	 * The description of the product. It could be copied from the description of the Product Offering.
	 * @return description
	 **/
	@ApiModelProperty(value = "The description of the product. It could be copied from the description of the Product Offering.")


	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public TmaProductWsDto isBundle(final Boolean isBundle)
	{
		this.isBundle = isBundle;
		return this;
	}

	/**
	 * If true, the product is a ProductBundle which is an instantiation of a BundledProductOffering. If false, the
	 * product is a
	 * ProductComponent which is an instantiation of a SimpleProductOffering
	 * @return isBundle
	 **/
	@ApiModelProperty(value = "If true, the product is a ProductBundle which is an instantiation of a BundledProductOffering. If false, the product is a ProductComponent which is an instantiation of a SimpleProductOffering")


	public Boolean isIsBundle()
	{
		return isBundle;
	}

	public void setIsBundle(final Boolean isBundle)
	{
		this.isBundle = isBundle;
	}

	public TmaProductWsDto isCustomerVisible(final Boolean isCustomerVisible)
	{
		this.isCustomerVisible = isCustomerVisible;
		return this;
	}

	/**
	 * If true, the product is visible by the customer
	 * @return isCustomerVisible
	 **/
	@ApiModelProperty(value = "If true, the product is visible by the customer")


	public Boolean isIsCustomerVisible()
	{
		return isCustomerVisible;
	}

	public void setIsCustomerVisible(final Boolean isCustomerVisible)
	{
		this.isCustomerVisible = isCustomerVisible;
	}

	public TmaProductWsDto name(final String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * The name of the product. It could be the same as the name of the Product Offering
	 * @return name
	 **/
	@ApiModelProperty(value = "The name of the product. It could be the same as the name of the Product Offering")


	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public TmaProductWsDto productSerialNumber(final String productSerialNumber)
	{
		this.productSerialNumber = productSerialNumber;
		return this;
	}

	/**
	 * Serial number for the product. This is typically applicable to tangible products e.g. Broadband Router.
	 * @return productSerialNumber
	 **/
	@ApiModelProperty(value = "Serial number for the product. This is typically applicable to tangible products e.g. Broadband Router.")


	public String getProductSerialNumber()
	{
		return productSerialNumber;
	}

	public void setProductSerialNumber(final String productSerialNumber)
	{
		this.productSerialNumber = productSerialNumber;
	}

	public TmaProductWsDto startDate(final java.util.Date startDate)
	{
		this.startDate = startDate;
		return this;
	}

	public TmaProductWsDto publicIdentifier(final String publicIdentifier)
	{
		this.publicIdentifier = publicIdentifier;
		return this;
	}

	/**
	 * Public number associated to the product (msisdn number for mobile line for example)
	 * @return publicIdentifier
	 **/
	@ApiModelProperty(value = "Public number associated to the product (msisdn number for mobile line for example)")


	public String getPublicIdentifier()
	{
		return publicIdentifier;
	}

	public void setPublicIdentifier(final String publicIdentifier)
	{
		this.publicIdentifier = publicIdentifier;
	}

	public TmaProductWsDto user(final TmaRelatedPartyWsDto user)
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

	/**
	 * The date from which the product starts
	 * @return startDate
	 **/
	@ApiModelProperty(value = "The date from which the product starts")

	@Valid

	public java.util.Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(final java.util.Date startDate)
	{
		this.startDate = startDate;
	}

	public TmaProductWsDto status(final TmaStatusTypeWsDto status)
	{
		this.status = status;
		return this;
	}

	/**
	 * Get status
	 * @return status
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaStatusTypeWsDto getStatus()
	{
		return status;
	}

	public void setStatus(final TmaStatusTypeWsDto status)
	{
		this.status = status;
	}

	public TmaProductWsDto terminationDate(final java.util.Date terminationDate)
	{
		this.terminationDate = terminationDate;
		return this;
	}

	/**
	 * The date when the product was terminated. Not applicable to active products
	 * @return terminationDate
	 **/
	@ApiModelProperty(value = "The date when the product was terminated. Not applicable to active products")

	@Valid

	public java.util.Date getTerminationDate()
	{
		return terminationDate;
	}

	public void setTerminationDate(final java.util.Date terminationDate)
	{
		this.terminationDate = terminationDate;
	}

	public TmaProductWsDto baseType(final String baseType)
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

	public TmaProductWsDto type(final String type)
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

	public TmaProductWsDto schemaLocation(final String schemaLocation)
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

	public TmaProductWsDto place(final List<TmaPlaceWsDto> place)
	{
		this.place = place;
		return this;
	}

	public TmaProductWsDto addPlaceItem(final TmaPlaceWsDto placeItem)
	{
		if (this.place == null)
		{
			this.place = new java.util.ArrayList<>();
		}
		this.place.add(placeItem);
		return this;
	}

	/**
	 * Get place
	 * @return place
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaPlaceWsDto> getPlace()
	{
		return place;
	}

	public void setPlace(final List<TmaPlaceWsDto> place)
	{
		this.place = place;
	}

	public TmaProductWsDto productOffering(final TmaProductOfferingRefWsDto productOffering)
	{
		this.productOffering = productOffering;
		return this;
	}

	/**
	 * Get productOffering
	 * @return productOffering
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaProductOfferingRefWsDto getProductOffering()
	{
		return productOffering;
	}

	public void setProductOffering(final TmaProductOfferingRefWsDto productOffering)
	{
		this.productOffering = productOffering;
	}

	public TmaProductWsDto productSpecification(final TmaProductSpecificationWsDto productSpecification)
	{
		this.productSpecification = productSpecification;
		return this;
	}

	/**
	 * Get productSpecification
	 * @return productSpecification
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public TmaProductSpecificationWsDto getProductSpecification()
	{
		return productSpecification;
	}

	public void setProductSpecification(final TmaProductSpecificationWsDto productSpecification)
	{
		this.productSpecification = productSpecification;
	}

	public TmaProductWsDto characteristic(final List<TmaProductCharacteristicWsDto> characteristic)
	{
		this.characteristic = characteristic;
		return this;
	}

	public TmaProductWsDto addCharacteristicItem(final TmaProductCharacteristicWsDto characteristicItem)
	{
		if (this.characteristic == null)
		{
			this.characteristic = new java.util.ArrayList<>();
		}
		this.characteristic.add(characteristicItem);
		return this;
	}

	/**
	 * Get characteristic
	 * @return characteristic
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaProductCharacteristicWsDto> getCharacteristic()
	{
		return characteristic;
	}

	public void setCharacteristic(final List<TmaProductCharacteristicWsDto> characteristic)
	{
		this.characteristic = characteristic;
	}

	public TmaProductWsDto productRelationship(final List<TmaProductRelationshipWsDto> productRelationship)
	{
		this.productRelationship = productRelationship;
		return this;
	}

	public TmaProductWsDto addProductRelationshipItem(final TmaProductRelationshipWsDto productRelationshipItem)
	{
		if (this.productRelationship == null)
		{
			this.productRelationship = new java.util.ArrayList<>();
		}
		this.productRelationship.add(productRelationshipItem);
		return this;
	}

	/**
	 * Get productRelationship
	 * @return productRelationship
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaProductRelationshipWsDto> getProductRelationship()
	{
		return productRelationship;
	}

	public void setProductRelationship(final List<TmaProductRelationshipWsDto> productRelationship)
	{
		this.productRelationship = productRelationship;
	}

	public TmaProductWsDto billingAccount(final List<TmaBillingAccountRefWsDto> billingAccount)
	{
		this.billingAccount = billingAccount;
		return this;
	}

	public TmaProductWsDto addBillingAccountItem(final TmaBillingAccountRefWsDto billingAccountItem)
	{
		if (this.billingAccount == null)
		{
			this.billingAccount = new java.util.ArrayList<>();
		}
		this.billingAccount.add(billingAccountItem);
		return this;
	}

	/**
	 * Get billingAccount
	 * @return billingAccount
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaBillingAccountRefWsDto> getBillingAccount()
	{
		return billingAccount;
	}

	public void setBillingAccount(final List<TmaBillingAccountRefWsDto> billingAccount)
	{
		this.billingAccount = billingAccount;
	}

	public TmaProductWsDto relatedParty(final List<TmaRelatedPartyWsDto> relatedParty)
	{
		this.relatedParty = relatedParty;
		return this;
	}

	public TmaProductWsDto addRelatedPartyItem(final TmaRelatedPartyWsDto relatedPartyItem)
	{
		if (this.relatedParty == null)
		{
			this.relatedParty = new java.util.ArrayList<>();
		}
		this.relatedParty.add(relatedPartyItem);
		return this;
	}

	/**
	 * Get relatedParty
	 * @return relatedParty
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaRelatedPartyWsDto> getRelatedParty()
	{
		return relatedParty;
	}

	public void setRelatedParty(final List<TmaRelatedPartyWsDto> relatedParty)
	{
		this.relatedParty = relatedParty;
	}

	public TmaProductWsDto realizingResource(final List<TmaRealizingResourceWsDto> realizingResource)
	{
		this.realizingResource = realizingResource;
		return this;
	}

	public TmaProductWsDto addRealizingResourceItem(final TmaRealizingResourceWsDto realizingResourceItem)
	{
		if (this.realizingResource == null)
		{
			this.realizingResource = new java.util.ArrayList<>();
		}
		this.realizingResource.add(realizingResourceItem);
		return this;
	}

	/**
	 * Get realizingResource
	 * @return realizingResource
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaRealizingResourceWsDto> getRealizingResource()
	{
		return realizingResource;
	}

	public void setRealizingResource(final List<TmaRealizingResourceWsDto> realizingResource)
	{
		this.realizingResource = realizingResource;
	}

	public TmaProductWsDto realizingService(final List<TmaRealizingServiceWsDto> realizingService)
	{
		this.realizingService = realizingService;
		return this;
	}

	public TmaProductWsDto addRealizingServiceItem(final TmaRealizingServiceWsDto realizingServiceItem)
	{
		if (this.realizingService == null)
		{
			this.realizingService = new java.util.ArrayList<>();
		}
		this.realizingService.add(realizingServiceItem);
		return this;
	}

	/**
	 * Get realizingService
	 * @return realizingService
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaRealizingServiceWsDto> getRealizingService()
	{
		return realizingService;
	}

	public void setRealizingService(final List<TmaRealizingServiceWsDto> realizingService)
	{
		this.realizingService = realizingService;
	}

	public TmaProductWsDto productPrice(final List<TmaProductPriceWsDto> productPrice)
	{
		this.productPrice = productPrice;
		return this;
	}

	public TmaProductWsDto addProductPriceItem(final TmaProductPriceWsDto productPriceItem)
	{
		if (this.productPrice == null)
		{
			this.productPrice = new java.util.ArrayList<>();
		}
		this.productPrice.add(productPriceItem);
		return this;
	}

	/**
	 * Get productPrice
	 * @return productPrice
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaProductPriceWsDto> getProductPrice()
	{
		return productPrice;
	}

	public void setProductPrice(final List<TmaProductPriceWsDto> productPrice)
	{
		this.productPrice = productPrice;
	}

	public TmaProductWsDto productOrder(final List<TmaProductOrderRefWsDto> productOrder)
	{
		this.productOrder = productOrder;
		return this;
	}

	public TmaProductWsDto addProductOrderItem(final TmaProductOrderRefWsDto productOrderItem)
	{
		if (this.productOrder == null)
		{
			this.productOrder = new java.util.ArrayList<>();
		}
		this.productOrder.add(productOrderItem);
		return this;
	}

	/**
	 * Get productOrder
	 * @return productOrder
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaProductOrderRefWsDto> getProductOrder()
	{
		return productOrder;
	}

	public void setProductOrder(final List<TmaProductOrderRefWsDto> productOrder)
	{
		this.productOrder = productOrder;
	}

	public TmaProductWsDto productTerm(final List<TmaProductTermWsDto> productTerm)
	{
		this.productTerm = productTerm;
		return this;
	}

	public TmaProductWsDto addProductTermItem(final TmaProductTermWsDto productTermItem)
	{
		if (this.productTerm == null)
		{
			this.productTerm = new java.util.ArrayList<>();
		}
		this.productTerm.add(productTermItem);
		return this;
	}

	/**
	 * Get productTerm
	 * @return productTerm
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public List<TmaProductTermWsDto> getProductTerm()
	{
		return productTerm;
	}

	public void setProductTerm(final List<TmaProductTermWsDto> productTerm)
	{
		this.productTerm = productTerm;
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
		final TmaProductWsDto product = (TmaProductWsDto) o;
		return Objects.equals(this.id, product.id) &&
				Objects.equals(this.href, product.href) &&
				Objects.equals(this.description, product.description) &&
				Objects.equals(this.isBundle, product.isBundle) &&
				Objects.equals(this.isCustomerVisible, product.isCustomerVisible) &&
				Objects.equals(this.name, product.name) &&
				Objects.equals(this.productSerialNumber, product.productSerialNumber) &&
				Objects.equals(this.startDate, product.startDate) &&
				Objects.equals(this.status, product.status) &&
				Objects.equals(this.terminationDate, product.terminationDate) &&
				Objects.equals(this.baseType, product.baseType) &&
				Objects.equals(this.type, product.type) &&
				Objects.equals(this.schemaLocation, product.schemaLocation) &&
				Objects.equals(this.place, product.place) &&
				Objects.equals(this.productOffering, product.productOffering) &&
				Objects.equals(this.productSpecification, product.productSpecification) &&
				Objects.equals(this.characteristic, product.characteristic) &&
				Objects.equals(this.productRelationship, product.productRelationship) &&
				Objects.equals(this.billingAccount, product.billingAccount) &&
				Objects.equals(this.relatedParty, product.relatedParty) &&
				Objects.equals(this.realizingResource, product.realizingResource) &&
				Objects.equals(this.realizingService, product.realizingService) &&
				Objects.equals(this.productPrice, product.productPrice) &&
				Objects.equals(this.productOrder, product.productOrder) &&
				Objects.equals(this.productTerm, product.productTerm) &&
				Objects.equals(this.publicIdentifier, product.publicIdentifier) &&
				Objects.equals(this.user, product.user);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, href, description, isBundle, isCustomerVisible, name, productSerialNumber, startDate, //status,
				terminationDate, baseType, type, schemaLocation, place, productOffering, productSpecification, characteristic,
				productRelationship, billingAccount, relatedParty, realizingResource, realizingService, productPrice, productOrder,
				productTerm, publicIdentifier, user);
	}

	@Override
	public String toString()
	{
		return (" Product {\n")
				+ ("    id: ") + (toIndentedString(id)) + ("\n")
				+ ("    href: ") + (toIndentedString(href)) + ("\n")
				+ ("    description: ") + (toIndentedString(description)) + ("\n")
				+ ("    isBundle: ") + (toIndentedString(isBundle)) + ("\n")
				+ ("    isCustomerVisible: ") + (toIndentedString(isCustomerVisible)) + ("\n")
				+ ("    name: ") + (toIndentedString(name)) + ("\n")
				+ ("    productSerialNumber: ") + (toIndentedString(productSerialNumber)) + ("\n")
				+ ("    startDate: ") + (toIndentedString(startDate)) + ("\n")
				+ ("    status: ") + (toIndentedString(status)) + ("\n")
				+ ("    terminationDate: ") + (toIndentedString(terminationDate)) + ("\n")
				+ ("    baseType: ") + (toIndentedString(baseType)) + ("\n")
				+ ("    type: ") + (toIndentedString(type)) + ("\n")
				+ ("    schemaLocation: ") + (toIndentedString(schemaLocation)) + ("\n")
				+ ("    place: ") + (toIndentedString(place)) + ("\n")
				+ ("    productOffering: ") + (toIndentedString(productOffering)) + ("\n")
				+ ("    productSpecification: ") + (toIndentedString(productSpecification)) + ("\n")
				+ ("    characteristic: ") + (toIndentedString(characteristic)) + ("\n")
				+ ("    productRelationship: ") + (toIndentedString(productRelationship)) + ("\n")
				+ ("    billingAccount: ") + (toIndentedString(billingAccount)) + ("\n")
				+ ("    relatedParty: ") + (toIndentedString(relatedParty)) + ("\n")
				+ ("    realizingResource: ") + (toIndentedString(realizingResource)) + ("\n")
				+ ("    realizingService: ") + (toIndentedString(realizingService)) + ("\n")
				+ ("    productPrice: ") + (toIndentedString(productPrice)) + ("\n")
				+ ("    productOrder: ") + (toIndentedString(productOrder)) + ("\n")
				+ ("    productTerm: ") + (toIndentedString(productTerm)) + ("\n")
				+ ("    publicIdentifier: ") + (toIndentedString(publicIdentifier)) + ("\n")
				+ ("    user: ") + (toIndentedString(user)) + ("\n")
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

