/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.creditcard;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CreditCardType;
import de.hybris.platform.b2ctelcofacades.data.TmaCreditCardData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for brand attribute between {@link TmaCreditCardData} and {@link CreditCardType}
 *
 * @since 1907
 */
public class CreditCardBrandAttributeMapper extends TmaAttributeMapper<TmaCreditCardData, CreditCardType>
{
	@Override
	public void populateTargetAttributeFromSource(TmaCreditCardData source, CreditCardType target, MappingContext context)
	{
		if (source.getCardTypeData() != null && StringUtils.isNotEmpty(source.getCardTypeData().getName()))
		{
			target.setBrand(source.getCardTypeData().getName());
		}
	}
}
