/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyStatementValidationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityConditionEvaluatorType;
import de.hybris.platform.b2ctelcoservices.model.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;


/**
 * Unit test for {@link DefaultTmaPolicyConditionValidator}.
 */
@UnitTest
public class DefaultTmaPolicyConditionValidatorTest
{
	@Mock
	private TmaPolicyStatementValidationStrategy statementValidationStrategy;

	@InjectMocks
	private DefaultTmaPolicyConditionValidator defaultTmaPolicyConditionValidator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void validateValidPolicyForAtomicPolicyConditionsWithOr()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.OR,
				createAtomicPolicyCondition("iphone_8", true),
				createAtomicPolicyCondition("tapas_s", false));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertTrue(validationResult);
	}

	@Test
	public void validateInvalidPolicyForAtomicPolicyConditionsWithOr()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.OR,
				createAtomicPolicyCondition("iphone_8", false),
				createAtomicPolicyCondition("tapas_s", false));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertFalse(validationResult);
	}

	@Test
	public void validateValidPolicyForAtomicPolicyConditionsWithAnd()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.AND,
				createAtomicPolicyCondition("iphone_8", true),
				createAtomicPolicyCondition("tapas_s", true));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertTrue(validationResult);
	}

	@Test
	public void validateInvalidPolicyForAtomicPolicyConditionsWithAnd()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.AND,
				createAtomicPolicyCondition("iphone_8", false),
				createAtomicPolicyCondition("tapas_s", true));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertFalse(validationResult);
	}

	@Test
	public void validateValidPolicyForComposedPolicyConditionsWithOr()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.OR,
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.OR,
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("iphone_8", true),
								createAtomicPolicyCondition("tapas_s", true)),
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("iphone_x", true),
								createAtomicPolicyCondition("tapas_m", false))),
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
						createAtomicPolicyCondition("iphone_8", true),
						createAtomicPolicyCondition("tapas_m", false)));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertTrue(validationResult);
	}

	@Test
	public void validateInvalidPolicyForComposedPolicyConditionsWithOr()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.OR,
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("iphone_8", true),
								createAtomicPolicyCondition("tapas_s", true)),
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("tv_S", false),
								createAtomicPolicyCondition("movies_go", true))),
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
						createAtomicPolicyCondition("iphone_x", false),
						createAtomicPolicyCondition("tapas_m", true)));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertFalse(validationResult);
	}

	@Test
	public void validateValidPolicyForComposedPolicyConditionsWithAnd()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.AND,
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.OR,
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("iphone_8", true),
								createAtomicPolicyCondition("tapas_s", false)),
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.OR,
								createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
										createAtomicPolicyCondition("iphone_x", false),
										createAtomicPolicyCondition("tapas_m", false)),
								createAtomicPolicyCondition("sombreroL", true))),
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.OR,
						createAtomicPolicyCondition("tv_S", true),
						createAtomicPolicyCondition("tv_M", false)));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertTrue(validationResult);
	}

	@Test
	public void validateInvalidPolicyForComposedPolicyConditionsWithAnd()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.AND,
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.OR,
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("iphone_8", true),
								createAtomicPolicyCondition("tapas_s", false)),
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("iphone_x", false),
								createAtomicPolicyCondition("tapas_m", true))),
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.OR,
						createAtomicPolicyCondition("tv_S", true),
						createAtomicPolicyCondition("tv_M", false)));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertFalse(validationResult);
	}

	@Test
	public void validateValidPolicyForAtomicAndComposedPolicyConditionsWithAnd()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.AND,
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.OR,
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("iphone_8", false),
								createAtomicPolicyCondition("tapas_s", false)),
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("iphone_x", true),
								createAtomicPolicyCondition("tapas_m", true))),
				createAtomicPolicyCondition("internet_pack_smart", true));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertTrue(validationResult);
	}

	@Test
	public void validateValidPolicyForAtomicAndComposedPolicyConditionsWithOr()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = givenCompatibilityPolicyModel(
				TmaCompatibilityConditionEvaluatorType.OR,
				createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
						createCompositePolicyCondition(TmaCompatibilityConditionEvaluatorType.AND,
								createAtomicPolicyCondition("iphone_8", false),
								createAtomicPolicyCondition("tapas_s", false)),
						createAtomicPolicyCondition("internet_pack_smart", true)),
				createAtomicPolicyCondition("sombreroL", true));

		boolean validationResult = whenPolicyConditionValidatorIsCalled(compatibilityPolicy, Collections.EMPTY_LIST);
		assertTrue(validationResult);
	}


	private TmaCompatibilityPolicyModel givenCompatibilityPolicyModel(
			final TmaCompatibilityConditionEvaluatorType conditionEvaluatorType, final TmaPolicyConditionModel... conditions)
	{
		final TmaCompatibilityPolicyModel compatibilityPolicy = new TmaCompatibilityPolicyModel();
		compatibilityPolicy.setConditions(Stream.of(conditions).collect(Collectors.toSet()));
		compatibilityPolicy.setConditionsEvaluatorType(conditionEvaluatorType);
		return compatibilityPolicy;
	}

	private TmaCompositePolicyConditionModel createCompositePolicyCondition(
			final TmaCompatibilityConditionEvaluatorType conditionEvaluatorType, final TmaPolicyConditionModel... policyConditions)
	{
		final TmaCompositePolicyConditionModel compositePolicyCondition = new TmaCompositePolicyConditionModel();
		compositePolicyCondition.setChildren(Stream.of(policyConditions).collect(Collectors.toSet()));
		compositePolicyCondition.setConditionsEvaluatorType(conditionEvaluatorType);
		return compositePolicyCondition;
	}

	private TmaAtomicPolicyConditionModel createAtomicPolicyCondition(String conditionStatement, boolean isStatementValid)
	{
		final TmaAtomicPolicyConditionModel atomicPolicyCondition = new TmaAtomicPolicyConditionModel();
		atomicPolicyCondition.setStatement(createPoPolicyStatement(conditionStatement, isStatementValid));
		return atomicPolicyCondition;
	}

	private TmaPoPolicyStatementModel createPoPolicyStatement(String policyStatementCode, boolean isStatementValid)
	{
		final TmaPoPolicyStatementModel policyStatement = mock(TmaPoPolicyStatementModel.class);
		policyStatement.setCode(policyStatementCode);
		final TmaSimpleProductOfferingModel productOffering = new TmaSimpleProductOfferingModel();
		policyStatement.setProductOffering(productOffering);

		given(statementValidationStrategy.isStatementValid(eq(policyStatement), any())).willReturn(isStatementValid);

		return policyStatement;
	}

	private boolean whenPolicyConditionValidatorIsCalled(final TmaCompatibilityPolicyModel compatibilityPolicyModel,
			final List<TmaPolicyContext> productListParams)
	{
		return defaultTmaPolicyConditionValidator.validatePolicyConditions(compatibilityPolicyModel, productListParams);
	}
}
