/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.daos;

import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;
import java.util.Map;


/**
 * Data access object for {@link AgrAgreementSpecificationModel}s.
 *
 * @since 2108
 */
public interface AgrAgreementSpecificationDao extends GenericDao<AgrAgreementSpecificationModel>
{
	/**
	 * Searches for a unique model based on given parameters.
	 * If none or multiple models are found, a specific exception will be thrown.
	 *
	 * @param params
	 *      {@link Map} containing name-value pairs used for identifying the unique model
	 * @return model for given parameter
	 */
	AgrAgreementSpecificationModel findUnique(final Map<String, ? extends Object> params);

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
}
