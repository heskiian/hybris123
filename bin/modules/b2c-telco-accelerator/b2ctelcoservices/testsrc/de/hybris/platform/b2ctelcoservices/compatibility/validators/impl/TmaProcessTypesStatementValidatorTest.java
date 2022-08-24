/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProcessTypesPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


/**
 * Test for {@link TmaProcessTypesStatementValidator}.
 */
@UnitTest
public class TmaProcessTypesStatementValidatorTest
{
	@InjectMocks
	private TmaProcessTypesStatementValidator tmaProcessTypesStatementValidator;

	private TmaProcessTypesPolicyStatementModel testStatementModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		testStatementModel = createProcessTypesPolicyStatement(TmaProcessType.ACQUISITION);
	}

	@Test
	public void validatePolicyStatementWithDifferentStatementType()
	{
		final TmaSimpleProductOfferingModel productOfferingModel = new TmaSimpleProductOfferingModel();
		productOfferingModel.setCode("SPO_CODE");

		final TmaPoPolicyStatementModel poPolicyStatementModel = new TmaPoPolicyStatementModel();
		poPolicyStatementModel.setMin(0);
		poPolicyStatementModel.setProductOffering(productOfferingModel);

		final boolean validationResponse = tmaProcessTypesStatementValidator.validatePolicyStatement(poPolicyStatementModel,
				Collections.singletonList(new TmaPolicyContext()));
		assertTrue(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithDifferentProcessType()
	{
		final TmaPolicyContext requestParam = new TmaPolicyContext();
		requestParam.setProcessType(TmaProcessType.DEVICE_ONLY);

		final boolean validationResponse = tmaProcessTypesStatementValidator
				.validatePolicyStatement(testStatementModel, Lists.newArrayList(requestParam));

		assertFalse(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithSameProcessType()
	{
		final TmaPolicyContext requestParam = new TmaPolicyContext();
		requestParam.setProcessType(TmaProcessType.ACQUISITION);

		final boolean validationResponse = tmaProcessTypesStatementValidator
				.validatePolicyStatement(testStatementModel, Lists.newArrayList(requestParam));

		assertTrue(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithMultipleProcessTypes()
	{
		final TmaProcessTypesPolicyStatementModel statementModel = createProcessTypesPolicyStatement(TmaProcessType.ACQUISITION,
				TmaProcessType.DEVICE_ONLY);

		final TmaPolicyContext requestParam = new TmaPolicyContext();
		requestParam.setProcessType(TmaProcessType.ACQUISITION);

		final boolean validationResponse = tmaProcessTypesStatementValidator
				.validatePolicyStatement(statementModel, Lists.newArrayList(requestParam));

		assertTrue(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithMultipleProcessTypesOnRequest()
	{
		final TmaProcessTypesPolicyStatementModel statementModel = createProcessTypesPolicyStatement(TmaProcessType.RETENTION,
				TmaProcessType.DEVICE_ONLY);

		final TmaPolicyContext requestParam = new TmaPolicyContext();
		requestParam.setProcessType(TmaProcessType.ACQUISITION);

		final TmaPolicyContext requestParam2 = new TmaPolicyContext();
		requestParam.setProcessType(TmaProcessType.RELOCATION);

		final TmaPolicyContext requestParam3 = new TmaPolicyContext();
		requestParam.setProcessType(TmaProcessType.DEVICE_ONLY);

		final boolean validationResponse = tmaProcessTypesStatementValidator
				.validatePolicyStatement(statementModel, Lists.newArrayList(requestParam, requestParam2,requestParam3));

		assertTrue(validationResponse);
	}

	protected TmaProcessTypesPolicyStatementModel createProcessTypesPolicyStatement(TmaProcessType... processTypes)
	{
		final TmaProcessTypesPolicyStatementModel statementModel = new TmaProcessTypesPolicyStatementModel();
		statementModel.setProcesses(new HashSet<>(Arrays.asList(processTypes)));
		return statementModel;
	}
}
