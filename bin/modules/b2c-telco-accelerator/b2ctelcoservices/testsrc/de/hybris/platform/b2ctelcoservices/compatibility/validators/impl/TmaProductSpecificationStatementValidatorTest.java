/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPsPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;

import java.util.Arrays;
import java.util.Collections;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


/**
 * Test for {@link TmaProductSpecificationStatementValidator}.
 */
@UnitTest
public class TmaProductSpecificationStatementValidatorTest
{
	private static final String TEST_ID = "testId";
	private static final String TEST_ID_2 = "testId2";
	private static final String SPO_CODE = "SPO_CODE";
	@InjectMocks
	private TmaProductSpecificationStatementValidator tmaProductSpecificationStatementValidator;

	private TmaPsPolicyStatementModel testStatementModel;

	private TmaProductSpecificationModel productSpecification;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		productSpecification = createProductSpecification(TEST_ID);

		testStatementModel = createProductSpecificationPolicyStatement(productSpecification);
	}

	@Test
	public void validatePolicyStatementWithDifferentStatementType()
	{
		final TmaSimpleProductOfferingModel productOfferingModel = new TmaSimpleProductOfferingModel();
		productOfferingModel.setCode(SPO_CODE);

		final TmaPoPolicyStatementModel poPolicyStatementModel = new TmaPoPolicyStatementModel();
		poPolicyStatementModel.setProductOffering(productOfferingModel);

		final boolean validationResponse = tmaProductSpecificationStatementValidator.validatePolicyStatement(poPolicyStatementModel,
				Collections.singletonList(new TmaPolicyContext()));
		assertTrue(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithSameProductSpecification()
	{
		final TmaSimpleProductOfferingModel productOfferingModel = createProductOffering(SPO_CODE, productSpecification);

		final TmaPolicyContext requestParam = new TmaPolicyContext();
		requestParam.setProductOfferings(Arrays.asList(productOfferingModel));
		requestParam.setQuantity(1);

		final boolean validationResponse = tmaProductSpecificationStatementValidator.validatePolicyStatement(testStatementModel,
				Lists.newArrayList(requestParam));

		assertTrue(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithDifferentProductSpecification()
	{
		final TmaProductSpecificationModel productSpecificationModel = createProductSpecification(TEST_ID_2);

		final TmaSimpleProductOfferingModel productOfferingModel = createProductOffering(SPO_CODE, productSpecificationModel);

		final TmaPolicyContext requestParam = new TmaPolicyContext();
		requestParam.setProductOfferings(Arrays.asList(productOfferingModel));
		requestParam.setQuantity(1);

		final boolean validationResponse = tmaProductSpecificationStatementValidator.validatePolicyStatement(testStatementModel,
				Lists.newArrayList(requestParam));

		assertFalse(validationResponse);
	}

	@Test
	public void validatePolicyStatementWithMultiplePS()
	{
		final TmaProductSpecificationModel productSpecificationModel = createProductSpecification(TEST_ID_2);

		final TmaSimpleProductOfferingModel productOfferingModel = createProductOffering(SPO_CODE, productSpecificationModel);
		final TmaSimpleProductOfferingModel productOfferingModel2 = createProductOffering(SPO_CODE, productSpecification);

		final TmaPolicyContext requestParam = new TmaPolicyContext();
		requestParam.setProductOfferings(Arrays.asList(productOfferingModel));
		requestParam.setQuantity(1);

		final TmaPolicyContext requestParam2 = new TmaPolicyContext();
		requestParam2.setProductOfferings(Arrays.asList(productOfferingModel2));
		requestParam2.setQuantity(1);

		final boolean validationResponse = tmaProductSpecificationStatementValidator.validatePolicyStatement(testStatementModel,
				Lists.newArrayList(requestParam, requestParam2));

		assertTrue(validationResponse);
	}

	protected TmaSimpleProductOfferingModel createProductOffering(final String code,
			final TmaProductSpecificationModel productSpecificationModel)
	{
		final TmaSimpleProductOfferingModel productOfferingModel = new TmaSimpleProductOfferingModel();
		productOfferingModel.setCode(code);
		productOfferingModel.setProductSpecification(productSpecificationModel);
		return productOfferingModel;
	}

	protected TmaProductSpecificationModel createProductSpecification(final String id)
	{
		final TmaProductSpecificationModel productSpecification = new TmaProductSpecificationModel();
		productSpecification.setId(id);
		return productSpecification;
	}

	protected TmaPsPolicyStatementModel createProductSpecificationPolicyStatement(
			final TmaProductSpecificationModel productSpecification)
	{
		final TmaPsPolicyStatementModel statementModel = new TmaPsPolicyStatementModel();
		statementModel.setProductSpecification(productSpecification);
		statementModel.setMin(1);
		return statementModel;
	}
}
