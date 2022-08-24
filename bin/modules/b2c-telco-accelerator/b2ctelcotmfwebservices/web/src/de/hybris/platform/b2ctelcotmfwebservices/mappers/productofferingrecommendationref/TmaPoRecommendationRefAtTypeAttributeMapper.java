/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingrecommendationref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRecommendationRef;
import de.hybris.platform.commercefacades.product.data.ProductData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link ProductData} and
 * {@link ProductOfferingRecommendationRef}
 *
 * @since 1907
 */
public class TmaPoRecommendationRefAtTypeAttributeMapper extends TmaAttributeMapper<ProductData, ProductOfferingRecommendationRef>
{
	 @Override
	 public void populateTargetAttributeFromSource(ProductData source, ProductOfferingRecommendationRef target,
		  MappingContext context)
	 {
		  if (StringUtils.isNotEmpty(source.getCode()))
		  {
				target.setAttype(target.getClass().getSimpleName());
		  }
	 }
}
