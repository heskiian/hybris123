/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementservices.services;


import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;


/**
 * Service responsible for handling {@link AgrAgreementModel}  related operations.
 *
 * @since 2108
 */
public interface AgrAgreementsService
{
	/**
	 * Returns a {@link AgrAgreementModel} for the given id.
	 *
	 * @param id
	 * 		identifier of {@link AgrAgreementModel}
	 * @return the {@link AgrAgreementModel} found.
	 * @throws ModelNotFoundException
	 * 		if no agreement is found.
	 */
	AgrAgreementModel getAgreement(final String id);

	/**
	 * Retrieves a list of {@link AgrAgreementModel} for a given context.
	 *
	 * @param agrAgreementContext
	 * 		the context.
	 * @param offset
	 * 		the offset.
	 * @param limit
	 * 		the maximum number of returned agreements.
	 * @return the list of agreements found for the given context.
	 */
	List<AgrAgreementModel> getAgreements(final AgrAgreementContext agrAgreementContext, final Integer offset,
			final Integer limit);

	/**
	 * Retrieves the total number of agreements found for a given context.
	 *
	 * @param agrAgreementContext
	 * 		the context.
	 * @return the number of agreements found.
	 */
	Integer getNumberOfAgreementsFor(final AgrAgreementContext agrAgreementContext);

	/**
	 * Creates an instance of {@link AgrAgreementModel}.
	 *
	 * @return the newly created model.
	 */
	AgrAgreementModel createAgreement();

	/**
	 * Removes the given {@link AgrAgreementModel}
	 *
	 * @param agreement
	 * 		the agreement.
	 */
	void removeAgreement(final AgrAgreementModel agreement);

	/**
	 * Saves the given {@link AgrAgreementModel}.
	 *
	 * @param agreement
	 * 		the agreement.
	 */
	void saveAgreement(final AgrAgreementModel agreement);
}
