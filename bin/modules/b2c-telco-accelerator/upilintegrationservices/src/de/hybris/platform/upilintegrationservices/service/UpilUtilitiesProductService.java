/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.service;

import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.data.UpilResponseList;

import java.util.List;


/**
 * UpilUtilitiesProductService interface to create new UtilitiesProduct
 * 
 * @since 1911
 */
public interface UpilUtilitiesProductService
{

	/**
	 * For Each PricePlan will create new UtilitiesProduct
	 * 
	 * @param commodityPricePlans
	 *           List of Price row model
	 * @return UpilResponseList
	 *         response containing successfully created products ids and failed products error details in upil
	 */

	UpilResponseList createUpilProduct(List<SubscriptionPricePlanModel> commodityPricePlans);
}
