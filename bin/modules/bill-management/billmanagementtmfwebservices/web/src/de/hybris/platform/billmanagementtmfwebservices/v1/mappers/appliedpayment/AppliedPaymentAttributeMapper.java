/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.appliedpayment;

import de.hybris.platform.billmanagementservices.model.BmPartyPaymentItemModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.AppliedPayment;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.PaymentRef;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for payment attribute between {@link BmPartyPaymentItemModel} and {@link AppliedPayment}
 *
 * @since 2108
 */
public class AppliedPaymentAttributeMapper extends BmAttributeMapper<BmPartyPaymentItemModel, AppliedPayment>
{
	private MapperFacade mapperFacade;

	public AppliedPaymentAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyPaymentItemModel source, final AppliedPayment target,
			final MappingContext context)
	{
		if (source.getPayment() != null)
		{
			target.setPayment(getMapperFacade().map(source.getPayment(), PaymentRef.class, context));
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
