/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Facade gathering {@link TmaSubscribedProductData} related operations.
 * 
 * @since 6.6
 */
public interface TmaSubscribedProductFacade
{
	/**
	 * Creates and persists a Subscribed Product.
	 * 
	 * @param subscribedProductSource
	 *           Subscribed Product to be persisted
	 * @return newly created Subscribed Product
	 */
	TmaSubscribedProductData createSubscribedProduct(final TmaSubscribedProductData subscribedProductSource);

	/**
	 * Updates the Subscribed Product with the specified {@link TmaSubscribedProductData}.
	 * 
	 * @param billingSystemId
	 *           identifier of the billing system
	 * @param billingSubscriptionId
	 *           identifier for existing Subscribed Product
	 * @param subscribedProductData
	 *           data used to update the Subscribed Product
	 * @return the updated Subscribed Product
	 */
	TmaSubscribedProductData updateSubscribedProduct(final String billingSystemId, final String billingSubscriptionId,
			final TmaSubscribedProductData subscribedProductData);

	/**
	 * Updates the Subscribed Product with the specified subscriptionId
	 * 
	 * @param subscriptionId
	 *           identifier of the subscription
	 * @param paymentMethodId
	 *           identifier for merchant subscription on credit card
	 * @return the updated Subscribed Product
	 */
	TmaSubscribedProductData replacePaymentMethod(final String subscriptionId, final String paymentMethodId);

	/**
	 * Deletes the Subscribed Product identified by billingSystemId and billingSubscriptionId.
	 * 
	 * @param billingSystemId
	 *           identifier of the billing system
	 * @param billingSubscriptionId
	 *           identifier of the billing subscription
	 */
	void deleteSubscribedProduct(final String billingSystemId, final String billingSubscriptionId);

	/**
	 * Returns the relevant list of TmaSubscribedProductData for logged in user
	 * 
	 * @return the Map<TmaSubscriptionAccessData, Set<TmaSubscribedProductData>> containing subscription related data
	 */
	Map<TmaSubscriptionAccessData, Set<TmaSubscribedProductData>> getSubscriptions() throws SubscriptionFacadeException;

	/**
	 * Returns the TmaSubscribedProductData for given id
	 * 
	 * @param subscriptionId
	 *           identifier of the subscription
	 * @return the TmaSubscribedProductData containing subscription related data
	 */
	TmaSubscribedProductData getSubscriptionsById(String subscriptionId);

	/**
	 * Retrieving {@link TmaSubscribedProductData}s for a given subscriberIdentity and billingSystemId.
	 * 
	 * @param subscriberIdentity
	 *           identifier of a SubscribedProduct
	 * @param billingSystemId
	 *           identifier of a SubscribedProduct
	 * @return {@link TmaSubscribedProductData}s belonging to the {@link SubscriptionBase} identified by the given
	 *         parameters.
	 */
	List<TmaSubscribedProductData> getSubscribedProducts(final String subscriberIdentity, final String billingSystemId);

	/**
	 * Returns the List of TmaSubscribedProductData for given id
	 * 
	 * @param paymentMethodId
	 *           paymentMethodId of the subscription
	 * @return the List of TmaSubscribedProductData containing subscription related data which has given paymentMethodId
	 */
	List<TmaSubscribedProductData> getSubscriptionsForPaymentMethod(final String paymentMethodId);

	/**
	 * Returns a list of possible upgrade options for the given subscription product.
	 * 
	 * @param productCode
	 *           the code of the product
	 * @return {@link List} of {@link ProductData}
	 */
	List<ProductData> getUpsellingOptionsForSubscription(String productCode);


	/**
	 * Returns all subscription base services for the current user.
	 * 
	 * @param billingSystemId
	 *           identifier of the billing system
	 * @param subscriberIdentity
	 *           identifier of the subscriber
	 * @return {@link Collection}<{@link TmaSubscribedProductModel}> the current user's subscriptionBase
	 * @exception SubscriptionFacadeException
	 *               for exception thrown by this method
	 */
	Collection<TmaSubscribedProductModel> getSubscriptionBaseServices(final String subscriberIdentity,
			final String billingSystemId) throws SubscriptionFacadeException;

	/**
	 * Returns all subscription base services for the current user.
	 * 
	 * @param billingSystemId
	 *           identifier of the billing system
	 * @param subscriberIdentity
	 *           identifier of the subscriber
	 * @return {@link Collection}<{@link TmaSubscribedProductData , @link Collection{@link TmaAverageServiceUsageData}
	 *         >the current user's subscriptionBase
	 * @exception SubscriptionFacadeException
	 *               for exception thrown by this method
	 */
	Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> getSubscriptionBaseServicesWithAvgValues(
			final String subscriberIdentity, final String billingSystemId) throws SubscriptionFacadeException;

	/**
	 * Returns the access type of current user's subscription base.
	 * 
	 * @param billingSystemId
	 *           identifier of the billing system
	 * @param subscriberIdentity
	 *           identifier of the subscriber
	 * @return Access type as {@link String}
	 */
	String getSubscriptionAccessByPrincipalAndSubscriptionBase(String billingSystemId, String subscriberIdentity);

