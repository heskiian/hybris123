/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.money;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps currency for cart price attribute between {@link PriceData} and {@link Money}
 *
 * @since 1907
 */
public class MoneyUnitAttributeMapper extends TmaAttributeMapper<PriceData, Money>
{
	@Override
	public void populateTargetAttributeFromSource(PriceData source, Money target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCurrencyIso()))
		{
			target.setUnit(source.getCurrencyIso());
		}
	}
}

