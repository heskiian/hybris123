/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPsPolicyStatementModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Implementation of {@link AbstractTmaPolicyStatementValidator} responsible for validating
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaPsPolicyStatementModel}
 *
 * @since 1907
 */
public class TmaProductSpecificationStatementValidator extends AbstractTmaPolicyStatementValidator
{

	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaPsPolicyStatementModel))
		{
			return contexts;
		}

		final Set<TmaPolicyContext> applicableRequestParams = new HashSet<>();

		contexts.forEach(context ->
		{
			if (CollectionUtils.isNotEmpty(context.getProductOfferings()))
			{
				context.getProductOfferings().forEach(productOffering ->
				{
					if (productOffering.getProductSpecification() != null && productOffering.getProductSpecification()
							.equals((((TmaPsPolicyStatementModel) statement).getProductSpecification())))
					{
						applicableRequestParams.add(context);
					}
				});
			}
		});

		if (applicableRequestParams.isEmpty() && statement.getMin() == 0)
		{
			return contexts;
		}

		final int quantity = applicableRequestParams.stream().mapToInt(TmaPolicyContext::getQuantity).sum();

		if (checkQuantity(quantity, statement.getMin(), statement.getMax()))
		{
			return new ArrayList<>(applicableRequestParams);
		}
		return Collections.emptyList();
	}
}
