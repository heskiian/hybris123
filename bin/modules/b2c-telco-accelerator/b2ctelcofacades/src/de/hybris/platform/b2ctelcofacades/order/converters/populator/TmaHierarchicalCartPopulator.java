/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcofacades.order.TmaOrderEntryFacade;
import de.hybris.platform.commercefacades.order.EntryGroupData;
import de.hybris.platform.commercefacades.order.converters.populator.CartPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.collections.CollectionUtils;


/**
 * Hierarchical Cart populator with TMA specific details.
 *
 * @since 2102
 */
public class TmaHierarchicalCartPopulator<T extends CartData> extends CartPopulator<T>
{
	private TmaOrderEntryFacade orderEntryFacade;

	public TmaHierarchicalCartPopulator(final TmaOrderEntryFacade orderEntryFacade)
	{
		this.orderEntryFacade = orderEntryFacade;
	}

	@Override
	protected void addEntries(final AbstractOrderModel source, final AbstractOrderData prototype)
	{
		prototype.setEntries(getOrderEntryConverter()
				.convertAll(source.getEntries().stream().filter((order -> order.getMasterEntry() == null)).collect(
						Collectors.toList())));
	}

	@Override
	protected void addEntryGroups(final AbstractOrderModel source, final AbstractOrderData target)
	{
		if (target.getEntries() == null)
		{
			target.setEntries(Collections.emptyList());
		}

		if (CollectionUtils.isEmpty(source.getEntryGroups()))
		{
			return;
		}

		final List<EntryGroupData> rootGroups;
		final List<List<EntryGroupData>> groupTrees = source.getEntryGroups().stream()
				.map(getEntryGroupService()::getNestedGroups)
				.map(getEntryGroupConverter()::convertAll)
				.collect(Collectors.toList());
		rootGroups = groupTrees.stream()
				.map(tree -> tree.get(0))
				.collect(Collectors.toList());
		final List<EntryGroup> sourceGroups = source.getEntryGroups().stream()
				.map(getEntryGroupService()::getNestedGroups)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
		final Map<Integer, EntryGroupData> targetGroups = groupTrees.stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toMap(EntryGroupData::getGroupNumber, x -> x));
		updateEntryGroupReferences(sourceGroups, targetGroups);
		assignParentGroups(targetGroups.values());

		final MultivaluedMap<Integer, OrderEntryData> groupIdToEntryDataMap = mapGroupIdToEntryData(source, target);
		rootGroups.forEach(rootGroup -> assignEntriesToGroups(rootGroup, groupIdToEntryDataMap));

		target.setRootGroups(rootGroups);
		sortEntryGroups(target);
	}

	@Override
	protected MultivaluedMap<Integer, OrderEntryData> mapGroupIdToEntryData(final AbstractOrderModel source,
			final AbstractOrderData target)
	{
		final MultivaluedMap<Integer, OrderEntryData> groupIdToEntryDataMap = new MultivaluedHashMap<>();

		if (CollectionUtils.isEmpty(source.getEntries()))
		{
			return groupIdToEntryDataMap;
		}

		source.getEntries().forEach(entryModel -> {
			final OrderEntryData dto = getOrderEntryFacade().getEntry(target.getEntries(), entryModel.getEntryNumber());

			if (dto == null)
			{
				throw new IllegalArgumentException(
						"Order entry model " + entryModel.getEntryNumber() + " has no corresponding entry data");
			}

			if (CollectionUtils.isEmpty(entryModel.getEntryGroupNumbers()))
			{
				groupIdToEntryDataMap.add(null, dto);
			}
			else
			{
				entryModel.getEntryGroupNumbers().forEach(entryGroupNumber -> groupIdToEntryDataMap.add(entryGroupNumber, dto));
			}
		});

		return groupIdToEntryDataMap;
	}

	protected TmaOrderEntryFacade getOrderEntryFacade()
	{
		return orderEntryFacade;
	}
}
