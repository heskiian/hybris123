/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.*;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * BPO Guided Selling DashBoard Populator class , responsible for populating BPO GuidedSelling DashBoard Data from
 * Guided Selling Step Data
 *
 * @since 6.7
 * @deprecated {@link TmaGuidedSellingDashBoardDataPopulator} should be used instead
 */
@Deprecated(since = "2102")
public class TmaBpoGuidedSellingDashBoardDataPopulator<SOURCE extends GuidedSellingDashBoardPopulatorParameters, TARGET extends GuidedSellingDashBoardData>
		implements Populator<SOURCE, TARGET>
{
	private CartService cartService;
	private Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter;
	private static final String DEFAULT_GROUP = "_default";

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		final List<GuidedSellingStepData> stepDataList = source.getStepData();
		final List<GuidedSellingDashBoardEntryData> entryDataList = new ArrayList<>();
		final EntryGroup rootGroup = source.getRootGroup();

		for (final GuidedSellingStepData stepData : stepDataList)
		{
			final GuidedSellingDashBoardEntryData entryData = new GuidedSellingDashBoardEntryData();
			entryData.setStepId(stepData.getId());
			entryData.setStepName(stepData.getName());

			if (null != rootGroup)
			{
				final List<GuidedSellingDashBoardOrderEntryData> orderEntries = getOrderEntryListForSteps(rootGroup,
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

	private List<GuidedSellingDashBoardOrderEntryData> getOrderEntryListForSteps(final EntryGroup rootGroup, final String groupId)
	{
		final List<AbstractOrderEntryModel> cartOrderEntries = getCartService().getSessionCart().getEntries();
		final List<AbstractOrderEntryModel> allOrderEntriesForRootGroup = new ArrayList<>();
		List<GuidedSellingDashBoardOrderEntryData> orderEntriesForGroup = new ArrayList<>();
		for (final AbstractOrderEntryModel abstractOrderEntryModel : cartOrderEntries)
		{
			if (abstractOrderEntryModel.getEntryGroupNumbers().contains(rootGroup.getGroupNumber()))
			{
				allOrderEntriesForRootGroup.add(abstractOrderEntryModel);
			}
		}
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
				getGroupForOrderEntry(orderEntriesForGroup, abstractOrderEntryModel, product, groupId);

			}
		}
		return orderEntriesForGroup;
	}

	private void getGroupForOrderEntry(final List<GuidedSellingDashBoardOrderEntryData> orderEntriesForGroup,
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
				for (final TmaProductOfferingGroupModel productOfferingGroup : product.getAssociatedProductOfferingGroups())
				{
					if (productOfferingGroup.getParentBundleProductOffering().equals(parentBpo))
					{
						return;
					}
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

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected Converter<AbstractOrderEntryModel, OrderEntryData> getOrderEntryConverter()
	{
		return orderEntryConverter;
	}

	@Required
	public void setOrderEntryConverter(final Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter)
	{
		this.orderEntryConverter = orderEntryConverter;
	}

}
