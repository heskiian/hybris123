/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.services.impl;

import de.hybris.platform.agreementservices.daos.AgrAgreementSpecificationDao;
import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrAgreementSpecificationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link AgrAgreementSpecificationService}
 *
 * @since 2108
 */
public class DefaultAgrAgreementSpecificationService implements AgrAgreementSpecificationService
{
	private AgrAgreementSpecificationDao agreementSpecificationDao;
	private ModelService modelService;

	public DefaultAgrAgreementSpecificationService(final AgrAgreementSpecificationDao agreementSpecificationDao,
			final ModelService modelService)
	{
		this.agreementSpecificationDao = agreementSpecificationDao;
		this.modelService = modelService;
	}

	@Override
	public AgrAgreementSpecificationModel getAgreementSpecification(final String id)
	{
		validateParameterNotNull(id, "Parameter id can not be null");
		final Map parameters = new HashMap();
		parameters.put(AgrAgreementSpecificationModel.ID, id);
		return getAgreementSpecificationDao().findUnique(parameters);
	}

	@Override
	public List<AgrAgreementSpecificationModel> getAgreementSpecifications(final AgrAgreementContext agrAgreementContext,
			final Integer offset, final Integer limit)
	{
		return getAgreementSpecificationDao().getAgreementSpecifications(agrAgreementContext, offset, limit);
	}

	@Override
	public Integer getNumberOfAgreementSpecificationsFor(final AgrAgreementContext agrAgreementContext)
	{
		return getAgreementSpecificationDao().getNumberOfAgreementSpecificationsFor(agrAgreementContext);
	}

	@Override
	public AgrAgreementSpecificationModel createAgreementSpecification()
	{
		return getModelService().create(AgrAgreementSpecificationModel.class);
	}

	@Override
	public void removeAgreementSpecification(final AgrAgreementSpecificationModel agreementSpecification)
	{
		getModelService().remove(agreementSpecification);
	}

	@Override
	public void saveAgreementSpecification(final AgrAgreementSpecificationModel agreementSpecification)
	{
		getModelService().save(agreementSpecification);
		getModelService().refresh(agreementSpecification);
	}

	protected AgrAgreementSpecificationDao getAgreementSpecificationDao()
	{
		return agreementSpecificationDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
