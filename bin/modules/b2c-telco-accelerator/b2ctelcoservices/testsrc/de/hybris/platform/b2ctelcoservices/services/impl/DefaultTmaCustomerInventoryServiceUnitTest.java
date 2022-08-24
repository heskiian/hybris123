/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductLineModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


/**
 * Test class for {@link DefaultTmaCustomerInventoryService}
 *
 * @since 1911
 */
@UnitTest
public class DefaultTmaCustomerInventoryServiceUnitTest
{
	private static final String PRODUCT_ID = "111";
	private static final String USER_ID = "user1";
	private static final String PRODUCT_OFFERING_SALSA_S = "salsaS";
	private static final String PRODUCT_SPEC_TYPE_GSM = "gsm";

	private DefaultTmaCustomerInventoryService customerInventoryService;
	private CustomerModel customer;
	private List<TmaSubscriptionBaseModel> subscriptionBases;
	private TmaSubscribedProductModel subscribedProduct;
	private TmaProductLineModel productLine;

	@Mock
	private TmaSubscriptionBaseService mockSubscriptionBaseService;

	@Mock
	private TmaPoService mockPoService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		customer = new CustomerModel();
		customer.setUid(USER_ID);
		productLine = new TmaProductLineModel();
		productLine.setCode(PRODUCT_SPEC_TYPE_GSM);
		populateSubscriptionBases();
		this.customerInventoryService = new DefaultTmaCustomerInventoryService();
		customerInventoryService.setSubscriptionBaseService(mockSubscriptionBaseService);
		when(mockSubscriptionBaseService.getSubscriptionBases(customer.getUid())).thenReturn(subscriptionBases);
		final TmaProductOfferingModel productOffering = new TmaProductOfferingModel();
		productOffering.setCode(PRODUCT_OFFERING_SALSA_S);
		when(mockPoService.getPoForCode(PRODUCT_OFFERING_SALSA_S)).thenReturn(productOffering);
	}

	@Test
	public void testDoesSubscribedProductBelongToCustomer()
	{
		Assert.assertTrue(customerInventoryService.getSubscribedProductById(PRODUCT_ID, customer).isPresent());
	}

	@Test
	public void testFindSubscribedProductById()
	{
		Assert.assertNotNull(customerInventoryService.getSubscribedProductById(PRODUCT_ID, customer));
	}

	@Test
	public void testCanReplaceSubscribedProductWithOffering()
	{
		final TmaProductOfferingModel offering = new TmaProductOfferingModel();
		final TmaProductSpecificationModel productSpecification = new TmaProductSpecificationModel();
		final Set<TmaProductSpecTypeModel> productSpecTypes = new HashSet<>();
		productSpecTypes.add(productLine);
		productSpecification.setProductSpecificationTypes(productSpecTypes);
		offering.setProductSpecification(productSpecification);
		offering.setCode(PRODUCT_OFFERING_SALSA_S);
		when(mockPoService.getPoForCode(PRODUCT_OFFERING_SALSA_S)).thenReturn(offering);

		Assert.assertTrue(customerInventoryService.canReplaceSubscribedProductWithOffering(subscribedProduct, offering));
	}

	private void populateSubscriptionBases()
	{
		final TmaSubscriptionBaseModel subscriptionBase = new TmaSubscriptionBaseModel();
		subscriptionBase.setSubscribedProducts(createSubscribedProducts());
		subscriptionBases = new ArrayList<>();
		subscriptionBases.add(subscriptionBase);
	}

	private Set<TmaSubscribedProductModel> createSubscribedProducts()
	{
		final Set<TmaSubscribedProductModel> subscribedProducts = new HashSet<>();
		subscribedProduct = new TmaSubscribedProductModel();
		subscribedProduct.setId(PRODUCT_ID);
		subscribedProduct.setProductCode(PRODUCT_OFFERING_SALSA_S);
		final TmaProductSpecificationModel productSpecification = new TmaProductSpecificationModel();
		final Set<TmaProductSpecTypeModel> productSpecTypes = new HashSet<>();
		productSpecTypes.add(productLine);
		productSpecification.setProductSpecificationTypes(productSpecTypes);
		subscribedProduct.setProductSpecification(productSpecification);
		subscribedProducts.add(subscribedProduct);
		return subscribedProducts;
	}
}
