/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.EntryMergeFilter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Merge only entries that do not contain configuration-dependent products (products that do not have configurable PSCVs)
 *
 * @since 1911
 */
public class TmaEntryMergeConfigurablePscFilter implements EntryMergeFilter
{
	private TmaPoService poService;

	public TmaEntryMergeConfigurablePscFilter(TmaPoService poService)
	{
		this.poService = poService;
	}

	@Override
	public Boolean apply(@Nonnull AbstractOrderEntryModel candidate, @Nonnull AbstractOrderEntryModel target)
	{
		final ProductModel product = target.getProduct();
		if (product instanceof TmaProductOfferingModel)
		{
			final Set<TmaProductSpecCharacteristicModel> configurableProductSpecCharacteristics = getPoService()
					.getConfigurableProductSpecCharacteristics((TmaProductOfferingModel) product);
			
			final Set<TmaProductSpecCharValueUseModel> configurablePscvu = getPoService().getConfigurableProductSpecCharValueUses((TmaProductOfferingModel) product);
			
			if (CollectionUtils.isNotEmpty(configurableProductSpecCharacteristics) || CollectionUtils.isNotEmpty(configurablePscvu))
			{
				return false;
			}
		}
		return true;
	}

	protected TmaPoService getPoService()
	{
		return poService;
	}
}
