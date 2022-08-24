/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaPoGroupPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Implementation of {@link AbstractTmaPolicyStatementValidator} responsible for validating
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaPoGroupPolicyStatementModel}
 *
 * @since 6.7
 */
public class TmaPoGroupStatementValidator extends AbstractTmaPolicyStatementValidator
{

	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		{
			if (!(statement instanceof TmaPoGroupPolicyStatementModel))
			{
				return contexts;
			}
			final TmaProductOfferingGroupModel group = ((TmaPoGroupPolicyStatementModel) statement).getProductOfferingGroup();
			if (group == null)
			{
				return contexts;
			}
			final List<TmaPolicyContext> poGroupRequestParams = contexts.stream()
					.filter(param -> group.equals(param.getGroup()))
					.collect(Collectors.toList());

			if (poGroupRequestParams.isEmpty() && statement.getMin() == 0)
			{
				return contexts;
			}

			final int quantity = poGroupRequestParams.stream().mapToInt(TmaPolicyContext::getQuantity).sum();

			if (checkQuantity(quantity, statement.getMin(), statement.getMax()))
			{
				return poGroupRequestParams;
			}
			return Collections.emptyList();
		}
	}

}

