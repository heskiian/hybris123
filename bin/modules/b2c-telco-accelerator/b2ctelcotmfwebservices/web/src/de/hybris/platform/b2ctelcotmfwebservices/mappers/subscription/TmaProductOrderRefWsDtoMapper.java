/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductOrderRefWsDto;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {@link TmaSubscribedProductData} and (@link TmaProductOrderRefWsDto}
 * 
 * @since 1810
 */
public class TmaProductOrderRefWsDtoMapper extends AbstractCustomMapper<TmaSubscribedProductData, TmaProductOrderRefWsDto>
{

	static final String HREF_LINK = Config.getString(B2ctelcotmfwebservicesConstants.API_BASE_URL,
			B2ctelcotmfwebservicesConstants.DEFAULT_HOST)
			+ Config.getString(B2ctelcotmfwebservicesConstants.HYBRIS_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_BASESITEID, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_ORDERAPI_URL, StringUtils.EMPTY);

	@Override
	public void mapAtoB(final TmaSubscribedProductData a, final TmaProductOrderRefWsDto b, final MappingContext context)
	{
		context.beginMappingField("orderNumber", getAType(), a, "id", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getOrderNumber() != null)
			{
				b.setId(a.getOrderNumber());
				b.setReferredType(Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_DEFAULT_REFERRED, StringUtils.EMPTY));
				b.setHref(HREF_LINK + a.getOrderNumber());
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField("orderEntryNumber", getAType(), a, "orderItemId", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getOrderEntryNumber() != null)
			{
				b.setOrderItemId(a.getOrderEntryNumber().toString());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaProductOrderRefWsDto b, final TmaSubscribedProductData a, final MappingContext context)
	{
		// other fields are mapped automatically
		context.beginMappingField("id", getBType(), b, "orderNumber", getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && b.getId() != null)
			{
				a.setOrderNumber(b.getId());
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField("orderItemId", getBType(), b, "orderEntryNumber", getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && b.getId() != null)
			{
				a.setOrderEntryNumber(Integer.valueOf(b.getOrderItemId()));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

}
