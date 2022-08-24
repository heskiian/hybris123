/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPsChecklistPolicyStatementModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Implementation of {@link AbstractTmaPolicyStatementValidator} responsible for validating
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaPsChecklistPolicyStatementModel}
 *
 * @since 1911
 */
public class TmaPsChecklistPolicyStatementValidator extends AbstractTmaPolicyStatementValidator
{
	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaPsChecklistPolicyStatementModel))
		{
			return contexts;
		}

		final Set<TmaPolicyContext> applicableContexts = new HashSet<>();
		final TmaPsChecklistPolicyStatementModel policyStatement = (TmaPsChecklistPolicyStatementModel) statement;
		final Set<TmaProductSpecificationModel> productSpecifications = policyStatement.getProductSpecifications();

		if (CollectionUtils.isEmpty(productSpecifications))
		{
			return contexts;
		}

		contexts.forEach(context ->
		{
			updateApplicableContext(context, productSpecifications, applicableContexts);
		});

		return new ArrayList<>(applicableContexts);
	}

	private void updateApplicableContext(final TmaPolicyContext context,
			final Set<TmaProductSpecificationModel> productSpecifications,
			final Set<TmaPolicyContext> applicableContexts)
	{
		if (CollectionUtils.isNotEmpty(context.getProductOfferings()))
		{
			context.getProductOfferings().forEach(productOffering ->
			{
				if (productOffering.getProductSpecification() != null
						&& productSpecifications.contains(productOffering.getProductSpecification()))
				{
					applicableContexts.add(context);
				}
			});
		}
	}
}
