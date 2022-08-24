/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.ServicelayerTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class DefaultTmaAbstractOrderEntryServiceIntegrationTest extends ServicelayerTest
{
	private static final String APPLE_IPHONE_6_CODE = "Apple_iPhone_6";
	private static final String TV_S_CODE = "tv_S";
	private static final String INT_100_CODE = "int_100";
	private static final String QUAD_PLAY_CODE = "quadPlay";
	private static final String MOBILE_DEAL_CODE = "mobileDeal";

	@Resource
	private DefaultTmaAbstractOrderEntryService defaultTmaAbstractOrderEntryService;

	@Test
	public void testGetPoQuantity_nullEntries()
	{
		final TmaProductOfferingModel productOffering = createProductOffering(APPLE_IPHONE_6_CODE);
		assertEquals(0, (long) defaultTmaAbstractOrderEntryService.getPoQuantity(null, productOffering));
	}

	@Test
	public void testGetPoQuantity_emptyEntries()
	{
		final TmaProductOfferingModel productOffering = createProductOffering(APPLE_IPHONE_6_CODE);
		assertEquals(0,
				(long) defaultTmaAbstractOrderEntryService.getPoQuantity(Collections.emptyList(), productOffering));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetPoQuantity_nullPo()
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(createEntry(APPLE_IPHONE_6_CODE, (long) 1));

		defaultTmaAbstractOrderEntryService.getPoQuantity(entries, null);
	}

	@Test
	public void testGetPoQuantity_poNotInEntries()
	{
		final TmaProductOfferingModel productOffering = createProductOffering(APPLE_IPHONE_6_CODE);
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(createEntry(TV_S_CODE, (long) 1));
		entries.add(createEntry(INT_100_CODE, (long) 2));

		assertEquals(0,
				(long) defaultTmaAbstractOrderEntryService.getPoQuantity(entries, productOffering));
	}

	@Test
	public void testGetPoQuantity_poInMultipleEntries()
	{
		final TmaProductOfferingModel productOffering = createProductOffering(APPLE_IPHONE_6_CODE);
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(createEntry(APPLE_IPHONE_6_CODE, (long) 1));
		entries.add(createEntry(INT_100_CODE, (long) 1));
		entries.add(createEntry(APPLE_IPHONE_6_CODE, (long) 1));
		entries.add(createEntry(TV_S_CODE, (long) 1));
		entries.add(createEntry(APPLE_IPHONE_6_CODE, (long) 1));
		entries.add(createEntry(INT_100_CODE, (long) 1));

		assertEquals(3,
				(long) defaultTmaAbstractOrderEntryService.getPoQuantity(entries, productOffering));
	}

	@Test
	public void testGetPoQuantity_poWithQuantity()
	{
		final TmaProductOfferingModel productOffering = createProductOffering(APPLE_IPHONE_6_CODE);
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(createEntry(APPLE_IPHONE_6_CODE, (long) 4));
		entries.add(createEntry(TV_S_CODE, (long) 3));
		entries.add(createEntry(INT_100_CODE, (long) 2));

		assertEquals(4,
				(long) defaultTmaAbstractOrderEntryService.getPoQuantity(entries, productOffering));
	}

	@Test
	public void testGetPoQuantity_poInMultipleEntriesWithQuantity()
	{
		final TmaProductOfferingModel productOffering = createProductOffering(APPLE_IPHONE_6_CODE);
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(createEntry(APPLE_IPHONE_6_CODE, (long) 1));
		entries.add(createEntry(INT_100_CODE, (long) 2));
		entries.add(createEntry(APPLE_IPHONE_6_CODE, (long) 3));
		entries.add(createEntry(TV_S_CODE, (long) 4));
		entries.add(createEntry(APPLE_IPHONE_6_CODE, (long) 5));
		entries.add(createEntry(INT_100_CODE, (long) 6));

		assertEquals(9,
				(long) defaultTmaAbstractOrderEntryService.getPoQuantity(entries, productOffering));
	}

	@Test
	public void testgetRootEntry_entryWithoutMasterEntry()
	{
		final AbstractOrderEntryModel entry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		assertNull(defaultTmaAbstractOrderEntryService.getRootEntry(entry));
	}

	@Test
	public void testgetRootEntry_entryWithOneMasterLevel()
	{
		final AbstractOrderEntryModel rootEntry = createEntryWithBpo(MOBILE_DEAL_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		spoChildEntry.setMasterEntry(rootEntry);

		assertEquals(rootEntry, defaultTmaAbstractOrderEntryService.getRootEntry(spoChildEntry));
	}

	@Test
	public void testgetRootEntry_entryWithTwoMasterLevels()
	{
		final AbstractOrderEntryModel rootEntry = createEntryWithBpo(QUAD_PLAY_CODE, (long) 1);
		final AbstractOrderEntryModel bpoChildEntry = createEntryWithBpo(MOBILE_DEAL_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		spoChildEntry.setMasterEntry(bpoChildEntry);
		bpoChildEntry.setMasterEntry(rootEntry);

		assertEquals(rootEntry, defaultTmaAbstractOrderEntryService.getRootEntry(spoChildEntry));
	}

	@Test
	public void testGetSpoChildEntries_spo()
	{
		final AbstractOrderEntryModel entry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		assertTrue(CollectionUtils.isEmpty(defaultTmaAbstractOrderEntryService.getSpoChildEntries(entry)));
	}

	@Test
	public void testGetSpoChildEntries_oneChildEntry()
	{
		final AbstractOrderEntryModel rootEntry = createEntryWithBpo(MOBILE_DEAL_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		rootEntry.setChildEntries(Collections.singleton(spoChildEntry));

		spoChildEntry.setMasterEntry(rootEntry);

		final List<AbstractOrderEntryModel> childEntries = defaultTmaAbstractOrderEntryService.getSpoChildEntries(rootEntry);

		assertEquals(1, childEntries.size());
		assertTrue(childEntries.contains(spoChildEntry));
	}

	@Test
	public void testGetSpoChildEntries_multipleChildEntries()
	{
		final AbstractOrderEntryModel rootEntry = createEntryWithBpo(QUAD_PLAY_CODE, (long) 1);
		final AbstractOrderEntryModel bpoChildEntry = createEntryWithBpo(MOBILE_DEAL_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry1 = createEntry(APPLE_IPHONE_6_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry2 = createEntry(TV_S_CODE, (long) 1);

		bpoChildEntry.setChildEntries(Collections.singleton(spoChildEntry1));
		rootEntry.setChildEntries(Arrays.asList(bpoChildEntry, spoChildEntry2));

		bpoChildEntry.setMasterEntry(rootEntry);
		spoChildEntry1.setMasterEntry(bpoChildEntry);
		spoChildEntry2.setMasterEntry(rootEntry);

		final List<AbstractOrderEntryModel> childEntries = defaultTmaAbstractOrderEntryService.getSpoChildEntries(rootEntry);

		assertEquals(2, childEntries.size());
		assertTrue(childEntries.contains(spoChildEntry1));
		assertTrue(childEntries.contains(spoChildEntry2));
	}


	@Test
	public void testGetAllChildEntries_spo()
	{
		final AbstractOrderEntryModel entry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		assertTrue(CollectionUtils.isEmpty(defaultTmaAbstractOrderEntryService.getAllChildEntries(entry)));
	}

	@Test
	public void testGetAllChildEntries_oneChildEntry()
	{
		final AbstractOrderEntryModel rootEntry = createEntryWithBpo(MOBILE_DEAL_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		rootEntry.setChildEntries(Collections.singleton(spoChildEntry));

		spoChildEntry.setMasterEntry(rootEntry);

		final List<AbstractOrderEntryModel> childEntries = defaultTmaAbstractOrderEntryService.getAllChildEntries(rootEntry);

		assertEquals(1, childEntries.size());
		assertTrue(childEntries.contains(spoChildEntry));
	}

	@Test
	public void testGetAllChildEntries_multipleChildEntries()
	{
		final AbstractOrderEntryModel rootEntry = createEntryWithBpo(QUAD_PLAY_CODE, (long) 1);
		final AbstractOrderEntryModel bpoChildEntry = createEntryWithBpo(MOBILE_DEAL_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry1 = createEntry(APPLE_IPHONE_6_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry2 = createEntry(TV_S_CODE, (long) 1);

		bpoChildEntry.setChildEntries(Collections.singleton(spoChildEntry1));
		rootEntry.setChildEntries(Arrays.asList(bpoChildEntry, spoChildEntry2));

		bpoChildEntry.setMasterEntry(rootEntry);
		spoChildEntry1.setMasterEntry(bpoChildEntry);
		spoChildEntry2.setMasterEntry(rootEntry);

		final List<AbstractOrderEntryModel> childEntries = defaultTmaAbstractOrderEntryService.getAllChildEntries(rootEntry);

		assertEquals(3, childEntries.size());
		assertTrue(childEntries.contains(bpoChildEntry));
		assertTrue(childEntries.contains(spoChildEntry1));
		assertTrue(childEntries.contains(spoChildEntry2));
	}


	@Test
	public void testGetBpoParentEntries_entryWithoutParentBpos()
	{
		final AbstractOrderEntryModel entry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		assertTrue(CollectionUtils.isEmpty(defaultTmaAbstractOrderEntryService.getBpoParentEntries(entry)));
	}

	@Test
	public void testGetBpoParentEntries_entryWithOneParentBpo()
	{
		final AbstractOrderEntryModel rootEntry = createEntryWithBpo(MOBILE_DEAL_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		spoChildEntry.setMasterEntry(rootEntry);

		final List<AbstractOrderEntryModel> parentEntries = defaultTmaAbstractOrderEntryService.getBpoParentEntries(spoChildEntry);

		assertEquals(1, parentEntries.size());
		assertTrue(parentEntries.contains(rootEntry));
	}

	@Test
	public void testGetBpoParentEntries_entryWithMultipleParentBpos()
	{
		final AbstractOrderEntryModel rootEntry = createEntryWithBpo(QUAD_PLAY_CODE, (long) 1);
		final AbstractOrderEntryModel bpoChildEntry = createEntryWithBpo(MOBILE_DEAL_CODE, (long) 1);
		final AbstractOrderEntryModel spoChildEntry = createEntry(APPLE_IPHONE_6_CODE, (long) 1);

		spoChildEntry.setMasterEntry(bpoChildEntry);
		bpoChildEntry.setMasterEntry(rootEntry);

		final List<AbstractOrderEntryModel> parentEntries = defaultTmaAbstractOrderEntryService.getBpoParentEntries(spoChildEntry);

		assertEquals(2, parentEntries.size());
		assertTrue(parentEntries.contains(rootEntry));
		assertTrue(parentEntries.contains(bpoChildEntry));
	}

	private AbstractOrderEntryModel createEntry(final String productCode, final Long quantity)
	{
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setProduct(createProductOffering(productCode));
		entry.setQuantity(quantity);

		return entry;
	}

	private AbstractOrderEntryModel createEntryWithBpo(final String productCode, final Long quantity)
	{
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setProduct(createBundledProductOffering(productCode));
		entry.setQuantity(quantity);

		return entry;
	}

	private TmaProductOfferingModel createProductOffering(final String productCode)
	{
		final TmaProductOfferingModel productOffering = new TmaProductOfferingModel();
		productOffering.setCode(productCode);

		return productOffering;
	}

	private TmaBundledProductOfferingModel createBundledProductOffering(final String productCode)
	{
		final TmaBundledProductOfferingModel bundledProductOffering = new TmaBundledProductOfferingModel();
		bundledProductOffering.setCode(productCode);

		return bundledProductOffering;
	}
}
