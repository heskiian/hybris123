/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.services;


import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;


/**
 * Service responsible for handling {@link AgrAgreementSpecificationModel}  related operations.
 *
 * @since 2108
 */
public interface AgrAgreementSpecificationService
{
	/**
	 * Returns a {@link AgrAgreementSpecificationModel} for the given id.
	 *
	 * @param id
	 * 		identifier of {@link AgrAgreementSpecificationModel}
	 * @return the {@link AgrAgreementSpecificationModel} found.
	 * @throws ModelNotFoundException
	 * 		if no agreement specification is found.
	 */
	AgrAgreementSpecificationModel getAgreementSpecification(final String id);

	/**
	 * Retrieves a list of {@link AgrAgreementSpecificationModel} for a given context.
	 *
	 * @param agrAgreementContext
	 * 		the context.
	 * @param offset
	 * 		the offset.
	 * @param limit
	 * 		the maximum number of returned agreementSpecifications.
	 * @return the list of agreement specifications found for the given context.
	 */
	List<AgrAgreementSpecificationModel> getAgreementSpecifications(final AgrAgreementContext agrAgreementContext,
			final Integer offset,
			final Integer limit);

	/**
	 * Retrieves the total number of agreement specifications found for a given context.
	 *
	 * @param agrAgreementContext
	 * 		the context.
	 * @return the number of agreement specifications found.
	 */
	Integer getNumberOfAgreementSpecificationsFor(final AgrAgreementContext agrAgreementContext);

	/**
	 * Creates an instance of {@link AgrAgreementSpecificationModel}.
	 *
	 * @return the newly created model.
	 */
	AgrAgreementSpecificationModel createAgreementSpecification();

	/**
	 * Removes the given {@link AgrAgreementSpecificationModel}
	 *
	 * @param agreementSpecification
	 * 		the agreement specification.
	 */
	void removeAgreementSpecification(final AgrAgreementSpecificationModel agreementSpecification);

	/**
	 * Saves the given {@link AgrAgreementSpecificationModel}.
	 *
	 * @param agreementSpecification
	 * 		the agreement specification.
	 */
	void saveAgreementSpecification(final AgrAgreementSpecificationModel agreementSpecification);
}
