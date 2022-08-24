/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoserviceabilityclient.client;

import de.hybris.platform.b2ctelcotmfresources.v4.dto.QueryServiceQualification;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.hybris.charon.annotations.Control;
import com.hybris.charon.annotations.Http;


/**
 * Charon Client to the
 * {@link de.hybris.platform.b2ctelcoserviceabilityclient.service.TmaServiceQualificationClientService}
 *
 * @since 2102
 */
@Http
public interface ServiceabilityHttpClient
{

	/**
	 * This method connects to the queryServiceQualification end-point and returns the list of the services that are
	 * available against the given search criteria
	 *
	 * @param searchCriteria
	 *           QueryServiceQualification with search criteria ,i.e, the geographical address
	 *
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/queryServiceQualification")
	@Control(retries = "3", retriesInterval = "500")
	QueryServiceQualification queryServiceQualification(QueryServiceQualification searchCriteria);

}
