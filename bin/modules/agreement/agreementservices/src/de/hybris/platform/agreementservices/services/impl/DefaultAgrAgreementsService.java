/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.services.impl;

import de.hybris.platform.agreementservices.daos.AgrAgreementDao;
import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.services.AgrAgreementsService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link AgrAgreementsService}.
 *
 * @since 2108
 */
public class DefaultAgrAgreementsService implements AgrAgreementsService
{
	private AgrAgreementDao agreementDao;
	private ModelService modelService;

	public DefaultAgrAgreementsService(final AgrAgreementDao agreementDao, final ModelService modelService)
	{
		this.agreementDao = agreementDao;
		this.modelService = modelService;
	}

	@Override
	public AgrAgreementModel getAgreement(final String id)
	{
		validateParameterNotNull(id, "Parameter id can not be null");
		final Map parameters = new HashMap();
		parameters.put(AgrAgreementModel.ID, id);
		return getAgreementDao().findUnique(parameters);
	}

	@Override
	public List<AgrAgreementModel> getAgreements(final AgrAgreementContext agrAgreementContext,
			final Integer offset, final Integer limit)
	{
		return getAgreementDao().getAgreements(agrAgreementContext, offset, limit);
	}

	@Override
	public Integer getNumberOfAgreementsFor(final AgrAgreementContext agrAgreementContext)
	{
		return getAgreementDao().getNumberOfAgreementsFor(agrAgreementContext);
	}

	@Override
	public AgrAgreementModel createAgreement()
	{
		return getModelService().create(AgrAgreementModel.class);
	}

	@Override
	public void removeAgreement(final AgrAgreementModel agreement)
	{
		getModelService().remove(agreement);
	}

	@Override
	public void saveAgreement(final AgrAgreementModel agreement)
	{
		getModelService().save(agreement);
		getModelService().refresh(agreement);
	}


	protected AgrAgreementDao getAgreementDao()
	{
		return agreementDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
