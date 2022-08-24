/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.indexedproductoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.IndexedProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atBaseType attribute between {@link ProductData} and {@link IndexedProductOffering}
 *
 * @since 1907
 */
public class TmaIndexedPoBaseTypeAttributeMapper extends TmaAttributeMapper<ProductData, IndexedProductOffering>
{
	 @Override
	 public void populateTargetAttributeFromSource(ProductData source, IndexedProductOffering target, MappingContext context)
	 {
		  if (StringUtils.isNotEmpty(source.getCode()) && target.getClass().getSuperclass() != null)
		  {
				target.setAtbaseType(target.getClass().getSuperclass().getSimpleName());
		  }
	 }
}
