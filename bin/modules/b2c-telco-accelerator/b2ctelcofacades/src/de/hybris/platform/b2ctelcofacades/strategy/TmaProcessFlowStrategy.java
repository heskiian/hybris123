/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.strategy;


import de.hybris.platform.b2ctelcofacades.data.TmaOfferContextData;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;

import java.util.List;
import java.util.Set;


/**
 * Handles product offers for different process flows (retention, tariff change, etc.)
 *
 * @since 6.7
 */
public interface TmaProcessFlowStrategy
{
	/**
	 * Returns product offers - when device is not in a BPO.
	 *
	 * @param productCode
	 * 		code of currently selected product
	 * @return {@link List} of {@link TmaOfferData}
	 */
	List<TmaOfferData> getOffersForDeviceOnly(String productCode);

	 /**
	  * Returns product offers filtered by parameters provided in offerContextData.
	  *
	  * @param offerContextData
	  * 		contains the parameters for the offer
	  * @return {@link List} of {@link TmaOfferData}
	  */
	 List<TmaOfferData> getOffers(TmaOfferContextData offerContextData);

	/**
	 * Returns product offers - when device is in a BPO.
	 *
	 * @param productCode
	 * 		code of the product that is currently selected
	 * @param bpoCode
	 * 		code of the bpo assigned to the main tariff service in CPI
	 * @param requiredProductCodes
	 * 		codes of the products assigned to the main tariff services in CPI
	 * @return {@link List} of {@link TmaOfferData}
	 */
	List<TmaOfferData> getOffersForDeviceInBpo(final String productCode, final String bpoCode,
			final Set<String> requiredProductCodes);

}
