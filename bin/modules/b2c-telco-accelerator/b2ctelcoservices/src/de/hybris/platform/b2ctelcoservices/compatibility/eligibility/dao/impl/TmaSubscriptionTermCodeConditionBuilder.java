/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityContextService;
import de.hybris.platform.b2ctelcoservices.daos.TmaGenericConditionBuilder;
import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;
import de.hybris.platform.b2ctelcoservices.model.TmaEligibilityContextModel;
import de.hybris.platform.core.*;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Search query condition builder for {@link SubscriptionTermModel#ID} based on received {@link TmaEligibilityContext}s.
 *
 * @since 1810
 */
public class TmaSubscriptionTermCodeConditionBuilder implements TmaGenericConditionBuilder
{
	private TmaEligibilityContextService tmaEligibilityContextService;

	@Override
	public GenericConditionList buildQueryConditions(GenericQuery query, Object param)
	{
		if (!getTmaEligibilityContextService().shouldApplyEligibility())
		{
			return GenericConditionList.createConditionList();
		}

		final Set<TmaEligibilityContextModel> eligibilityContexts = getTmaEligibilityContextService().extractEligibilityContext();
		if (CollectionUtils.isEmpty(eligibilityContexts))
		{
			return GenericConditionList.createConditionList();
		}

		final List<GenericCondition> termCodesConditions = new ArrayList<>();
		eligibilityContexts.forEach(eligibilityContext ->
		{
			final GenericSearchField termCodeSearchField = new GenericSearchField(SubscriptionTermModel._TYPECODE,
					SubscriptionTermModel.ID);
			termCodesConditions.add(buildConditionForFieldWith(termCodeSearchField, eligibilityContext.getTermCodes()));
		});

		return GenericCondition.or(termCodesConditions);
	}

	protected GenericCondition buildConditionForFieldWith(final GenericSearchField searchField, final List<String> termCodes)
	{
		if (CollectionUtils.isNotEmpty(termCodes))
		{
			return GenericCondition
					.createConditionForValueComparison(searchField, Operator.IN, termCodes, searchField.getQualifier());
		}

		return GenericCondition.createIsNotNullCondition(searchField);
	}

	public TmaEligibilityContextService getTmaEligibilityContextService()
	{
		return tmaEligibilityContextService;
	}

	@Required
	public void setTmaEligibilityContextService(
			TmaEligibilityContextService tmaEligibilityContextService)
	{
		this.tmaEligibilityContextService = tmaEligibilityContextService;
	}
}
