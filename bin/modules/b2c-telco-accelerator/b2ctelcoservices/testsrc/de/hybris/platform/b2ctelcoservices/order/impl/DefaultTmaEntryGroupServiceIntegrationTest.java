/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.impl.DefaultEntryGroupServiceIntegrationTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class DefaultTmaEntryGroupServiceIntegrationTest extends DefaultEntryGroupServiceIntegrationTest
{
	@Resource
	private DefaultTmaEntryGroupService defaultTmaEntryGroupService;

	private AbstractOrderModel abstractOrder;

	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		abstractOrder = createAbstractOrderWithEntries();
	}

	@Test
	public void getEntriesFrom_nullAbstractOrder()
	{
		final EntryGroup entryGroup = entryGroup(1);
		assertTrue(CollectionUtils.isEmpty(defaultTmaEntryGroupService.getEntriesFrom(null, entryGroup)));
	}

	@Test
	public void getEntriesFrom_nullEntryGroup()
	{
		assertTrue(CollectionUtils.isEmpty(defaultTmaEntryGroupService.getEntriesFrom(abstractOrder, null)));
	}

	@Test
	public void getEntriesFrom_groupWithoutMatchingEntries()
	{
		final EntryGroup entryGroup = entryGroup(3);
		assertTrue(CollectionUtils.isEmpty(defaultTmaEntryGroupService.getEntriesFrom(abstractOrder, entryGroup)));
	}

	@Test
	public void getEntriesFrom_groupWithMatchingEntries()
	{
		final EntryGroup entryGroup = entryGroup(1);
		final List<AbstractOrderEntryModel> entries = defaultTmaEntryGroupService.getEntriesFrom(abstractOrder, entryGroup);

		final List<Integer> entryNumbers = entries.stream().map((AbstractOrderEntryModel::getEntryNumber))
				.collect(Collectors.toList());

		assertEquals(2, entries.size());
		assertTrue(entryNumbers.contains(0));
		assertTrue(entryNumbers.contains(1));
	}

	private AbstractOrderModel createAbstractOrderWithEntries()
	{
		final AbstractOrderModel abstractOrder = new AbstractOrderModel();
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();

		entries.add(createAbstractOrderEntry(0, 1));
		entries.add(createAbstractOrderEntry(1, 1));
		entries.add(createAbstractOrderEntry(2, 2));
		abstractOrder.setEntries(entries);

		return abstractOrder;
	}

	private AbstractOrderEntryModel createAbstractOrderEntry(final Integer entryNumber, final Integer entryGroupNumber)
	{
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setEntryNumber(entryNumber);
		entry.setEntryGroupNumbers(new HashSet<>(Collections.singletonList(entryGroupNumber)));

		return entry;
	}
}
