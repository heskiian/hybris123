/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentmethod;

import de.hybris.platform.b2ctelcofacades.data.TmaTimePeriodData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriodType;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link CCPaymentInfoData} and {@link PaymentMethodType}
 *
 * @since 1907
 */
public class PaymentMethodValidForAttributeMapper extends TmaAttributeMapper<CCPaymentInfoData, PaymentMethodType>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CCPaymentInfoData source, PaymentMethodType target, MappingContext context)
	{

		TmaTimePeriodData timePeriodData = new TmaTimePeriodData();
		timePeriodData.setStartDateMonth(source.getStartMonth());
		timePeriodData.setStartDateYear(source.getStartYear());
		timePeriodData.setEndDateMonth(source.getExpiryMonth());
		timePeriodData.setEndDateYear(source.getExpiryYear());

		target.setValidFor(getMapperFacade().map(timePeriodData, TimePeriodType.class, context));
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
