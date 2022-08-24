/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute mapper maps data for code attribute between {@link PriceData} and
 * {@link ProductOfferingPriceWsDTO}
 *
 * @since 2007
 */
public class TmaPriceIdAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(PriceData source, ProductOfferingPriceWsDTO target,
			MappingContext context)
	{
		if (source.getCode() != null)
		{
			target.setId(source.getCode());
		}
	}
}
