/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.impl;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.TmaPolicyActionValidator;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.TmaPolicyConditionValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;


/**
 * Test for {@link DefaultTmaCompatibilityPolicyEvaluationStrategy}.
 *
 * @deprecated since 1911
 */
@UnitTest
@Deprecated(since = "1911", forRemoval= true)
public class DefaultTmaCompatibilityPolicyEvaluationStrategyTest
{
	@Mock
	private TmaPolicyConditionValidator conditionValidator;
	@Mock
	private TmaPolicyActionValidator actionValidator;
	@InjectMocks
	private DefaultTmaCompatibilityPolicyEvaluationStrategy policyEvaluationStrategy;

	private TmaCompatibilityPolicyModel compatibilityPolicyModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		compatibilityPolicyModel = new TmaCompatibilityPolicyModel();
	}

	@Test
	public void testEvaluateCompatibilityPolicyWithValidConditionsAndValidActions()
	{
		when(conditionValidator.validatePolicyConditions(eq(compatibilityPolicyModel),
				anyListOf(TmaPolicyContext.class))).thenReturn(true);
		when(actionValidator.getInvalidPolicyActions(eq(compatibilityPolicyModel),
				anyListOf(TmaPolicyContext.class))).thenReturn(Collections.emptyList());
	}

	@Test
	public void testEvaluateCompatibilityPolicyWithValidConditionsAndInvalidActions()
	{
		final TmaPolicyActionModel action1 = new TmaPolicyActionModel();
		final TmaPolicyActionModel action2 = new TmaPolicyActionModel();

		List<TmaPolicyActionModel> policyActions = Arrays.asList(action1, action2);
		when(conditionValidator.validatePolicyConditions(eq(compatibilityPolicyModel),
				anyListOf(TmaPolicyContext.class))).thenReturn(true);
		when(actionValidator.getInvalidPolicyActions(eq(compatibilityPolicyModel),
				anyListOf(TmaPolicyContext.class))).thenReturn(policyActions);
	}

	@Test
	public void testEvaluateCompatibilityPolicyWithInvalidConditions()
	{
		when(conditionValidator.validatePolicyConditions(eq(compatibilityPolicyModel),
				anyListOf(TmaPolicyContext.class))).thenReturn(false);
	}





}
