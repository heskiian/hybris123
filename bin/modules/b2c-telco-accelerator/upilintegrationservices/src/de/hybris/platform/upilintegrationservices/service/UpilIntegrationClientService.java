/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.service;

import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.data.UpilResponse;
import de.hybris.platform.upilintegrationservices.exceptions.UpilintegrationservicesException;

import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;


/**
 * Upil client interface to establish the connection with Upil and perform integration operations
 *
 * @since 1911
 */
public interface UpilIntegrationClientService
{
	/**
	 * Connects with UPIL System and each {@link SubscriptionPricePlanModel} will create as a new UtilitiesProduct in
	 * UPIL
	 *
	 * @param product
	 *           product to be created
	 * @return {@link UpilResponse} returns response from Upil
	 */
	UpilResponse createUpilProduct(final Map<String, Object> product);

	/**
	 * Connects with UPIL System and get list of {@link ODataEntry} which contains UpilSemanticsModel details from UPIL
	 *
	 * @return returns list of {@link ODataEntry}
	 * @exception UpilintegrationservicesException
	 *               for exception throw by this method
	 */
	List<ODataEntry> getIsuSemantics() throws UpilintegrationservicesException;

	/**
	 * Connects with UPIL System and get list of {@link ODataEntry} which contains TmaProductSpecTypeModel details from
	 * UPIL
	 *
	 * @return returns list of {@link ODataEntry}
	 * @exception UpilintegrationservicesException
	 *               for exception throw by this method
	 */
	List<ODataEntry> getIsuReferenceTypes() throws UpilintegrationservicesException;
}
