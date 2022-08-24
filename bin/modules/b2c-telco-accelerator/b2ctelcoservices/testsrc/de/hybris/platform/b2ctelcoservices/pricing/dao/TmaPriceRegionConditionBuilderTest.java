/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test class for @{TmaPriceRegionConditionBuilder}
 *
 * @since 1907
 */
@UnitTest
public class TmaPriceRegionConditionBuilderTest
{
	TmaPriceRegionConditionBuilder tmaPriceRegionConditionBuilder;

	@Mock
	GenericQuery query;

	TmaPriceContext priceContext;

	@Mock
	RegionModel region;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		tmaPriceRegionConditionBuilder = new TmaPriceRegionConditionBuilder();
		priceContext = new TmaPriceContext();
		final Set<RegionModel> regions = new HashSet<>();
		regions.add(region);
		priceContext.setRegions(regions);
	}

	@Test
	public void shouldApplyConditionTest()
	{
		assertThat(tmaPriceRegionConditionBuilder.shouldApplyCondition(priceContext)).isEqualTo(true);
	}

	@Test
	public void createQueryConditionsTest()
	{
		Mockito.when(query.getInitialTypeCode()).thenReturn(PriceRowModel._TYPECODE);
		final GenericCondition condition = tmaPriceRegionConditionBuilder.createQueryConditions(query, priceContext);
		assertThat(condition instanceof GenericConditionList).isTrue();
		final GenericConditionList conditionList = (GenericConditionList) condition;

		assertThat(conditionList.getConditionList()).hasSize(2);
		assertThat(conditionList.getOperator()).isEqualTo(Operator.OR);
		assertThat(conditionList.getConditionList().get(0).getOperator()).isEqualTo(Operator.IN);
		assertThat(conditionList.getConditionList().get(1).getOperator()).isEqualTo(Operator.IS_NULL);

	}
}
