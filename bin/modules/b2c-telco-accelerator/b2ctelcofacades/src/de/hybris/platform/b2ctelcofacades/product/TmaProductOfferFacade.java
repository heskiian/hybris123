/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product;


import de.hybris.platform.b2ctelcofacades.data.TmaOfferContextData;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.data.TmaPriceContextData;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Facade that handles operations related to {@link TmaProductOfferingModel}.
 *
 * @since 6.7
 */
public interface TmaProductOfferFacade
{
	/**
	 * Retrieves the {@link ProductData} with the code given populated with information depending on the provided
	 * {@param productOptions}. The current session data (catalog versions, user) are used, so a valid
	 * {@link TmaProductOfferingModel} for the current session parameters will be returned.
	 *
	 * @param poCode
	 * 		unique identifier of the {@link TmaProductOfferingModel} to be retrieved
	 * @param productOptions
	 * 		product options determining the attributes of the {@link ProductData} to be populated
	 * @return {@link ProductData} populated with the given options for the provided code
	 * @throws IllegalArgumentException
	 * 		when given product code is null
	 * @throws UnknownIdentifierException
	 * 		when product with the given code is not found
	 */
	ProductData getPoForCode(final String poCode, final Collection<ProductOption> productOptions);

	/**
	 * Retrieves the {@link TmaBundledProductOfferingModel}s representing direct and indirect parents of the
	 * {@link TmaProductOfferingModel} with the code given.
	 *
	 * @param poCode
	 * 		unique identifier of the {@link TmaProductOfferingModel} for which to retrieve the parent bpos
	 * @param productOptions
	 * 		product options determining the attributes of the {@link ProductData} to be populated
	 * @return {@link List<ProductData>} all bpo parents for the {@link TmaProductOfferingModel} with the code given
	 */
	List<ProductData> getParentsForCodeAndOptions(final String poCode, final Collection<ProductOption> productOptions);

	/**
	 * Verifies if the given parent code represents a valid {@link TmaBundledProductOfferingModel} parent for the given
	 * product offering code. A valid parent it not necessary a first direct parent, it can also be a parent of one of
	 * product parents.
	 *
	 * @param poCode
	 * 		product offering code
	 * @param bpoCode
	 * 		parent bundled product offering code
	 * @return true if the parent is valid, false otherwise
	 */
	boolean isValidParent(final String poCode, final String bpoCode);

	/**
	 * Returns product offers for given parameters.
	 *
	 * @param processType
	 * 		type of the process
	 * @param affectedProductCode
	 * 		code of the product that is currently selected
	 * @param bpoCode
	 * 		code of the bpo assigned to the main tariff service in CPI
	 * @param requiredProductCodes
	 * 		main tariff codes assigned in CPI
	 * @return {@link List} of {@link TmaOfferData}
	 */
	List<TmaOfferData> getOffers(final String processType, final String affectedProductCode, final String bpoCode,
			final Set<String> requiredProductCodes);

	/**
	 * Retrieves the {@link ProductData} with the given code, with additional information provided as
	 * {@param productOptions} and prices of the {@link TmaProductOfferingModel} based on the
	 * {@link TmaPriceContextData}.
	 *
	 * @param poCode
	 * 		unique identifier of the {@link TmaProductOfferingModel} to be retrieved
	 * @param productOptions
	 * 		product options determining the attributes of the {@link ProductData} to be populated
	 * @param priceContextData
	 *      {@link TmaPriceContextData} to create price context data
	 * @return {@link ProductData} populated with the given options for the provided code
	 */
	ProductData getPoForCode(String poCode, Collection<ProductOption> productOptions, TmaPriceContextData priceContextData);

	/**
	 * Returns product offers filtered by parameters provided in offerContextData.
	 *
	 * @param offerContextData
	 * 		contains the parameters for the offer
	 * @return {@link List} of {@link TmaOfferData}
	 */
	List<TmaOfferData> getOffers(final TmaOfferContextData offerContextData);

	/**
	 * This method verifies if the action from the parameter is a service request action
	 *
	 * @param action
	 * 		The input action.
	 * @return True if the action is a service request action.
	 */
	boolean isServiceRequestAction(final TmaSubscribedProductAction action);

	/**
	 * This method checks if the product offering is of type BPO.
	 *
	 * @param productData
	 * 		the product offering.
	 * @return true if BPO, false otherwise.
	 */
	boolean isBpo(final ProductData productData);
}