	/**
	 * Returns a list of possible compatible addon products for current user's subscription base.
	 * 
	 * @param subscriberIdentity
	 *           the id of the subscription Base
	 * @param billingSystemId
	 *           identifier of the billing system
	 * @return {@link Set} of {@link ProductData} returns product data
	 * @exception SubscriptionFacadeException
	 *               for exception throw by this method
	 */
	Set<ProductData> getSubscriptionCompatibleAddons(String subscriberIdentity, String billingSystemId)
			throws SubscriptionFacadeException;

	/**
	 * Returns compatible upsell subscription product data.
	 * 
	 * @param subscriberIdentity
	 *           identifier of the subscriber
	 * @param billingSystemId
	 *           identifier of the billing system
	 * @return Product data
	 */
	ProductData upsellSubscriptionProductData(String subscriberIdentity, String billingSystemId);


	/**
	 * Creates the Subscription Billing data.
	 * 
	 * @param subscriptionId
	 *           id for active subscription
	 * @return {@link List} of {@link SubscriptionBillingData}
	 * @exception SubscriptionFacadeException
	 *               for exception throw by this method
	 */
	List<SubscriptionBillingData> createBillingActivityData(String subscriptionId) throws SubscriptionFacadeException;

	/**
	 * Get the Subscription End date .
	 * 
	 * @param frequencyCode
	 *           Code for {@link TermOfServiceFrequency}
	 * @param contractDuration
	 *           Duration of Contract of the Service
	 * @param startDate
	 *           Start date of Contract of the Service
	 * @return the End Date of the Service
	 */
	Date getSubscriptionServiceEndDate(final String frequencyCode, final Integer contractDuration, final Date startDate);

	/**
	 * Returns a list of possible up sell products based on current user's subscription service usage .
	 * 
	 * @param subscriberIdentity
	 *           the id of the subscription Base
	 * @param billingSystemId
	 *           identifier of the billing system
	 * @return {@link Set} of {@link ProductData} returns product data
	 * @exception SubscriptionFacadeException
	 *               for exception throw by this method
	 */
	Set<ProductData> getServiceUsageUpSellProducts(String subscriberIdentity, String billingSystemId)
			throws SubscriptionFacadeException;

	/**
	 * Returns true if product belongs to addon category .
	 * 
	 * @param product
	 *           ProductModel instance
	 * @return {@link boolean} returns true if product belongs to addon category else false
	 */
	boolean isAddonProduct(ProductModel product);

	/**
	 * Returns true if product type of Subscription Product .
	 * 
	 * @param product
	 *           ProductModel instance
	 * @return {@link boolean} returns true if product is of type Subscription Product else false
	 */
	boolean isSubscriptionProduct(ProductModel product);

	/**
	 * Returns ProductData with updated price plan subscription term .
	 * 
	 * @param productData
	 *           ProdcutData instance
	 * @return {@link ProductData} returns ProdcutData with updated price plan subscription term
	 */
	ProductData getSubscriptionTermAndPrice(ProductData productData);

	/**
	 * Returns all subscription base services for subscription base service Id and subscriberIdentity.
	 * 
	 * @param subscriberIdentity
	 *           identifier of the subscriber
	 * @param subscribedProductId
	 *           subscription base service Id
	 * @return {@link Collection}<{@link TmaSubscribedProductData , @link Collection{@link TmaAverageServiceUsageData}
	 *         >the current user's subscriptionBase
	 * @exception SubscriptionFacadeException
	 *               for exception thrown by this method
	 */
	Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> getSubscriptionBaseProductWithAvgValues(
			String subscriberIdentity, String subscribedProductId) throws SubscriptionFacadeException;

	/**
	 * Returns compatible upsell subscription product data.
	 * 
	 * @param subscriberIdentity
	 *           identifier of the subscriber
	 * @return Product data
	 */
	ProductData getUpsellSubscribedProductForIdentity(String subscriberIdentity);

	/**
	 * Returns a list of possible compatible addon products for current user's subscription base.
	 * @param subscriberIdentity
	 *           the id of the subscription Base
	 * @return {@link Set} of {@link ProductData} returns product data
	 * @exception SubscriptionFacadeException
	 *               for exception throw by this method
	 */
	Set<ProductData> getCompatibleAddonsForSubscriberIdentity(String subscriberIdentity)
			throws SubscriptionFacadeException;

	/**
	 * Returns a list of possible up sell products based on current user's subscription service usage .
	 * 
	 * @param subscriberIdentity
	 *           the id of the subscription Base
	 * @return {@link Set} of {@link ProductData} returns product data
	 * @exception SubscriptionFacadeException
	 *               for exception throw by this method
	 */
	Set<ProductData> getServiceUsageUpSellProductsWithSubscriberIdentity(String subscriberIdentity)
			throws SubscriptionFacadeException;

	/**
	 * Returns true if product belongs to plan category .
	 * 
	 * @param product
	 *           ProductModel instance
	 * @return {@link boolean} returns true if product belongs to plan category else false
	 */
	boolean isServicePlan(ProductModel product);
}
