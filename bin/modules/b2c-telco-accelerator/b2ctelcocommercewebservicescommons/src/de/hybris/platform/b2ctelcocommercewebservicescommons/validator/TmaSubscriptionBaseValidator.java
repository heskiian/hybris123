/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcofacades.user.impl.TmaDefaultCustomerFacade;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;




/**
 * Validates instances of {@link TmaSubscriptionBaseData}.
 *
 * @since 1907
 *
 */
public class TmaSubscriptionBaseValidator implements Validator
{
	public static final String DEFAULT_MESSAGE = "Invalid subscriberIdentities";
	public static final String INVALID_SUBSCRIPTION_BASE_DATA_MESSAGE = "subscriptionBaseData.invalidValue";
	public static final String INVALID_SUBSCRIPTION_BASE_FOR_USER_MESSAGE = "subscriptionBaseData.user.invalidValue";
	public static final String NON_IDENTICAL_BILL_AGREEMENT_MESSAGE = "subscriptionBaseData.nonIdenticalBillingAgreementId";
	private TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;
	private TmaDefaultCustomerFacade tmaDefaultCustomerFacade;

	public TmaSubscriptionBaseValidator(final TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade,
			final TmaDefaultCustomerFacade tmaDefaultCustomerFacade)
	{
		this.tmaSubscriptionBaseFacade = tmaSubscriptionBaseFacade;
		this.tmaDefaultCustomerFacade = tmaDefaultCustomerFacade;
	}

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return TmaSubscriptionBaseData.class.equals(clazz);
	}

	/**
	 * Validates given {@link TmaSubscriptionBaseData} validates given subscriberIdentities and the requested user is
	 * having access to the given subscriberIdentities.
	 *
	 * @param target
	 *           the {@link TmaSubscriptionBaseData}
	 * @param errors
	 *           the list of errors
	 */

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final List<TmaSubscriptionBaseData> selectedSubscriptions = (List<TmaSubscriptionBaseData>) target;
		if (CollectionUtils.isEmpty(selectedSubscriptions))
		{
			errors.reject(DEFAULT_MESSAGE, new String[]
			{ String.valueOf(selectedSubscriptions) }, DEFAULT_MESSAGE);
		}
		else
		{
			validateSubscriptionForUser(selectedSubscriptions, errors);
		}
	}

	private void validateSubscriptionForUser(final List<TmaSubscriptionBaseData> selectedSubscriptions, final Errors errors)
	{
		for (final TmaSubscriptionBaseData subscriptionBaseData : selectedSubscriptions)
		{
			try
			{
				getTmaSubscriptionBaseFacade().getSubscriptionBaseForSubscriberIdentity(subscriptionBaseData.getSubscriberIdentity(),
						subscriptionBaseData.getBillingSystemId());
			}
			catch (final ModelNotFoundException e)
			{
				errors.reject(INVALID_SUBSCRIPTION_BASE_DATA_MESSAGE, new String[]
				{ subscriptionBaseData.getSubscriberIdentity() + "__" + subscriptionBaseData.getBillingSystemId() }, DEFAULT_MESSAGE);
			}
		}
		final List<TmaSubscriptionBaseData> validSubscriptionsForCustomer = getTmaDefaultCustomerFacade()
				.getValidSubscriptionDataForCustomer(selectedSubscriptions);
		if (CollectionUtils.isEmpty(validSubscriptionsForCustomer))
		{
			errors.reject(INVALID_SUBSCRIPTION_BASE_FOR_USER_MESSAGE, new String[]
			{ getTmaDefaultCustomerFacade().getCurrentCustomer().getCustomerId() }, DEFAULT_MESSAGE);
		}
		if (!CollectionUtils.isEmpty(validSubscriptionsForCustomer)
				&& !validSubscriptionsForCustomer.containsAll(selectedSubscriptions))
		{
			errors.reject(INVALID_SUBSCRIPTION_BASE_FOR_USER_MESSAGE, new String[]
			{ getTmaDefaultCustomerFacade().getCurrentCustomer().getCustomerId() }, DEFAULT_MESSAGE);
		}
		final boolean isIdenticalBillId = getTmaSubscriptionBaseFacade().isIdenticalBillAgremment(selectedSubscriptions);
		if (!isIdenticalBillId)
		{
			errors.reject(NON_IDENTICAL_BILL_AGREEMENT_MESSAGE);
		}
	}

	protected TmaSubscriptionBaseFacade getTmaSubscriptionBaseFacade()
	{
		return tmaSubscriptionBaseFacade;
	}

	public void setTmaSubscriptionBaseFacade(final TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade)
	{
		this.tmaSubscriptionBaseFacade = tmaSubscriptionBaseFacade;
	}

	protected TmaDefaultCustomerFacade getTmaDefaultCustomerFacade()
	{
		return tmaDefaultCustomerFacade;
	}

	public void setTmaDefaultCustomerFacade(final TmaDefaultCustomerFacade tmaDefaultCustomerFacade)
	{
		this.tmaDefaultCustomerFacade = tmaDefaultCustomerFacade;
	}
}
