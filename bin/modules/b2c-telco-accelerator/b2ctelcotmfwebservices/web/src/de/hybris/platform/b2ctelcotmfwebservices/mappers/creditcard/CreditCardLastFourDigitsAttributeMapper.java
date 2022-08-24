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
 * This attribute Mapper class maps data for last four digits attribute between {@link TmaCreditCardData} and
 * {@link CreditCardType}
 *
 * @since 1907
 */
public class CreditCardLastFourDigitsAttributeMapper extends TmaAttributeMapper<TmaCreditCardData, CreditCardType>
{
	@Override
	public void populateTargetAttributeFromSource(TmaCreditCardData source, CreditCardType target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getLastFourDigits()))
		{
			target.setLastFourDigits(source.getLastFourDigits().replace("*", ""));
		}
	}
}
