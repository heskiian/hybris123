/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaConfigurablePscvPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Implementation of {@link AbstractTmaPolicyStatementValidator} responsible for validating
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaConfigurablePscvPolicyStatementModel}
 *
 * @since 1911
 */
public class TmaConfigurablePscvPolicyStatementValidator extends AbstractTmaPolicyStatementValidator
{

	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaConfigurablePscvPolicyStatementModel) || CollectionUtils.isEmpty(contexts))
		{
			return contexts;
		}

		final TmaConfigurablePscvPolicyStatementModel configurablePscvStatement = (TmaConfigurablePscvPolicyStatementModel) statement;

		final Set<TmaPolicyContext> applicableContexts = new HashSet<>();

		contexts.forEach(context -> {
			if (CollectionUtils.isNotEmpty(context.getProductOfferings()))
			{
				applicableContexts.addAll(getApplicableContext(context, configurablePscvStatement.getProductSpecCharacteristic()));
			}
		});
		return new ArrayList<>(applicableContexts);
	}



	private Set<TmaPolicyContext> getApplicableContext(final TmaPolicyContext tmaPolicyContext,
			final TmaProductSpecCharacteristicModel statementPsc)
	{
		final Set<TmaPolicyContext> applicableContexts = new HashSet<>();

		final List<TmaProductOfferingModel> productOfferings = tmaPolicyContext.getProductOfferings();

		productOfferings.forEach(productOffering -> {
			final TmaProductSpecificationModel productSpecification = productOffering.getProductSpecification();
			if (productSpecification != null && CollectionUtils.isNotEmpty(productSpecification.getProductSpecCharacteristics()))
			{
				if (productSpecification.getProductSpecCharacteristics().contains(statementPsc))
				{
					applicableContexts.add(tmaPolicyContext);
				}
			}
		});
		return applicableContexts;
	}
}
