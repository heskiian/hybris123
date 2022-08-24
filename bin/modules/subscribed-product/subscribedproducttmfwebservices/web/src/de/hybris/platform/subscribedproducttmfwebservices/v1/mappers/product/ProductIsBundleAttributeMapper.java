/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductBundleModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for isBundle attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductIsBundleAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	public ProductIsBundleAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			if (source instanceof SpiProductBundleModel)
			{
				target.setIsBundle(true);
			}
			else
			{
				target.setIsBundle(false);
			}
		}
	}
}
