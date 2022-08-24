/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order;

import de.hybris.platform.b2ctelcofacades.data.CartActionInput;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.exceptions.CalculationException;

import java.util.List;
import java.util.Optional;

/**
 * Facade handling cart operations for {@link de.hybris.platform.b2ctelcoservices.jalo.TmaProductOffering}.
 *
 * @since 6.7
 */
public interface TmaCartFacade extends CartFacade
{
	/**
	 * Get a cart using code and {@link UserModel}
	 *
	 * @param code
	 *           The cart identifier code
	 * @param customerId
	 *           The id of the customer who owns the cart
	 * @return cart
	 */
	Optional<CartData> getCartForCodeAndCustomer(String code, String customerId);

	/**
	 * Get a cart using code and customer id
	 *
	 * @param code
	 *           The cart identifier code
	 * @param customerId
	 *           The id of the customer who owns the cart
	 * @param recalculate
	 *           Flag which indicates if the cart should be recalculated or not
	 * @return cart
	 * @throws CalculationException
	 *            If errors occurre during cart recalculation
	 */
	Optional<CartData> getCartForCodeAndCustomer(String code, String customerId, boolean recalculate) throws CalculationException;

	/**
	 * Get the list of carts for a customer
	 *
	 * @param customerId
	 *           The id of the customer who owns the carts
	 * @return the cart list
	 */
	List<CartData> getCartsForCustomer(String customerId);

	/**
	 * Get the list of carts for a customer
	 *
	 * @param customerId
	 *           The id of the customer who owns the carts
	 * @param recalculate
	 *           Flag which indicates if the cart should be recalculated or not
	 * @return the cart list
	 * @throws CalculationException
	 *            If errors occurre during cart recalculation
	 */
	List<CartData> getCartsForCustomer(String customerId, boolean recalculate) throws CalculationException;

	/**
	 * Processes cart entry updates.
	 *
	 * @param cartActionInputList
	 *           contains attributes used for cart entry updates
	 * @return list {@link CartModificationData}
	 * @throws CommerceCartModificationException
	 *            in case of any error occurs during cart entry update
	 */
	List<CartModificationData> processCartAction(List<CartActionInput> cartActionInputList)
			throws CommerceCartModificationException;

	/**
	 * Creates a cart for the provided user if cart does not exist. If cart exists returns the existing cart.
	 *
	 * @param userId
	 *           The unique identifier of the user.
	 * @return The cart which was created.
	 */
	CartData createCartForUserAndCurrentBaseSite(String userId);

	/**
	 * Adds a new cart entry containing a {@link de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel}
	 * to the existing cart.
	 * The product offering can be added as a single offering, but also as part of a
	 * {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel}.
	 *
	 * @param spoCode
	 *           {@link de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel#CODE }
	 * @param quantity
	 *           product offering quantity to be added to cart
	 * @param processType
	 *           represents the process flow in the context of which the entry is added to cart (Acquisition, Retention,
	 *           etc.)
	 * @param rootBpoCode
	 *           root {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel#CODE}
	 * @param cartGroupNo
	 *           the cart entry group number on which the new entry to be attached to, if -1 a new entry group will be
	 *           created
	 * @param subscriptionTermId
	 *           subscription term id for the entry
	 * @param subscriberId
	 *           subscriber id in case of an existing customer
	 * @param subscriberBillingId
	 *           subscriber billing id in case of an existing customer
	 * @return details related to the new cart entry
	 * @throws CommerceCartModificationException
	 *            in case of any error occurs during new cart entry addition
	 * @deprecated since 1907, use {@link TmaCartFacade#processCartAction(List)} instead
	 */
	@Deprecated(since = "1907", forRemoval = true)
	CartModificationData addProductOfferingToCart(String spoCode, long quantity, String processType, String rootBpoCode,
			int cartGroupNo, String subscriptionTermId, String subscriberId, String subscriberBillingId)
			throws CommerceCartModificationException;

	/**
	 * Adds multiple cart entries containing a
	 * {@link de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel} to the existing cart, all of them
	 * being part of the
	 * {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel} specified by the rootBpoCode. If
	 * the cartGroupNo is valid, then the new entries are added
	 * as part of the corresponding cart group, otherwise, if cartGroupNo is -1, a new cart group is created for the new
	 * added entries.
	 *
	 * @param rootBpoCode
	 *           {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel#CODE} of the bundled
	 *           offering added to cart
	 * @param simpleProductOfferings
	 *           a list containing {@link de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel#CODE}
	 * @param processType
	 *           represents the process flow in the context of which the entry is added to cart (Acquisition, Retention,
	 *           etc.)
	 * @param cartGroupNo
	 *           existing cart entry group number
	 * @param subscriptionTermId
	 *           subscription term id for the entry
	 * @param subscriberId
	 *           subscriber id in case of an existing customer
	 * @param subscriberBillingId
	 *           subscriber billing id in case of an existing customer
	 * @return details regarding new added entries
	 * @throws CommerceCartModificationException
	 *            in case of any error occurs during new cart entries addition
	 * @deprecated since 1907, use {@link TmaCartFacade#processCartAction(List)} instead
	 */
	@Deprecated(since = "1907", forRemoval = true)
	List<CartModificationData> addBpoSelectedOfferings(String rootBpoCode, List<String> simpleProductOfferings, String processType,
			int cartGroupNo, String subscriptionTermId, String subscriberId, String subscriberBillingId)
			throws CommerceCartModificationException;

	/**
	 * Get a cart using guid
	 *
	 * @param guid
	 * @return the specific cart for given guid
	 */
	Optional<CartData> getCartForGuid(String guid);

	/**
	 * Checks if given card belongs to anonymous user.
	 *
	 * @param cartGuid
	 *           GUID of the cart.
	 * @return <tt>true</tt> if the cart belongs to anonymous user.
	 */
	boolean isAnonymousUserCart(String cartGuid);

	/**
	 * Checks if given card belongs to current user.
	 *
	 * @param cartGuid
	 *           GUID of the cart.
	 * @return <tt>true</tt> if the cart belongs to current user.
	 */
	boolean isCurrentUserCart(String cartGuid);

	/**
	 * Validates and updates cart action inputs for clone operation
	 *
	 * @param cartActionInput
	 * @throws CommerceSaveCartException
	 */
	void validateAndUpdateCartActionInputForClone(CartActionInput cartActionInput) throws CommerceSaveCartException;

	/**
	 * Checks if the requested status for cart is valid or not
	 *
	 * @param status
	 *           the requested status to be updated for cart
	 * @return True if requested status is valid status, otherwise false
	 */
	boolean isValidCartStatus(String status);
}
