/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositePolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test for {@link DefaultTmaPolicyService}.
 *
 * @since 1810
 */
@UnitTest
public class DefaultTmaPolicyServiceUnitTest
{
	@InjectMocks
	private DefaultTmaPolicyService defaultTmaPolicyService;
	@Mock
	private ModelService modelService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddNewChildConditionToParentCondition()
	{
		final TmaCompositePolicyConditionModel parentCondition = new TmaCompositePolicyConditionModel();
		parentCondition.setCode("parentCondition");
		parentCondition.setChildren(new HashSet<>());

		final TmaPolicyConditionModel childCondition = new TmaPolicyConditionModel();
		childCondition.setCode("childCondition");
		childCondition.setParents(new HashSet<>());

		defaultTmaPolicyService.addNewCondition(parentCondition, childCondition);

		Assert.assertTrue(parentCondition.getChildren().size() == 1);
		Assert.assertTrue(parentCondition.getChildren().contains(childCondition));
	}

	@Test
	public void testAddNewChildConditionToPolicy()
	{
		final TmaCompatibilityPolicyModel policyModel = new TmaCompatibilityPolicyModel();
		policyModel.setCode("parentPolicy");
		policyModel.setConditions(new HashSet<>());

		final TmaPolicyConditionModel childCondition = new TmaPolicyConditionModel();
		childCondition.setCode("childCondition");
		childCondition.setPolicies(new HashSet<>());

		defaultTmaPolicyService.addNewCondition(policyModel, childCondition);

		Assert.assertTrue(policyModel.getConditions().size() == 1);
		Assert.assertTrue(policyModel.getConditions().contains(childCondition));
	}

	@Test
	public void testRemoveChildConditionFromParentCondition()
	{
		final TmaCompositePolicyConditionModel parentCondition = new TmaCompositePolicyConditionModel();
		parentCondition.setCode("parentCondition");
		final TmaPolicyConditionModel childCondition = new TmaPolicyConditionModel();
		childCondition.setCode("childCondition");
		childCondition.setParents(Collections.singleton(parentCondition));
		parentCondition.setChildren(Collections.singleton(childCondition));

		defaultTmaPolicyService.removeCondition(parentCondition, childCondition);

		Assert.assertTrue(parentCondition.getChildren().size() == 0);
		Assert.assertFalse(parentCondition.getChildren().contains(childCondition));
	}
}
