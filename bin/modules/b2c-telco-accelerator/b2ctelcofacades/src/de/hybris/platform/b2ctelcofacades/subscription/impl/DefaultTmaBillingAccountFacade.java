/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import de.hybris.platform.b2ctelcofacades.data.CreateTmaBillingAccountRequest;
import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaBillingAccountFacade;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingAccountService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaBillingAccountFacade}.
 *
 * @since 6.6
 */
public class DefaultTmaBillingAccountFacade implements TmaBillingAccountFacade
{
	private TmaBillingAccountService tmaBillingAccountService;
	private Converter<TmaBillingAccountModel, TmaBillingAccountData> tmaBillingAccountDataConverter;

	@Override
	public TmaBillingAccountData createBillingAccount(final CreateTmaBillingAccountRequest billingAccountRequest)
	{
		TmaBillingAccountModel billingAccountModel;
		if (StringUtils.isNotBlank(billingAccountRequest.getParentBillingAccountId()))
		{
			billingAccountModel = getTmaBillingAccountService().createBillingAccountWithParentAccount(
					billingAccountRequest.getBillingSystemId(), billingAccountRequest.getBillingAccountId(),
					billingAccountRequest.getParentBillingAccountId());
		}
		else
		{
			billingAccountModel = getTmaBillingAccountService().createBillingAccount(billingAccountRequest.getBillingSystemId(),
					billingAccountRequest.getBillingAccountId());
		}
		return getTmaBillingAccountDataConverter().convert(billingAccountModel);
	}

	@Override
	public void deleteBillingAccount(final String billingSystemId, final String billingAccountId)
	{
		getTmaBillingAccountService().deleteBillingAccount(billingSystemId, billingAccountId);
	}

	@Override
	public TmaBillingAccountData generateBillingAccount()
	{
		return getTmaBillingAccountDataConverter().convert(getTmaBillingAccountService().generateBillingAccount());
	}

	public TmaBillingAccountService getTmaBillingAccountService()
	{
		return tmaBillingAccountService;
	}

	@Required
	public void setTmaBillingAccountService(final TmaBillingAccountService tmaBillingAccountService)
	{
		this.tmaBillingAccountService = tmaBillingAccountService;
	}

	public Converter<TmaBillingAccountModel, TmaBillingAccountData> getTmaBillingAccountDataConverter()
	{
		return tmaBillingAccountDataConverter;
	}

	@Required
	public void setTmaBillingAccountDataConverter(
			final Converter<TmaBillingAccountModel, TmaBillingAccountData> tmaBillingAccountDataConverter)
	{
		this.tmaBillingAccountDataConverter = tmaBillingAccountDataConverter;
	}
}
