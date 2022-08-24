/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for rating attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoRatingAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	@Override
	public void populateTargetAttributeFromSource(ProductData source, ProductOffering target, MappingContext context)
	{
		if (source.getAverageRating() != null)
		{
			target.setRating(source.getAverageRating().toString());
		}
	}
}
