/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;


import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaPolicyContextBuilder;
import de.hybris.platform.b2ctelcoservices.model.TmaPoGroupPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;

import java.util.Arrays;
import java.util.Collections;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


/**
 * Test for {@link TmaPoStatementValidator}.
 */
@UnitTest
public class TmaPoStatementValidatorTest
{
	@InjectMocks
	private TmaPoStatementValidator tmaPoStatementValidator;

	private TmaPoPolicyStatementModel testStatementModel;

	private TmaSimpleProductOfferingModel productOfferingModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		productOfferingModel = new TmaSimpleProductOfferingModel();
		productOfferingModel.setCode("PO_TEST_CODE");

		testStatementModel = new TmaPoPolicyStatementModel();
		testStatementModel.setMin(2);
		testStatementModel.setProductOffering(productOfferingModel);
	}

	@Test
	public void validatePolicyStatementWithDifferentStatementType()
	{
		final TmaProductOfferingGroupModel productOfferingGroup = new TmaProductOfferingGroupModel();
		productOfferingGroup.setCode("PO_GROUP_TEST_CODE");

		final TmaPoGroupPolicyStatementModel groupPolicyStatementModel = new TmaPoGroupPolicyStatementModel();
		groupPolicyStatementModel.setMin(0);
		groupPolicyStatementModel.setProductOfferingGroup(productOfferingGroup);

		final boolean validationResponse = tmaPoStatementValidator.validatePolicyStatement(groupPolicyStatementModel,
				Collections.singletonList(new TmaPolicyContext()));
		assertTrue(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithDifferentPo()
	{
		final TmaSimpleProductOfferingModel productOfferingModel = new TmaSimpleProductOfferingModel();
		productOfferingModel.setCode("SPO_CODE");

		final TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder()
				.withProductOfferings(Arrays.asList(productOfferingModel)).withQuantity(3).build();
		final boolean validationResponse = tmaPoStatementValidator.validatePolicyStatement(testStatementModel,
				Lists.newArrayList(context));
		assertFalse(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithTheSamePo()
	{
		final TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder()
				.withProductOfferings(Arrays.asList(productOfferingModel)).withQuantity(3).build();
		final boolean validationResponse = tmaPoStatementValidator.validatePolicyStatement(testStatementModel,
				Lists.newArrayList(context));
		assertTrue(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithTheSamePoButInvalidQuantity()
	{
		final TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder()
				.withProductOfferings(Arrays.asList(productOfferingModel)).withQuantity(1).build();
		final boolean validationResponse = tmaPoStatementValidator.validatePolicyStatement(testStatementModel,
				Lists.newArrayList(context));
		assertFalse(validationResponse);
	}
}
