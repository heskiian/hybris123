/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.indexedproductoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.IndexedProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for isInStock attribute between {@link ProductData} and {@link IndexedProductOffering}
 *
 * @since 1911
 */
public class IndexedPoIsInStockAttributeMapper extends TmaAttributeMapper<ProductData, IndexedProductOffering>
{
	@Override
	public void populateTargetAttributeFromSource(ProductData source, IndexedProductOffering target, MappingContext context)
	{
		boolean isInStock = false;
		if (source.getStock() != null && source.getStock().getStockLevelStatus() != null)
		{
			isInStock = source.getStock().getStockLevelStatus().getCode().equalsIgnoreCase(StockLevelStatus.INSTOCK.getCode()) || source
					.getStock().getStockLevelStatus().getCode().equalsIgnoreCase(StockLevelStatus.LOWSTOCK.getCode());
		}

		target.isInStock(isInStock);
	}
}