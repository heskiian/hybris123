/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardData;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardEntryData;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardOrderEntryData;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardPopulatorParameters;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingStepData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * BPO Guided Selling DashBoard Populator class , responsible for populating BPO GuidedSelling DashBoard Data from
 * Guided Selling Step Data. It makes use of parent entries for determining the entries in the same group.
 *
 * @since 2102
 */
public class TmaGuidedSellingDashBoardDataPopulator<SOURCE extends GuidedSellingDashBoardPopulatorParameters, TARGET extends GuidedSellingDashBoardData>
		implements Populator<SOURCE, TARGET>
{
	private static final String DEFAULT_GROUP = "_default";

	private CartService cartService;
	private Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter;

	private TmaAbstractOrderEntryService abstractOrderEntryService;

	public TmaGuidedSellingDashBoardDataPopulator(CartService cartService,
			Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter,
			TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.cartService = cartService;
		this.orderEntryConverter = orderEntryConverter;
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		final List<GuidedSellingStepData> stepDataList = source.getStepData();
		final List<GuidedSellingDashBoardEntryData> entryDataList = new ArrayList<>();

		final AbstractOrderEntryModel parentEntry = source.getParentEntry();

		for (final GuidedSellingStepData stepData : stepDataList)
		{
			final GuidedSellingDashBoardEntryData entryData = new GuidedSellingDashBoardEntryData();
			entryData.setStepId(stepData.getId());
			entryData.setStepName(stepData.getName());

			if (null != parentEntry)
			{
				final List<GuidedSellingDashBoardOrderEntryData> orderEntries = getOrderEntryListForSteps(parentEntry,
						stepData.getId());
				entryData.setDashBoardOrderEntries(orderEntries);

				if (CollectionUtils.isNotEmpty(orderEntries))
				{
					target.setIsAnyProductSelected(true);
				}
			}
			entryDataList.add(entryData);
		}

		target.setDashBoardEntries(entryDataList);
	}

	private List<GuidedSellingDashBoardOrderEntryData> getOrderEntryListForSteps(final AbstractOrderEntryModel parentEntry,
			final String groupId)
	{
		final List<AbstractOrderEntryModel> allOrderEntriesForRootGroup = getAbstractOrderEntryService()
				.getSpoChildEntries(parentEntry);
		List<GuidedSellingDashBoardOrderEntryData> orderEntriesForGroup = new ArrayList<>();

		if (!allOrderEntriesForRootGroup.isEmpty())
		{
			orderEntriesForGroup = getOrderEntryAssociatedWithStep(allOrderEntriesForRootGroup, groupId);
		}

		return orderEntriesForGroup;
	}

	private List<GuidedSellingDashBoardOrderEntryData> getOrderEntryAssociatedWithStep(
			final List<AbstractOrderEntryModel> allOrderEntriesForRootGroup, final String groupId)
	{
		final List<GuidedSellingDashBoardOrderEntryData> orderEntriesForGroup = new ArrayList<>();

		for (final AbstractOrderEntryModel abstractOrderEntryModel : allOrderEntriesForRootGroup)
		{
			if (abstractOrderEntryModel.getProduct() instanceof TmaProductOfferingModel)
			{
				final TmaProductOfferingModel product = (TmaProductOfferingModel) abstractOrderEntryModel.getProduct();
				updateOrderEntriesInTheSameGroup(orderEntriesForGroup, abstractOrderEntryModel, product, groupId);

			}
		}
		return orderEntriesForGroup;
	}

	private void updateOrderEntriesInTheSameGroup(final List<GuidedSellingDashBoardOrderEntryData> orderEntriesForGroup,
			final AbstractOrderEntryModel abstractOrderEntryModel, final TmaProductOfferingModel product, final String groupId)
	{
		for (final TmaProductOfferingGroupModel productOfferingGroup : product.getAssociatedProductOfferingGroups())
		{
			if (productOfferingGroup.getCode().equals(groupId))
			{
				final GuidedSellingDashBoardOrderEntryData orderEntryData = new GuidedSellingDashBoardOrderEntryData();
				orderEntryData.setOrderEntry(getOrderEntryConverter().convert(abstractOrderEntryModel));
				orderEntriesForGroup.add(orderEntryData);
				return;
			}
		}

		for (final TmaBundledProductOfferingModel parentBpo : product.getParents())
		{
			if (parentBpo.getCode().concat(DEFAULT_GROUP).equals(groupId))
			{
				final boolean isPoInAssociatedGroups = product.getAssociatedProductOfferingGroups().stream()
						.anyMatch((TmaProductOfferingGroupModel productOfferingGroup) ->
								productOfferingGroup.getParentBundleProductOffering().equals(parentBpo));

				if (isPoInAssociatedGroups)
				{
					return;
				}

				final GuidedSellingDashBoardOrderEntryData orderEntryData = new GuidedSellingDashBoardOrderEntryData();
				orderEntryData.setOrderEntry(getOrderEntryConverter().convert(abstractOrderEntryModel));
				orderEntriesForGroup.add(orderEntryData);
			}
		}
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	protected Converter<AbstractOrderEntryModel, OrderEntryData> getOrderEntryConverter()
	{
		return orderEntryConverter;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
