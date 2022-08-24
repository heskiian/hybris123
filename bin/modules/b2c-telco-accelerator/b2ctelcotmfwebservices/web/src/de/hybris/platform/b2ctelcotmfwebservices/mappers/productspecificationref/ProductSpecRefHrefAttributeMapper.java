/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecificationref;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecificationRef;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaProductSpecificationData} and
 * {@link ProductSpecificationRef}
 *
 * @since 1903
 */
public class ProductSpecRefHrefAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecificationData, ProductSpecificationRef>
{

	@Override
	public void populateTargetAttributeFromSource(TmaProductSpecificationData source, ProductSpecificationRef target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_SPECIFICATION_API_URL + source.getId());
		}
	}
}
