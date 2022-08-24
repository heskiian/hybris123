/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptionservices.services.impl;

import de.hybris.platform.usageconsumptionservices.daos.UcUsageVolumeProductDao;
import de.hybris.platform.usageconsumptionservices.data.UcUsageVolumeProductContext;
import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeProductModel;
import de.hybris.platform.usageconsumptionservices.services.UcUsageVolumeProductService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Default implementation of {@link UcUsageVolumeProductService}.
 *
 * @since 2108
 */
public class DefaultUcUsageVolumeProductService implements UcUsageVolumeProductService
{
	private UcUsageVolumeProductDao usageVolumeProductDao;

	public DefaultUcUsageVolumeProductService(final UcUsageVolumeProductDao usageVolumeProductDao)
	{
		this.usageVolumeProductDao = usageVolumeProductDao;
	}

	@Override
	public List<UcUsageVolumeProductModel> getUsageVolumeProducts(final UcUsageVolumeProductContext ucUsageVolumeProductContext,
			final Integer offset, final Integer limit)
	{
		return getUsageVolumeProductDao().getUsageVolumeProducts(ucUsageVolumeProductContext, offset, limit);
	}

	@Override
	public Integer getNumberOfUsageVolumeProductFor(final UcUsageVolumeProductContext ucUsageVolumeProductContext)
	{
		return getUsageVolumeProductDao().getNumberOfUsageVolumeProductFor(ucUsageVolumeProductContext);
	}

	@Override
	public Date getMostRecentDate(final List<UcUsageVolumeProductModel> usageVolumeProducts)
	{
		final List<Date> date = new ArrayList<>();
		date.add(usageVolumeProducts.get(0).getConsumptionSummaries().iterator().next().getEffectiveDate());
		usageVolumeProducts
				.forEach(usageVolumeProduct -> usageVolumeProduct.getConsumptionSummaries().forEach(ucConsumptionSummaryModel -> {
					if (ucConsumptionSummaryModel.getEffectiveDate().after(date.get(0)))
					{
						date.add(0, ucConsumptionSummaryModel.getEffectiveDate());
					}
				}));

		return date.get(0);
	}

	protected UcUsageVolumeProductDao getUsageVolumeProductDao()
	{
		return usageVolumeProductDao;
	}
}
