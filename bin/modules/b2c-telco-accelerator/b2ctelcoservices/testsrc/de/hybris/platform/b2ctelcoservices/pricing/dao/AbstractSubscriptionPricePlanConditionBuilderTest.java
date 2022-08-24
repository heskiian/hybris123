/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaSearchQueryException;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.europe1.model.PriceRowModel;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Test class for @{AbstractSubscriptionPricePlanConditionBuilder}
 *
 * @since 1907
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AbstractSubscriptionPricePlanConditionBuilderTest
{
	@InjectMocks
	AbstractSubscriptionPricePlanConditionBuilder subscriptionPricePlanConditionBuilder = new AbstractSubscriptionPricePlanConditionBuilder<T>()
	{
		@Override
		protected boolean shouldApplyCondition(final T parameter)
		{
			return false;
		}

		@Override
		protected GenericConditionList createQueryConditions(final GenericQuery query, final T param) throws TmaSearchQueryException
		{
			return null;
		}
	};
	private static final String RELATION_NAME = "relation";
	private static final String SUBSCRIPTIONPRICE_REALTION = "sup";
	private static final String TARGET_CODE = "123";
	private static final String TARGET_REALTION = "targetRelation";

	@Mock
	GenericQuery query;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void addRelationJoinToQueryTest()
	{
		Mockito.when(query.getInitialTypeCode()).thenReturn(PriceRowModel._TYPECODE);
		final GenericSearchField target = subscriptionPricePlanConditionBuilder.addRelationJoinToQuery(query, RELATION_NAME,
				SUBSCRIPTIONPRICE_REALTION, TARGET_CODE, TARGET_REALTION);
		Assert.assertNotNull(target);

	}
}
