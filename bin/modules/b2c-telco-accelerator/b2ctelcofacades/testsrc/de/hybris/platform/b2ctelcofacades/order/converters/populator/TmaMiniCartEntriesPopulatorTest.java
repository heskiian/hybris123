/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@UnitTest
public class TmaMiniCartEntriesPopulatorTest
{
	@Mock
	private AbstractPopulatingConverter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter;
	@Mock
	private CartModel cartModel;
	private CartData cartData;
	private TmaMiniCartEntriesPopulator populator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		cartData = new CartData();
		populator = new TmaMiniCartEntriesPopulator();
		populator.setTelcoOrderEntryConverter(orderEntryConverter);
	}

	@Test
	public void testPopulateNoEntries()
	{
		givenCartModelEntries(dummyEntries(0));
		whenDataIsPopulated();
		thenCartDataAllEntriesCountIs(0);
	}

	@Test
	public void testPopulateMultipleEntries()
	{
		givenCartModelEntries(dummyEntries(2));
		whenDataIsPopulated();
		thenCartDataAllEntriesCountIs(2);
	}

	@Test
	public void shouldAllowNullSource()
	{
		this.cartModel = null;
		whenDataIsPopulated();
		assertTrue(CollectionUtils.isEmpty(cartData.getEntries()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotAllowNullTarget()
	{
		this.cartData = null;
		whenDataIsPopulated();
	}

	private void givenCartModelEntries(final List<AbstractOrderEntryModel> entries)
	{
		when(cartModel.getEntries()).thenReturn(entries);
		List<OrderEntryData> orderEntries = orderModel2OrderData(entries);
		when(orderEntryConverter.convertAll(entries)).thenReturn(orderEntries);
	}

	private void whenDataIsPopulated()
	{
		populator.populate(cartModel, cartData);
	}

	private void thenCartDataAllEntriesCountIs(final int expected)
	{
		assertEquals(expected, cartData.getEntries().size());
	}

	private List<AbstractOrderEntryModel> dummyEntries(final int count)
	{
		List<AbstractOrderEntryModel> entries = new ArrayList<>();
		for (int i = 0; i < count; i++)
		{
			AbstractOrderEntryModel entryModel = Mockito.mock(AbstractOrderEntryModel.class);
			when(entryModel.getEntryNumber()).thenReturn(i);
			entries.add(entryModel);
		}
		return entries;
	}

	private List<OrderEntryData> orderModel2OrderData(final List<AbstractOrderEntryModel> entries)
	{
		List<OrderEntryData> orderEntries = new ArrayList<>();
		for (AbstractOrderEntryModel entryModel : entries)
		{
			OrderEntryData orderEntryData = new OrderEntryData();
			orderEntryData.setEntryNumber(entryModel.getEntryNumber());
			orderEntries.add(orderEntryData);
		}
		return orderEntries;
	}
}
