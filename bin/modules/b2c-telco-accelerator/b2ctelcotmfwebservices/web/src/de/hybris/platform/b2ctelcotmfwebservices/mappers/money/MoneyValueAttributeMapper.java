/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.money;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import de.hybris.platform.commercefacades.product.data.PriceData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps price value for cart price attribute between {@link PriceData} and {@link Money}
 *
 * @since 1907
 */
public class MoneyValueAttributeMapper extends TmaAttributeMapper<PriceData, Money>
{
	@Override
	public void populateTargetAttributeFromSource(PriceData source, Money target,
			MappingContext context)
	{
		if (source.getValue() != null)
		{
			target.setValue(source.getValue().toString());
		}

	}
}
