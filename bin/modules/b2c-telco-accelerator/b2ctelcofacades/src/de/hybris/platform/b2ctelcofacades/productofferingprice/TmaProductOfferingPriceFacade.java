/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;


/**
 * Facade that handles operations related to {@link TmaProductOfferingPriceData}.
 *
 * @since 2102
 */
public interface TmaProductOfferingPriceFacade
{
	/**
	 * Retrieves the {@link TmaProductOfferingPriceData} with the given code.
	 *
	 * @param code
	 * 		unique identifier of the {@link TmaProductOfferingPriceData} to be retrieved
	 * @return the {@link TmaProductOfferingPriceData} found.
	 * @throws ModelNotFoundException
	 * 		if no product offering price is found.
	 */
	TmaProductOfferingPriceData getPop(final String code);
}
