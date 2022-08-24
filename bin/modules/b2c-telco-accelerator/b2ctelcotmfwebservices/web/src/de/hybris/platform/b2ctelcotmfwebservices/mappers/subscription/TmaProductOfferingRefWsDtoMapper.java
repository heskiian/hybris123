/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductOfferingRefWsDto;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {@link ProductData} and (@link TmaProductOfferingRefWsDto}
 *
 * @since 1810
 */
public class TmaProductOfferingRefWsDtoMapper extends AbstractCustomMapper<ProductData, TmaProductOfferingRefWsDto>
{
	private static final String CODE = "code";
	private static final String HREF = "href";
	private static final String ID = "id";

	@Override
	public void mapAtoB(final ProductData a, final TmaProductOfferingRefWsDto b, final MappingContext context)
	{
		//other fields map automatically
		mapAtoBDefault(a, b, context);
		mapAtoBId(a, b, context);
	}

	protected void mapAtoBDefault(final ProductData a, final TmaProductOfferingRefWsDto b, final MappingContext context)
	{
		context.beginMappingField(CODE, getAType(), a, HREF, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getCode()))
			{
				b.setHref((Config.getParameter(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_API_URL) + a.getCode()));
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField(CODE, getAType(), a, "referredType", getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				b.setReferredType(Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_DEFAULT_REFERRED, StringUtils.EMPTY));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapAtoBId(final ProductData a, final TmaProductOfferingRefWsDto b, final MappingContext context)
	{
		context.beginMappingField(CODE, getAType(), a, ID, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getCode()))
			{
				b.setId(a.getCode());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaProductOfferingRefWsDto b, final ProductData a, final MappingContext context)
	{
		// other fields are mapped automatically
		context.beginMappingField(ID, getBType(), b, "customerId", getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && StringUtils.isNotEmpty(b.getId()))
			{
				a.setCode(b.getId());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

}
