/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.retention.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionAccessService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.directpersistence.audit.dao.WriteAuditRecordsDAO;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ServicesCustomerCleanupHookUnitTest
{
	private static final String AVERAGE_SERVICE_USAGE_PK = "123456";
	private static final String SERVICE_PK = "465432";
	private static final String BILLINGACCOUNT_PK = "376543";
	private static final String SUBSCRIPTIONBASE_PK = "287654";
	private static final String SUBSCRIPTIONACCESS_PK = "198765";
	private static final String CUSTOMER_UID = "customer01";
	private static final String BILLING_SYSTEM_ID = "billingSystemId";
	private static final String SUBSCRIBER_IDENTITY = "subscriberIdentity";
	@InjectMocks
	private final ServicesCustomerCleanupHook customerCleanupHook = new ServicesCustomerCleanupHook();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Mock
	private ModelService modelService;

	@Mock
	private TmaSubscribedProductModel subscribedProductModel;

	@Mock
	private TmaSubscriptionAccessService subscriptionAccessService;
	@Mock
	private WriteAuditRecordsDAO writeAuditRecordsDAO;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldCleanupRelatedObjects()
	{
		shouldCleanupRelatedObjects(true, true);
	}

	@Test
	public void shouldCleanupRelatedObjectsWhenPrincipalIsNull()
	{
		shouldCleanupRelatedObjects(false, true);
	}

	@Test
	public void shouldCleanUpRelatedObjectsWhenSubscriptionBaseIsNull()
	{
		shouldCleanupRelatedObjects(false, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotCleanupRelatedObjectsIfInputIsNull()
	{
		customerCleanupHook.cleanupRelatedObjects(null);
	}

	@Test
	public void whenAverageServiceUsagesIsNull()
	{
		given(subscribedProductModel.getAverageServiceUsages()).willReturn(null);
		customerCleanupHook.removeAverageServiceUsages(subscribedProductModel);
	}

	@Test
	public void shouldRemoveItem()
	{
		final TmaSubscriptionAccessModel subscriptionAccessModel = mock(TmaSubscriptionAccessModel.class);
		final PK subscriptionAccessPK = PK.parse(SUBSCRIPTIONACCESS_PK);

		given(subscriptionAccessModel.getPk()).willReturn(subscriptionAccessPK);

		customerCleanupHook.removeItem(TmaSubscriptionAccessModel._TYPECODE, subscriptionAccessModel.getPk());
		verify(modelService).remove(subscriptionAccessModel.getPk());
	}

	private void shouldCleanupRelatedObjects(final boolean givenMockedCustomer, final boolean givenMockedSubscriptionBase)
	{
		final MockCleanUpRelatedObjects mockCleanUpRelatedObjects = new MockCleanUpRelatedObjects().mockCommonValues();

		givenCleanUpRelatedObjects(givenMockedCustomer, givenMockedSubscriptionBase, mockCleanUpRelatedObjects);

		assertCleanUpRelatedObjects(givenMockedCustomer, givenMockedSubscriptionBase, mockCleanUpRelatedObjects);
	}

	private void assertCleanUpRelatedObjects(final boolean mockPrincipal, final boolean mockSubscriptionBase,
			final MockCleanUpRelatedObjects mockCleanUpRelatedObjects)
	{
		final TmaSubscriptionAccessModel subscriptionAccessModel = mockCleanUpRelatedObjects.getTmaSubscriptionAccessModel();
		final TmaSubscriptionBaseModel subscriptionBaseModel = mockCleanUpRelatedObjects.getTmaSubscriptionBaseModel();

		boolean readyToRemove = customerCleanupHook.isSubscriptionBaseReadyToRemove(subscriptionBaseModel, subscriptionAccessModel);

		if (mockPrincipal && mockSubscriptionBase)
		{
			assertTrue("subscriptionBaseModel was ready to remove:", readyToRemove);
		}
		else
		{
			assertFalse("subscriptioBaseModel wasn't ready to remove: ", readyToRemove);
		}

		readyToRemove = customerCleanupHook.isSubscriptionBaseReadyToRemove(subscriptionBaseModel, createSubscriptionAccess());
		assertFalse("subscriptionBaseModel is Not ready to remove:", readyToRemove);

		if (mockPrincipal && mockSubscriptionBase)
		{
			verifyCleanUpRelatedObjects(mockCleanUpRelatedObjects);

		}
	}

	private void givenCleanUpRelatedObjects(final boolean mockPrincipal, final boolean mockSubscriptionBase,
			final MockCleanUpRelatedObjects mockCleanUpRelatedObjects)
	{
		final TmaSubscriptionAccessModel subscriptionAccessModel = mockCleanUpRelatedObjects.getTmaSubscriptionAccessModel();
		final CustomerModel customerModel = mockCleanUpRelatedObjects.getCustomerModel();
		final TmaSubscriptionBaseModel subscriptionBaseModel = mockCleanUpRelatedObjects.getTmaSubscriptionBaseModel();

		if (mockPrincipal)
		{
			given(subscriptionAccessModel.getPrincipal()).willReturn(customerModel);
		}
		else
		{
			given(subscriptionAccessModel.getPrincipal()).willReturn(null);
		}
		if (mockSubscriptionBase)
		{
			given(subscriptionAccessModel.getSubscriptionBase()).willReturn(subscriptionBaseModel);
		}
		else
		{
			given(subscriptionAccessModel.getSubscriptionBase()).willReturn(null);
		}
	}

	private void verifyCleanUpRelatedObjects(final MockCleanUpRelatedObjects mockCleanUpRelatedObjects)
	{
		final TmaSubscriptionAccessModel subscriptionAccessModel = mockCleanUpRelatedObjects.getTmaSubscriptionAccessModel();
		final CustomerModel customerModel = mockCleanUpRelatedObjects.getCustomerModel();
		final TmaBillingAccountModel billingAccountModel = mockCleanUpRelatedObjects.getTmaBillingAccountModel();
		final TmaSubscribedProductModel subscribedProductModel = mockCleanUpRelatedObjects.getTmaSubscribedProductModel();
		final TmaAverageServiceUsageModel averageServiceUsageModel = mockCleanUpRelatedObjects.getAverageServiceUsageModel();
		final PK subscriptionAccessPK = mockCleanUpRelatedObjects.getSubscriptionAccessPK();
		final PK billingAccountModelPK = mockCleanUpRelatedObjects.getTmaBillingAccountModelPK();
		final PK subscribedProductModelPK = mockCleanUpRelatedObjects.getTmaSubscribedProductModelPK();
		final PK averageServiceUsagePK = mockCleanUpRelatedObjects.getAverageServiceUsagePK();

		customerCleanupHook.cleanupRelatedObjects(customerModel);
		verify(modelService).remove(subscriptionAccessModel.getPk());
		verify(modelService).remove(billingAccountModel.getPk());
		verify(modelService).remove(averageServiceUsageModel.getPk());
		verify(modelService).remove(subscribedProductModel.getPk());
		verify(writeAuditRecordsDAO).removeAuditRecordsForType(TmaSubscriptionAccessModel._TYPECODE, subscriptionAccessPK);
		verify(writeAuditRecordsDAO).removeAuditRecordsForType(TmaBillingAccountModel._TYPECODE, billingAccountModelPK);
		verify(writeAuditRecordsDAO).removeAuditRecordsForType(TmaAverageServiceUsageModel._TYPECODE, averageServiceUsagePK);
		verify(writeAuditRecordsDAO).removeAuditRecordsForType(TmaSubscribedProductModel._TYPECODE, subscribedProductModelPK);
	}

	private TmaSubscriptionAccessModel createSubscriptionAccess()
	{
		final TmaSubscriptionAccessModel subscriptionAccessModel = new TmaSubscriptionAccessModel();
		final CustomerModel customerModel = new CustomerModel();
		customerModel.setCustomerID(CUSTOMER_UID);
		final TmaSubscriptionBaseModel subscriptionBaseModel = new TmaSubscriptionBaseModel();
		subscriptionBaseModel.setBillingSystemId(BILLING_SYSTEM_ID);
		subscriptionBaseModel.setSubscriberIdentity(SUBSCRIBER_IDENTITY);
		subscriptionAccessModel.setPrincipal(customerModel);
		subscriptionAccessModel.setSubscriptionBase(subscriptionBaseModel);
		return subscriptionAccessModel;
	}

	private class MockCleanUpRelatedObjects
	{
		private CustomerModel customerModel;
		private TmaAverageServiceUsageModel averageServiceUsageModel;
		private PK averageServiceUsagePK;
		private TmaSubscriptionAccessModel subscriptionAccessModel;
		private PK subscriptionAccessPK;
		private TmaSubscriptionBaseModel subscriptionBaseModel;
		private PK subscriptionBaseModelPK;
		private TmaBillingAccountModel billingAccountModel;
		private PK billingAccountModelPK;
		private TmaSubscribedProductModel subscribedProductModel;
		private PK subscribedProductModelPK;

		protected CustomerModel getCustomerModel()
		{
			return customerModel;
		}

		public TmaAverageServiceUsageModel getAverageServiceUsageModel()
		{
			return averageServiceUsageModel;
		}

		public PK getAverageServiceUsagePK()
		{
			return averageServiceUsagePK;
		}

		protected TmaSubscriptionAccessModel getTmaSubscriptionAccessModel()
		{
			return subscriptionAccessModel;
		}

		protected PK getSubscriptionAccessPK()
		{
			return subscriptionAccessPK;
		}

		protected TmaSubscriptionBaseModel getTmaSubscriptionBaseModel()
		{
			return subscriptionBaseModel;
		}

		protected PK getTmaSubscriptionBaseModelPK()
		{
			return subscriptionBaseModelPK;
		}

		protected TmaBillingAccountModel getTmaBillingAccountModel()
		{
			return billingAccountModel;
		}

		protected PK getTmaBillingAccountModelPK()
		{
			return billingAccountModelPK;
		}

		protected TmaSubscribedProductModel getTmaSubscribedProductModel()
		{
			return subscribedProductModel;
		}

		protected PK getTmaSubscribedProductModelPK()
		{
			return subscribedProductModelPK;
		}

		public MockCleanUpRelatedObjects mockCommonValues()
		{
			customerModel = mock(CustomerModel.class);
			averageServiceUsageModel = mock(TmaAverageServiceUsageModel.class);
			averageServiceUsagePK = PK.parse(AVERAGE_SERVICE_USAGE_PK);
			final Collection<TmaAverageServiceUsageModel> averageServiceUsage = Collections
					.singleton(averageServiceUsageModel);
			subscriptionAccessModel = mock(TmaSubscriptionAccessModel.class);
			final Collection<TmaSubscriptionAccessModel> subscriptionAccessModelForCustomer = Collections
					.singletonList(subscriptionAccessModel);
			subscriptionAccessPK = PK.parse(SUBSCRIPTIONACCESS_PK);
			subscriptionBaseModel = mock(TmaSubscriptionBaseModel.class);
			subscriptionBaseModelPK = PK.parse(SUBSCRIPTIONBASE_PK);
			final Collection<TmaSubscriptionAccessModel> subscriptionAccessModelForSubscriotionBase = Collections
					.singleton(subscriptionAccessModel);
			billingAccountModel = mock(TmaBillingAccountModel.class);
			billingAccountModelPK = PK.parse(BILLINGACCOUNT_PK);
			subscribedProductModel = mock(TmaSubscribedProductModel.class);
			final Collection<TmaSubscribedProductModel> serviceModelSet = Collections.singleton(subscribedProductModel);
			subscribedProductModelPK = PK.parse(SERVICE_PK);

			given(customerModel.getUid()).willReturn(CUSTOMER_UID);
			given(subscriptionAccessService.getSubscriptionAccessesByType(customerModel.getUid(), TmaAccessType.OWNER)).willReturn(
					(List<TmaSubscriptionAccessModel>) subscriptionAccessModelForCustomer);
			given(subscriptionAccessModel.getPk()).willReturn(subscriptionAccessPK);
			given(subscriptionAccessModel.getSubscriptionBase()).willReturn(subscriptionBaseModel);
			given(subscriptionBaseModel.getPk()).willReturn(subscriptionBaseModelPK);
			given(subscriptionBaseModel.getBillingAccount()).willReturn(billingAccountModel);
			given(subscriptionBaseModel.getSubscribedProducts()).willReturn((Set<TmaSubscribedProductModel>) serviceModelSet);
			given(subscriptionBaseModel.getSubscriptionAccesses()).willReturn(
					(Set<TmaSubscriptionAccessModel>) subscriptionAccessModelForSubscriotionBase);
			given(subscriptionBaseModel.getBillingSystemId()).willReturn(BILLING_SYSTEM_ID);
			given(subscriptionBaseModel.getSubscriberIdentity()).willReturn(SUBSCRIBER_IDENTITY);
			given(billingAccountModel.getPk()).willReturn(billingAccountModelPK);
			given(subscribedProductModel.getPk()).willReturn(subscribedProductModelPK);
			given(subscribedProductModel.getAverageServiceUsages())
					.willReturn((Set<TmaAverageServiceUsageModel>) averageServiceUsage);
			given(averageServiceUsageModel.getPk()).willReturn(averageServiceUsagePK);

			return this;
		}
	}
}
