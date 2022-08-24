/*
 * Copyright (c)  2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order.impl;

import de.hybris.platform.b2ctelcofacades.order.TmaOrderEntryFacade;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation for {@link TmaOrderEntryFacade}.
 *
 * @since 2102
 */
public class DefaultTmaOrderEntryFacade implements TmaOrderEntryFacade
{
	@Override
	public OrderEntryData getEntry(final List<OrderEntryData> entries, final long entryNumber)
	{
		if (CollectionUtils.isEmpty(entries))
		{
			return null;
		}

		final Optional<OrderEntryData> entry = entries.stream()
				.filter((OrderEntryData orderEntryData) -> orderEntryData.getEntryNumber() == entryNumber).findFirst();

		if (entry.isPresent())
		{
			return entry.get();
		}

		for (OrderEntryData orderEntryData : entries)
		{
			final OrderEntryData matchingEntry = getEntry(orderEntryData.getEntries(), entryNumber);
			if (matchingEntry != null)
			{
				return matchingEntry;
			}
		}

		return null;
	}

	@Override
	public OrderEntryData getEntry(final OrderEntryData entry, String productCode)
	{
		if (entry == null || productCode == null)
		{
			return null;
		}

		if (productCode.equals(entry.getProduct().getCode()))
		{
			return entry;
		}

		if (CollectionUtils.isEmpty(entry.getEntries()))
		{
			return null;
		}

		for (OrderEntryData childEntry : entry.getEntries())
		{
			final OrderEntryData matchingEntry = getEntry(childEntry, productCode);
			if (matchingEntry != null)
			{
				return matchingEntry;
			}
		}

		return null;
	}

	@Override
	public OrderEntryData getEntry(final List<OrderEntryData> entries, final String productCode, final String pickupStore)
	{
		if (CollectionUtils.isEmpty(entries))
		{
			return null;
		}

		final Optional<OrderEntryData> optionalEntry = entries.stream()
				.filter((OrderEntryData orderEntryData) -> orderEntryData.getProduct().getCode().equals(productCode)).findFirst();

		if (optionalEntry.isPresent())
		{
			final OrderEntryData entry = optionalEntry.get();
			if (pickupStore == null && entry.getDeliveryPointOfService() == null)
			{
				return entry;
			}

			if (pickupStore != null && entry.getDeliveryPointOfService() != null && pickupStore
					.equals(entry.getDeliveryPointOfService().getName()))
			{
				return entry;
			}
		}

		for (OrderEntryData orderEntryData : entries)
		{
			final OrderEntryData matchingEntry = getEntry(orderEntryData.getEntries(), productCode, pickupStore);
			if (matchingEntry != null)
			{
				return matchingEntry;
			}
		}

		return null;
	}

	@Override
	public List<OrderEntryData> getAllSpoEntries(final OrderEntryData entry)
	{
		final List<OrderEntryData> spoEntries = new ArrayList<>();
		if (entry == null)
		{
			return spoEntries;
		}

		final List<OrderEntryData> childEntries = entry.getEntries();

		if (CollectionUtils.isEmpty(childEntries))
		{
			spoEntries.add(entry);
			return spoEntries;
		}

		childEntries.forEach((OrderEntryData childEntry) -> {
			if (CollectionUtils.isEmpty(childEntry.getEntries()))
			{
				spoEntries.add(childEntry);
				return;
			}

			spoEntries.addAll(getAllSpoEntries(childEntry));
		});

		return spoEntries;
	}

	@Override
	public List<OrderEntryData> getAllSpoEntries(final AbstractOrderData abstractOrderData)
	{
		if (abstractOrderData == null || CollectionUtils.isEmpty(abstractOrderData.getEntries()))
		{
			return Collections.emptyList();
		}

		final List<OrderEntryData> spoEntries = new ArrayList<>();

		abstractOrderData.getEntries().forEach((OrderEntryData entry) -> {
			if (CollectionUtils.isEmpty(entry.getEntries()))
			{
				spoEntries.add(entry);
				return;
			}

			final List<OrderEntryData> entryChildren = getAllSpoEntries(entry);
			if (CollectionUtils.isNotEmpty(entryChildren))
			{
				spoEntries.addAll(entryChildren);
			}
		});

		return spoEntries;
	}

	@Override
	public List<OrderEntryData> getAllChildEntries(final OrderEntryData entry)
	{
		if (entry == null || (CollectionUtils.isEmpty(entry.getEntries())))
		{
			return Collections.emptyList();
		}

		if (CollectionUtils.isEmpty(entry.getEntries()))
		{
			return Collections.singletonList(entry);
		}

		final List<OrderEntryData> orderEntryList = new ArrayList<>();

		orderEntryList.add(entry);
		entry.getEntries().forEach((OrderEntryData childEntry) -> orderEntryList.addAll(getAllChildEntries(childEntry)));

		return orderEntryList;
	}
}
