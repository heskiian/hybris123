/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;


import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Populates {@link OrderData} with promotion details for entries from {@link AbstractOrderModel}.
 *
 * @since 2003
 */
public class TmaOrderPromotionPopulator implements Populator<AbstractOrderModel, OrderData>
{
	@Override
	public void populate(AbstractOrderModel source, OrderData target) throws ConversionException
	{
		if (target.getEntries() == null || target.getEntries().isEmpty())
		{
			return;
		}

		setPromotionOnEntries(target.getEntries(), target.getAppliedProductPromotions());
	}


	/**
	 * Sets the promotion on the entry on which is applied.
	 *
	 * @param entries
	 * 		The entries
	 * @param appliedPromotions
	 * 		The list of applied promotions
	 */
	protected void setPromotionOnEntries(final List<OrderEntryData> entries, final List<PromotionResultData> appliedPromotions)
	{
		if (CollectionUtils.isEmpty(entries) || CollectionUtils.isEmpty(appliedPromotions))
		{
			return;
		}

		entries.forEach((OrderEntryData entry) -> {
			if (entry == null)
			{
				return;
			}

			final Set<PromotionResultData> promotionsForEntry = appliedPromotions.stream()
					.filter((PromotionResultData promotionResult) ->
							promotionResult.getConsumedEntries().stream().anyMatch((PromotionOrderEntryConsumedData consumedEntry) ->
									consumedEntry.getOrderEntryNumber().equals(entry.getEntryNumber())))
					.collect(Collectors.toSet());

			entry.setAppliedPromotionResults(promotionsForEntry);

			setPromotionOnEntries(entry.getEntries(), appliedPromotions);
		});
	}
}
