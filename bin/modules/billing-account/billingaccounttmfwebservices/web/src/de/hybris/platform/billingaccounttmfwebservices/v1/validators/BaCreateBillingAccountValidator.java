/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billingaccounttmfwebservices.v1.validators;

import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for POST /billingAccount API.
 *
 * @since 2111.
 */
public class BaCreateBillingAccountValidator implements Validator
{
	private static final String BILLING_ACCOUNT_RELATED_PARTY = "relatedParty";
	private static final String BILLING_ACCOUNT_ID = "id";
	private static final String FIELD_REQUIRED_MESSAGE = "field.required";
	private static final String BA_ACCOUNT_EXISTS_MESSAGE = "billingAccount.id.exists";

	private BaGenericService baGenericService;

	public BaCreateBillingAccountValidator(final BaGenericService baGenericService)
	{
		this.baGenericService = baGenericService;
	}

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return BillingAccount.class.equals(aClass);
	}

	/**
	 * Checks that the {@link BillingAccount} has valid relatedParty
	 *
	 * @param o
	 * 		the billing account
	 * @param errors
	 * 		the errors
	 */
	@Override
	public void validate(final Object o, final Errors errors)
	{
		final BillingAccount billingAccount = (BillingAccount) o;

		if (getBaGenericService().getItem(BaBillingAccountModel._TYPECODE, billingAccount.getId()) != null)
		{
			errors.rejectValue(BILLING_ACCOUNT_ID, BA_ACCOUNT_EXISTS_MESSAGE);
		}

		if (billingAccount.getRelatedParty() == null)
		{
			errors.rejectValue(BILLING_ACCOUNT_RELATED_PARTY, FIELD_REQUIRED_MESSAGE);
		}
	}

	protected BaGenericService getBaGenericService()
	{
		return baGenericService;
	}
}
