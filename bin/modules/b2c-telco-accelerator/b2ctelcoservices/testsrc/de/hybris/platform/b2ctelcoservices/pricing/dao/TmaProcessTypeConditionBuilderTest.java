/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.Operator;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Test class for @{TmaProcessTypeConditionBuilder}
 *
 * @since 1907
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class TmaProcessTypeConditionBuilderTest
{
	@InjectMocks
	TmaProcessTypeConditionBuilder tmaProcessTypeConditionBuilde = new TmaProcessTypeConditionBuilder();
	@Mock
	GenericQuery query;

	TmaPriceContext priceContext = new TmaPriceContext();

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		final Set<TmaProcessType> processTypes = new HashSet<TmaProcessType>();
		processTypes.add(TmaProcessType.MNP);
		priceContext.setProcessTypes(processTypes);
	}

	@Test
	public void createQueryConditionsTest()
	{
		Mockito.when(query.getInitialTypeCode()).thenReturn(PriceRowModel._TYPECODE);
		final GenericCondition condition = tmaProcessTypeConditionBuilde.createQueryConditions(query, priceContext);
		assertThat(condition instanceof GenericConditionList).isTrue();
		final GenericConditionList conditionList = (GenericConditionList) condition;

		assertThat(conditionList.getConditionList()).hasSize(2);
		assertThat(conditionList.getOperator()).isEqualTo(Operator.OR);
		assertThat(conditionList.getConditionList().get(0).getOperator()).isEqualTo(Operator.IN);
		assertThat(conditionList.getConditionList().get(1).getOperator()).isEqualTo(Operator.IS_NULL);

	}
}
