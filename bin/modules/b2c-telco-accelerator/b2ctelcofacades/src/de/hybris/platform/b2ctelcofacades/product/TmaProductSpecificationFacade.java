/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;


/**
 * Facade that handles operations related to {@link TmaProductSpecificationData}.
 *
 * @since 2102
 */
public interface TmaProductSpecificationFacade
{
	/**
	 * Retrieves the {@link TmaProductSpecificationData} with the given id.
	 *
	 * @param id
	 * 		unique identifier of the {@link TmaProductSpecificationData} to be retrieved
	 * @return the {@link TmaProductSpecificationData} found.
	 * @throws ModelNotFoundException
	 * 		if no product specification is found.
	 */
	TmaProductSpecificationData getProductSpecification(final String id);
}
