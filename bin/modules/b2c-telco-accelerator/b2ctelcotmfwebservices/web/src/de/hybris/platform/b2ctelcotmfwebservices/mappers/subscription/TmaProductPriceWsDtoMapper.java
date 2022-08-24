/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductPriceWsDto;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {@link TmaSubscribedProductData} and (@link TmaProductPriceWsDto}
 *
 * @since 1810
 */
public class TmaProductPriceWsDtoMapper
		extends AbstractCustomMapper<TmaSubscribedProductData, TmaProductPriceWsDto>
{

	public static final String PRICE_DESCRIPTOIN = "Price for product: ";
	public static final String RECURRING_CHARGE = "recurringChargePeriod";
	public static final String BILLING_FREQUENCY = "billingFrequency";

	@Override
	public void mapAtoB(final TmaSubscribedProductData a, final TmaProductPriceWsDto b, final MappingContext context)
	{
		mapAtoBBillingFrequency(a, b, context);
		mapAtoBDefault(a, b, context);
	}

	protected void mapAtoBBillingFrequency(final TmaSubscribedProductData a, final TmaProductPriceWsDto b,
			final MappingContext context)
	{
		context.beginMappingField(BILLING_FREQUENCY, getAType(), a, RECURRING_CHARGE, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getBillingFrequency() != null)
			{
				b.setRecurringChargePeriod(a.getBillingFrequency());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapAtoBDefault(final TmaSubscribedProductData a, final TmaProductPriceWsDto b,
			final MappingContext context)
	{
		context.beginMappingField("productCode", getAType(), a, "description", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getBillingFrequency() != null)
			{
				b.setType(a.getClass().getSimpleName());
				b.setSchemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
				b.setDescription(PRICE_DESCRIPTOIN + a.getProductCode());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaProductPriceWsDto b, final TmaSubscribedProductData a, final MappingContext context)
	{
		// other fields are mapped automatically
		context.beginMappingField(RECURRING_CHARGE, getBType(), b, BILLING_FREQUENCY, getAType(), a);
		try
		{
			a.setBillingFrequency(b.getRecurringChargePeriod());
		}
		finally
		{
			context.endMappingField();
		}
	}
}
