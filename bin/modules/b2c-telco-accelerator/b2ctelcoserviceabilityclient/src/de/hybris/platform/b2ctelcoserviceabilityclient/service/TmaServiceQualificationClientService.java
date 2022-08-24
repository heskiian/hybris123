/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoserviceabilityclient.service;

import de.hybris.platform.b2ctelcotmfresources.v4.dto.QueryServiceQualification;


/**
 * Interface for getting the queryServiceQualification resource from the external system retrieves list of the services
 * available against the given search criteria
 *
 * @since 2102
 */
public interface TmaServiceQualificationClientService
{

	/**
	 * The method consumes QueryServiceQualification with search criteria and returns the
	 * QueryServiceQualification with the list of services available against the given search criteria, if no services
	 * are available then it returns the QueryServiceQualification with empty list of services .
	 *
	 * @param searchCriteria
	 *           QueryServiceQualification with search criteria ,i.e, the geographical address
	 *
	 * @return {@link QueryServiceQualification}
	 */
	QueryServiceQualification createQueryServiceQualification(final QueryServiceQualification searchCriteria);

}
