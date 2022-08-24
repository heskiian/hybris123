/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.daos;



import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;
import java.util.Map;


/**
 * Data access object for {@link AgrAgreementModel}s.
 *
 * @since 2108
 */
public interface AgrAgreementDao extends GenericDao<AgrAgreementModel>
{
	/**
	 * Searches for a unique model based on given parameters.
	 * If none or multiple models are found, a specific exception will be thrown.
	 *
	 * @param params
	 *      {@link Map} containing name-value pairs used for identifying the unique model
	 * @return model for given parameter
	 */
	AgrAgreementModel findUnique(final Map<String, ? extends Object> params);

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
}
