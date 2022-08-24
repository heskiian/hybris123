/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcofacades.user.impl.TmaDefaultCustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


/**
 * test class for TmaSubscriptionBaseValidator that validates instances of {@link TmaSubscriptionBaseData}.
 *
 * @since 1907
 *
 */
@UnitTest
public class TmaSubscriptionBaseValidatorTest
{
	private TmaSubscriptionBaseValidator tmaSubscriptionBaseValidator;
	private TmaSubscriptionBaseData tmaSubscriptionBaseData;
	private Errors errors;
	List<TmaSubscriptionBaseData> selectedSubscriptions;
	List<TmaSubscriptionBaseData> validSubscriptionsForCustomer;
	@Mock
	private TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;
	@Mock
	private TmaDefaultCustomerFacade tmaDefaultCustomerFacade;
	@Mock
	private CustomerData customerData;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		tmaSubscriptionBaseValidator = new TmaSubscriptionBaseValidator(tmaSubscriptionBaseFacade,tmaDefaultCustomerFacade);
		selectedSubscriptions = new ArrayList<>();
		validSubscriptionsForCustomer = new ArrayList<>();
		BDDMockito.given(customerData.getCustomerId()).willReturn("test_user");
		BDDMockito.given(tmaDefaultCustomerFacade.getCurrentCustomer()).willReturn(customerData);

	}

	@Test
	public void testSubscriptionBaseIsEmpty()
	{
		errors = new BeanPropertyBindingResult(tmaSubscriptionBaseData, "tmaSubscriptionBaseData");
		tmaSubscriptionBaseValidator.validate(selectedSubscriptions, errors);
		assertEquals(1, errors.getErrorCount());
	}

	@Test
	public void testSubscriptionBaseIsValid()
	{
		tmaSubscriptionBaseData = new TmaSubscriptionBaseData();
		tmaSubscriptionBaseData.setBillingSystemId("IN");
		tmaSubscriptionBaseData.setSubscriberIdentity("test_0040123333333");
		selectedSubscriptions.add(tmaSubscriptionBaseData);
		validSubscriptionsForCustomer.add(tmaSubscriptionBaseData);
		given(tmaSubscriptionBaseFacade.getSubscriptionBaseForSubscriberIdentity("test_0040123333333", "IN"))
				.willReturn(tmaSubscriptionBaseData);
		given(tmaDefaultCustomerFacade.getValidSubscriptionDataForCustomer(selectedSubscriptions))
				.willReturn(validSubscriptionsForCustomer);
		given(tmaSubscriptionBaseFacade.isIdenticalBillAgremment(validSubscriptionsForCustomer))
		.willReturn(true);
		errors = new BeanPropertyBindingResult(tmaSubscriptionBaseData, "tmaSubscriptionBaseData");
		tmaSubscriptionBaseValidator.validate(selectedSubscriptions, errors);
		assertEquals(0, errors.getErrorCount());
	}

	@Test
	public void testSubscriptionBaseInValid()
	{
		tmaSubscriptionBaseData = new TmaSubscriptionBaseData();
		tmaSubscriptionBaseData.setBillingSystemId("IN");
		tmaSubscriptionBaseData.setSubscriberIdentity("test_0040123333333");
		selectedSubscriptions.add(tmaSubscriptionBaseData);
		given(tmaSubscriptionBaseFacade.getSubscriptionBaseForSubscriberIdentity("test_0040123333333", "IN"))
				.willReturn(tmaSubscriptionBaseData);
		given(tmaDefaultCustomerFacade.getValidSubscriptionDataForCustomer(selectedSubscriptions))
				.willReturn(validSubscriptionsForCustomer);
		errors = new BeanPropertyBindingResult(tmaSubscriptionBaseData, "tmaSubscriptionBaseData");
		tmaSubscriptionBaseValidator.validate(selectedSubscriptions, errors);
		assertEquals(2, errors.getErrorCount());
	}

	@Test
	public void testSubscriptionBaseNotFound()
	{
		tmaSubscriptionBaseData = new TmaSubscriptionBaseData();
		tmaSubscriptionBaseData.setBillingSystemId("IN");
		tmaSubscriptionBaseData.setSubscriberIdentity("test_0040123333333");
		selectedSubscriptions.add(tmaSubscriptionBaseData);
		given(tmaSubscriptionBaseFacade.getSubscriptionBaseForSubscriberIdentity("test_0040123333333", "IN"))
		.willThrow(ModelNotFoundException.class);		
		errors = new BeanPropertyBindingResult(tmaSubscriptionBaseData, "tmaSubscriptionBaseData");
		tmaSubscriptionBaseValidator.validate(selectedSubscriptions, errors);
		assertEquals(3, errors.getErrorCount());
	}
}
