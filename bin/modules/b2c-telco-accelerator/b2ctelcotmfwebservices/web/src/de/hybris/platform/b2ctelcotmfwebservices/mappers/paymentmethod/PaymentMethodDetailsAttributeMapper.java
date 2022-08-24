/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentmethod;

import de.hybris.platform.b2ctelcofacades.data.TmaCreditCardData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CreditCardType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodType;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for details attribute between {@link CCPaymentInfoData} and {@link PaymentMethodType}
 *
 * @since 1907
 */
public class PaymentMethodDetailsAttributeMapper extends TmaAttributeMapper<CCPaymentInfoData, PaymentMethodType>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CCPaymentInfoData source, PaymentMethodType target, MappingContext context)
	{
		TmaCreditCardData creditCardData = new TmaCreditCardData();
		creditCardData.setAccountHolderName(source.getAccountHolderName());
		creditCardData.setLastFourDigits(source.getCardNumber());
		creditCardData.setCardTypeData(source.getCardTypeData());

		Calendar endDateCalendar = new GregorianCalendar(Integer.parseInt(source.getExpiryYear()),
				Integer.parseInt(source.getExpiryMonth()) - 1, 1);
		endDateCalendar.set(Calendar.DAY_OF_MONTH, endDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date expirationDate = endDateCalendar.getTime();
		creditCardData.setExpirationDate(expirationDate);

		creditCardData.setLastFourDigits(source.getCardNumber());

		target.setDetails(getMapperFacade().map(creditCardData, CreditCardType.class, context));
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
