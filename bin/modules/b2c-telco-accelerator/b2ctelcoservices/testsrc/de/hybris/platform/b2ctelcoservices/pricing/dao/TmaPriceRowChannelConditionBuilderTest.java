/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaSearchQueryException;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericValueCondition;
import de.hybris.platform.core.Operator;
import de.hybris.platform.europe1.channel.strategies.RetrieveChannelStrategy;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.jalo.JaloSession;

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
 * Test class for @{TmaPriceRowChannelConditionBuilder}
 *
 * @since 1907
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class TmaPriceRowChannelConditionBuilderTest
{
	@InjectMocks
	TmaPriceRowChannelConditionBuilder priceRowChannelConditionBuilder = new TmaPriceRowChannelConditionBuilder();

	@Mock
	private RetrieveChannelStrategy retrieveChannelStrategy;

	GenericQuery query;
	TmaPriceContext priceContext;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void createQueryConditionsTest()
	{
		final TmaPriceContext parameter = new TmaPriceContext();
		priceRowChannelConditionBuilder.shouldApplyCondition(parameter);
		Assert.assertNotNull("Should return cart tmaPriceContext", parameter);
	}

	@Test
	public void shouldApplyConditionTest() throws TmaSearchQueryException
	{
		Mockito.when(retrieveChannelStrategy.getChannel(JaloSession.getCurrentSession().getSessionContext()))
				.thenReturn(PriceRowChannel.DESKTOP);
		final GenericCondition condition = priceRowChannelConditionBuilder.createQueryConditions(query, priceContext);
		assertThat(condition instanceof GenericConditionList).isTrue();
		final GenericConditionList conditionList = (GenericConditionList) condition;

		assertThat(conditionList.getConditionList()).hasSize(2);
		assertThat(conditionList.getOperator()).isEqualTo(Operator.OR);
		assertThat(conditionList.getConditionList().get(0).getOperator()).isEqualTo(Operator.EQUAL);
		assertThat(((GenericValueCondition) conditionList.getConditionList().get(0)).getValue()).isEqualTo(PriceRowChannel.DESKTOP);
	}
}
