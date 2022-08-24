/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;


import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Set;


/**
 * Service for operations related to the {@link TmaProductSpecificationModel}.
 *
 * @since 6.7
 */
public interface TmaProductSpecificationService
{
	/**
	 * Collects all {@link TmaProductSpecCharacteristicModel}s configured as Product Specification Characteristics on the
	 * given {@param productSpecification} as well as on the children product specifications configured on the provided
	 * parent.
	 *
	 * @param productSpecification
	 *           product specification for which to retrieve all available {@link TmaProductSpecCharacteristicModel}s
	 *           (configured on itself and on its children)
	 * @return collection of {@link TmaProductSpecCharacteristicModel}s containing the Product Specification
	 *         Characteristics from both the{@param productSpecification} given as well as its children
	 */
	Set<TmaProductSpecCharacteristicModel> getProductSpecCharacteristicsForPsStructure(
			final TmaProductSpecificationModel productSpecification);


	/**
	 * get TmaProductSpecCharacteristicValueModel For given Id
	 *
	 * @param pscvId
	 *           String should be ID of required PSCV
	 * @return TmaProductSpecCharacteristicValueModel Product Spec Characteristic Value Model for given ID
	 *
	 */
	TmaProductSpecCharacteristicValueModel getTmaProductSpecCharacteristicValueModelForId(final String pscvId);

	/**
	 * Returns a {@link TmaProductSpecificationModel} for the given id.
	 *
	 * @param id
	 * 		identifier of {@link TmaProductSpecificationModel}
	 * @return the {@link TmaProductSpecificationModel} found.
	 * @throws ModelNotFoundException
	 * 		if no Product Specification is found.
	 */
	TmaProductSpecificationModel getProductSpecification(final String id);
}
