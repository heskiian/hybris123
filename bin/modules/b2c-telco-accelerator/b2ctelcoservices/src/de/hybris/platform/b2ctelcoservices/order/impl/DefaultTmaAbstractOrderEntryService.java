/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.impl.DefaultAbstractOrderEntryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of the {@link TmaAbstractOrderEntryService}.
 *
 * @since 2011
 */
public class DefaultTmaAbstractOrderEntryService extends DefaultAbstractOrderEntryService implements TmaAbstractOrderEntryService
{
	@Override
	public Long getPoQuantity(final List<AbstractOrderEntryModel> entries, final TmaProductOfferingModel productOffering)
	{
		validateParameterNotNull(productOffering, "The product offering can not be null.");

		if (CollectionUtils.isEmpty(entries))
		{
			return (long) 0;
		}

		return entries.stream()
				.filter((AbstractOrderEntryModel entry) -> productOffering.getCode().equals(entry.getProduct().getCode()))
				.map(AbstractOrderEntryModel::getQuantity)
				.reduce((long) 0, Long::sum);
	}

	@Override
	public List<AbstractOrderEntryModel> getSpoChildEntries(final AbstractOrderEntryModel orderEntry)
	{
		final List<AbstractOrderEntryModel> orderEntryList = new ArrayList<>();

		if (orderEntry == null || (CollectionUtils.isEmpty(orderEntry.getChildEntries()) && orderEntry.getMasterEntry() == null))
		{
			return Collections.emptyList();
		}

		if (CollectionUtils.isEmpty(orderEntry.getChildEntries()) &&
				!(orderEntry.getProduct() instanceof TmaBundledProductOfferingModel))
		{
			orderEntryList.add(orderEntry);
			return orderEntryList;
		}

		for (AbstractOrderEntryModel entry : orderEntry.getChildEntries())
		{
			orderEntryList.addAll(getSpoEntries(entry));
		}

		return orderEntryList;
	}

	@Override
	public AbstractOrderEntryModel getRootEntry(final AbstractOrderEntryModel entry)
	{
		if (entry == null)
		{
			return null;
		}

		if (entry.getMasterEntry() == null)
		{
			return entry.getProduct() instanceof TmaBundledProductOfferingModel ? entry : null;
		}

		return getRootEntry(entry.getMasterEntry());
	}

	@Override
	public AbstractOrderEntryModel getEntryBy(AbstractOrderModel abstractOrder, int entryNumber)
	{
		if (abstractOrder.getEntries() == null) {
			return null;
		}

		for (AbstractOrderEntryModel entryModel : abstractOrder.getEntries())
		{
			if (entryNumber == entryModel.getEntryNumber()) {
				return entryModel;
			}
		}

		return null;
	}

	@Override
	public List<AbstractOrderEntryModel> getAllChildEntries(final AbstractOrderEntryModel orderEntry)
	{
		if (orderEntry == null || (CollectionUtils.isEmpty(orderEntry.getChildEntries()) && orderEntry.getMasterEntry() == null))
		{
			return Collections.emptyList();
		}

		if (CollectionUtils.isEmpty(orderEntry.getChildEntries()))
		{
			return Collections.singletonList(orderEntry);
		}

		final List<AbstractOrderEntryModel> orderEntryList = new ArrayList<>();

		orderEntry.getChildEntries().forEach((AbstractOrderEntryModel entry) -> orderEntryList.addAll(getAllChildEntries(entry)));

		if (orderEntry.getMasterEntry() != null)
		{
			orderEntryList.add(orderEntry);
		}

		return orderEntryList;
	}

	@Override
	public List<AbstractOrderEntryModel> getBpoParentEntries(final AbstractOrderEntryModel orderEntry)
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();

		if (orderEntry.getProduct() instanceof TmaBundledProductOfferingModel)
		{
			entries.add(orderEntry);
		}

		if (orderEntry.getMasterEntry() != null)
		{
			entries.addAll(getBpoParentEntries(orderEntry.getMasterEntry()));
		}

		return entries;
	}

	/**
	 * Recursive method used for returning all the child SPOs.
	 *
	 * @param orderEntry
	 * 		The entry
	 * @return List of {@link AbstractOrderEntryModel}
	 */
	protected List<AbstractOrderEntryModel> getSpoEntries(final AbstractOrderEntryModel orderEntry)
	{
		if (CollectionUtils.isEmpty(orderEntry.getChildEntries()))
		{
			return Collections.singletonList(orderEntry);
		}

		final List<AbstractOrderEntryModel> orderEntryList = new ArrayList<>();
		orderEntry.getChildEntries().forEach((AbstractOrderEntryModel entry) -> orderEntryList.addAll(getSpoEntries(entry)));

		return orderEntryList;
	}
}
