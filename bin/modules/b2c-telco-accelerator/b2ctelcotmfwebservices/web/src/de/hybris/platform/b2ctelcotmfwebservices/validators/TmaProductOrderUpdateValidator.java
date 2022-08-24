/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.validators;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.StateType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Update order validator.
 *
 * @since 2105
 */
public class TmaProductOrderUpdateValidator implements Validator
{
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private static final String INVALID_STATE = "state.invalid";

	private static final String RELATED_PARTY = "relatedParty";
	private static final String STATE = "state";
	private static final String FIRST_RELATED_PARTY_ID = "relatedParty[0].id";

	private static final Collection<StateType> APPROVAL_STATE_TYPES = Arrays.asList(StateType.APPROVED, StateType.REJECTED);

	@Override
	public boolean supports(Class<?> aClass)
	{
		return ProductOrder.class.equals(aClass);
	}

	@Override
	public void validate(Object object, Errors errors)
	{
		final ProductOrder productOrder = (ProductOrder) object;

		final List<RelatedPartyRef> relatedPartyRefList = productOrder.getRelatedParty();

		validateRelatedParty(relatedPartyRefList, errors);
		validateState(productOrder.getState(), errors);
	}

	/**
	 * Validates the order state.
	 *
	 * @param state
	 * 		The state of the order
	 * @param errors
	 * 		The errors found
	 */
	protected void validateState(final StateType state, final Errors errors)
	{
		if (state == null)
		{
			errors.rejectValue(STATE, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}

		if (!APPROVAL_STATE_TYPES.contains(state))
		{
			errors.rejectValue(STATE, INVALID_STATE);
		}
	}

	/**
	 * Validates the related party.
	 *
	 * @param relatedPartyRefList
	 * 		Related party reference list
	 * @param errors
	 * 		The errors found
	 */
	protected void validateRelatedParty(final List<RelatedPartyRef> relatedPartyRefList, final Errors errors)
	{
		if (CollectionUtils.isEmpty(relatedPartyRefList))
		{
			errors.rejectValue(RELATED_PARTY, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}

		if (StringUtils.isEmpty(relatedPartyRefList.get(0).getId()))
		{
			errors.rejectValue(FIRST_RELATED_PARTY_ID, FIELD_REQUIRED_MESSAGE_ID);
		}
	}
}
