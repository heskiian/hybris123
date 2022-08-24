/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyStatementValidationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;

import static junit.framework.TestCase.assertFalse;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;


/**
 * Test for {@link DefaultTmaPolicyActionValidator}.
 *
 * @deprecated since 1911
 */
@UnitTest
@Deprecated(since = "1911", forRemoval= true)
public class DefaultTmaPolicyActionValidatorTest
{
	@Mock
	private TmaPolicyStatementValidationStrategy statementValidationStrategy;

	@InjectMocks
	private DefaultTmaPolicyActionValidator defaultTmaPolicyActionValidator;

	private TmaCompatibilityPolicyModel compatibilityPolicyModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final TmaPolicyActionModel action1 = new TmaPolicyActionModel();
		final TmaPolicyActionModel action2 = new TmaPolicyActionModel();

		final TmaPoPolicyStatementModel statementModel1 = new TmaPoPolicyStatementModel();
		statementModel1.setCode("PO_STATEMENT_1");
		final TmaSimpleProductOfferingModel productOffering = new TmaSimpleProductOfferingModel();
		statementModel1.setProductOffering(productOffering);

		final TmaPoPolicyStatementModel statementModel2 = new TmaPoPolicyStatementModel();
		statementModel2.setCode("PO_STATEMENT_2");
		final TmaSimpleProductOfferingModel productOffering2 = new TmaSimpleProductOfferingModel();
		statementModel1.setProductOffering(productOffering2);

		action1.setStatement(statementModel1);
		action2.setStatement(statementModel2);

		compatibilityPolicyModel = new TmaCompatibilityPolicyModel();
		compatibilityPolicyModel.setActions(Sets.newSet(action1, action2));
		when(statementValidationStrategy.validateStatement(eq(statementModel1),
				anyListOf(TmaPolicyContext.class))).thenReturn(true);
		when(statementValidationStrategy.validateStatement(eq(statementModel2),
				anyListOf(TmaPolicyContext.class))).thenReturn(false);


	}

	@Test
	public void testGetInvalidPolicyActions()
	{
		List<TmaPolicyActionModel> invalidActions = defaultTmaPolicyActionValidator.getInvalidPolicyActions
				(compatibilityPolicyModel, Collections.emptyList());
		assertFalse(invalidActions.isEmpty());
	}
}
