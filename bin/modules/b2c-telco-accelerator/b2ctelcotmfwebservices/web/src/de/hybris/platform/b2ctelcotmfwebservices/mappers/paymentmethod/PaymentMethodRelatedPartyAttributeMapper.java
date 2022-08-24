/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentmethod;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRefType;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for related party attribute between {@link CCPaymentInfoData} and
 * {@link PaymentMethodType}
 *
 * @since 1907
 */
public class PaymentMethodRelatedPartyAttributeMapper extends TmaAttributeMapper<CCPaymentInfoData, PaymentMethodType>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CCPaymentInfoData source, PaymentMethodType target, MappingContext context)
	{
		List<RelatedPartyRefType> relatedPartyRefTypeList = new ArrayList<>();
		relatedPartyRefTypeList.add(getMapperFacade().map(source.getUser(), RelatedPartyRefType.class, context));
		target.setRelatedParty(relatedPartyRefTypeList);
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
