/*
*Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for lastUpdate attribute between {@link PriceData} and
 * {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPoPriceLastUpdateAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getModifiedtime() != null)
		{
			target.setModifiedTime(source.getModifiedtime());
		}
	}
}
