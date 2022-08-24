/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Implementation of {@link AbstractTmaPolicyStatementValidator} responsible for validating
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel}
 *
 * @since 6.7
 */
public class TmaPoStatementValidator extends AbstractTmaPolicyStatementValidator
{

	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaPoPolicyStatementModel))
		{
			return contexts;
		}

		final Set<TmaPolicyContext> poRequestParam = new HashSet<>();

		contexts.forEach(context -> {
			if (CollectionUtils.isNotEmpty(context.getProductOfferings())
					&& context.getProductOfferings().contains(((TmaPoPolicyStatementModel) statement).getProductOffering()) && !(context.isSubscribed()))
			{
				poRequestParam.add(context);
			}
		});


		if (poRequestParam.isEmpty() && statement.getMin() == 0)
		{
			return contexts;
		}

		final int quantity = poRequestParam.stream().mapToInt(TmaPolicyContext::getQuantity).sum();

		if (checkQuantity(quantity, statement.getMin(), statement.getMax()))
		{
			return new ArrayList<>(poRequestParam);
		}
		return Collections.emptyList();
	}
}
