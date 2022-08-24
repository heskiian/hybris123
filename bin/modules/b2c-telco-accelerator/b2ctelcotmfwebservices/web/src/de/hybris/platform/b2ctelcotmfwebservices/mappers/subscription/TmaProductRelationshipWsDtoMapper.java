/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaProductRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductRelationshipWsDto;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import javax.annotation.Resource;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {@link TmaSubscribedProductData} and {@link TmaProductRelationshipWsDto}
 *
 * @since 1810
 */
public class TmaProductRelationshipWsDtoMapper extends AbstractCustomMapper<TmaSubscribedProductData, TmaProductRelationshipWsDto>
{

	public static final String DEFAULT_TYPE = "bundled";

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;


	@Override
	public void mapAtoB(final TmaSubscribedProductData a, final TmaProductRelationshipWsDto b, final MappingContext context)
	{
		context.beginMappingField("id", getAType(), a, "type", getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				b.setType(DEFAULT_TYPE);
				b.setProduct(dataMapper.map(a, TmaProductRefWsDto.class));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaProductRelationshipWsDto b, final TmaSubscribedProductData a, final MappingContext context)
	{
		// other fields are mapped automatically
		context.beginMappingField("product", getBType(), b, "productCode", getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && b.getProduct().getId() != null)
			{
				a.setProductCode(b.getProduct().getId());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}
}
