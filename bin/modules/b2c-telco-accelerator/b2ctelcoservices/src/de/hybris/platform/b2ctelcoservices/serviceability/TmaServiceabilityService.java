/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.serviceability;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;


/**
 * The service to compute the product serviceability at provided installation address
 *
 * @since 2102
 *
 */
public interface TmaServiceabilityService
{
	/**
	 *
	 * Method computes if the product is serviceable at the provided installation address
	 *
	 * @param product
	 *           the product Model
	 * @param installationAddress
	 *           the Installation address
	 *
	 * @return true if productOffering is serviceable, else false
	 */
	boolean isProductOfferingServiceable(ProductModel product, AddressModel installationAddress);
}
