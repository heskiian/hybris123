/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityPolicyService;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.DefaultTmaPolicyService;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;

import java.util.List;


/**
 * Default implementation of {@link TmaEligibilityPolicyService}.
 *
 * @since 1810
 */
public class DefaultTmaEligibilityPolicyService extends DefaultTmaPolicyService implements TmaEligibilityPolicyService
{
	@Override
	public List<TmaCompatibilityPolicyModel> getAvailableEligibilityPolicies()
	{
		return getTmaPolicyDao().findPoliciesByActionType(TmaCompatibilityPolicyActionType.ALLOW);
	}
}
