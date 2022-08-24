/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaPolicyContextBuilder;
import de.hybris.platform.b2ctelcoservices.model.TmaPoGroupPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;

import java.util.Collections;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


/**
 * Test for {@link TmaPoGroupStatementValidator}.
 */
@UnitTest
public class TmaPoGroupStatementValidatorTest
{
	@InjectMocks
	private TmaPoGroupStatementValidator tmaPoGroupStatementValidator;

	private TmaPoGroupPolicyStatementModel testStatementModel;

	private TmaProductOfferingGroupModel productOfferingGroup;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		productOfferingGroup = new TmaProductOfferingGroupModel();
		productOfferingGroup.setCode("PO_GROUP_TEST_CODE");

		testStatementModel = new TmaPoGroupPolicyStatementModel();
		testStatementModel.setMin(2);
		testStatementModel.setProductOfferingGroup(productOfferingGroup);
	}

	@Test
	public void validatePolicyStatementWithDifferentStatementType()
	{
		final TmaSimpleProductOfferingModel productOfferingModel = new TmaSimpleProductOfferingModel();
		productOfferingModel.setCode("SPO_CODE");

		final TmaPoPolicyStatementModel poPolicyStatementModel = new TmaPoPolicyStatementModel();
		poPolicyStatementModel.setMin(0);
		poPolicyStatementModel.setProductOffering(productOfferingModel);

		final boolean validationResponse = tmaPoGroupStatementValidator.validatePolicyStatement(poPolicyStatementModel,
				Collections.singletonList(new TmaPolicyContext()));
		assertTrue(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithDifferentPoGroup()
	{
		final TmaProductOfferingGroupModel productOfferingGroup = new TmaProductOfferingGroupModel();
		productOfferingGroup.setCode("PO_GROUP");

		TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder().withGroup(productOfferingGroup).withQuantity(3)
				.build();
		final boolean validationResponse = tmaPoGroupStatementValidator
				.validatePolicyStatement(testStatementModel, Lists.newArrayList(context));
		assertFalse(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithTheSamePoGroup()
	{
		TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder().withGroup(productOfferingGroup).withQuantity(3)
				.build();
		final boolean validationResponse = tmaPoGroupStatementValidator
				.validatePolicyStatement(testStatementModel, Lists.newArrayList(context));
		assertTrue(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithTheSamePoGroupButInvalidQuantity()
	{
		TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder().withGroup(productOfferingGroup).withQuantity(1)
				.build();

		final boolean validationResponse = tmaPoGroupStatementValidator
				.validatePolicyStatement(testStatementModel, Lists.newArrayList(context));
		assertFalse(validationResponse);
	}

}
