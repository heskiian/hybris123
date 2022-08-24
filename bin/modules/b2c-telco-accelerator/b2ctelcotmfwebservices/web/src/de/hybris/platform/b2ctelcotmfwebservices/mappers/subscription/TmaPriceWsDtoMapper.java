/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaPriceWsDto;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import java.math.BigDecimal;

import javax.annotation.Resource;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {@link PriceData} and (@link TmaPriceWsDto}
 * 
 * @since 1810
 */
public class TmaPriceWsDtoMapper extends AbstractCustomMapper<PriceData, TmaPriceWsDto>
{
	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	public static final String TAX_INCLUDED_AMOUNT = "taxIncludedAmount";
	public static final String VALUE = "value";

	@Override
	public void mapAtoB(final PriceData a, final TmaPriceWsDto b, final MappingContext context)
	{
		context.beginMappingField(VALUE, getAType(), a, TAX_INCLUDED_AMOUNT, getBType(), b);
		try
		{
			if (shouldMap(b, a, context) && a.getValue() != null)
			{
				b.setSchemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
				b.setType(b.getClass().getSimpleName());
				b.setTaxIncludedAmount(dataMapper.map(a, Money.class));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaPriceWsDto b, final PriceData a, final MappingContext context)
	{
		// other fields are mapped automatically
		context.beginMappingField(TAX_INCLUDED_AMOUNT, getBType(), b, VALUE, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && b.getTaxIncludedAmount() != null)
			{
				a.setValue(new BigDecimal(b.getTaxIncludedAmount().getValue()));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}
}
