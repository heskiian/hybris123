/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * TmaPriceIsPriceOverrideAttributeMapper populates value of isPriceOverride attribute from
 * {@link PriceData} to {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPriceIsPriceOverrideAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		boolean isPriceOverride = false;
		if (source.getAffectedProductOffering() != null)
		{
			isPriceOverride = true;
		}

		if (CollectionUtils.isNotEmpty(source.getRequiredProductOfferings()))
		{
			isPriceOverride = true;
		}

		target.setIsPriceOverride(isPriceOverride);
	}

}
