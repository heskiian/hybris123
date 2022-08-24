/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaPoChecklistPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;



/**
 * Implementation of {@link AbstractTmaPolicyStatementValidator} responsible for validating
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaPoChecklistPolicyStatementModel}
 *
 * @since 1911
 */
public class TmaPoChecklistPolicyStatementValidator extends AbstractTmaPolicyStatementValidator
{

	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaPoChecklistPolicyStatementModel))
		{
			return contexts;
		}

		final TmaPoChecklistPolicyStatementModel policyStatement = (TmaPoChecklistPolicyStatementModel) statement;
		final Set<TmaProductOfferingModel> productOfferings = policyStatement.getProductOfferings();

		if (CollectionUtils.isEmpty(productOfferings))
		{
			return contexts;
		}

		final Set<TmaPolicyContext> appliedPolicyContext = new HashSet<>();

		contexts.forEach(context -> {
			if (CollectionUtils.isNotEmpty(context.getProductOfferings()))
			{
				context.getProductOfferings().forEach(productOffering -> {
					if (productOfferings.contains(productOffering))
					{
						appliedPolicyContext.add(context);
					}
				});
			}
		});

		return new ArrayList<>(appliedPolicyContext);

	}
}
