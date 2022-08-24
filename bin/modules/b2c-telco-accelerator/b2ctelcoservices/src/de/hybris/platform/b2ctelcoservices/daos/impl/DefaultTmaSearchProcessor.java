/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaGenericConditionBuilder;
import de.hybris.platform.b2ctelcoservices.daos.TmaGenericSearchProcessor;
import de.hybris.platform.b2ctelcoservices.daos.TmaSearchQueryException;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link TmaGenericSearchProcessor}.
 *
 * @param <T>
 * 		parameter based on which the search query will be enhanced.
 * @since 1810
 */
public class DefaultTmaSearchProcessor<T> implements TmaGenericSearchProcessor<T>
{
	private List<TmaGenericConditionBuilder> conditionBuilders;

	@Override
	public void enhanceQuery(GenericQuery query) throws TmaSearchQueryException
	{
		enhanceQuery(query, null);
	}

	@Override
	public void enhanceQuery(GenericQuery query, T param) throws TmaSearchQueryException
	{
		final List<GenericCondition> conditions = new ArrayList<>();
		for (final TmaGenericConditionBuilder builder : getConditionBuilders())
		{
			final GenericConditionList builderCondition = builder.buildQueryConditions(query, param);
			if (!builderCondition.isEmpty())
			{
				conditions.add(builderCondition);
			}
		}

		query.addCondition(GenericConditionList.and(conditions));
	}

	protected List<TmaGenericConditionBuilder> getConditionBuilders()
	{
		return conditionBuilders;
	}

	@Required
	public void setConditionBuilders(List<TmaGenericConditionBuilder> conditionBuilders)
	{
		this.conditionBuilders = conditionBuilders;
	}

}
