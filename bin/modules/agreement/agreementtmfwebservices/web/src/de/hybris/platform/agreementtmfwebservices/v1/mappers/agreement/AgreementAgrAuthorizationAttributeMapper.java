/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreement;

import de.hybris.platform.agreementservices.model.AgrAgreementAuthorizationModel;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementAuthorization;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for agreement authorization attribute between {@link AgrAgreementModel} and {@link Agreement}
 *
 * @since 2108
 */
public class AgreementAgrAuthorizationAttributeMapper extends AgrAttributeMapper<AgrAgreementModel, Agreement>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementAgrAuthorizationAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementModel source, final Agreement target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getAgreementAuthorizations()))
		{
			return;
		}

		final List<AgreementAuthorization> agreementAuthorizationList = source.getAgreementAuthorizations().stream()
				.map(authorizationModel -> getMapperFacade().map(authorizationModel, AgreementAuthorization.class, context))
				.collect(Collectors.toList());

		target.setAgreementAuthorization(agreementAuthorizationList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Agreement target, final AgrAgreementModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getAgreementAuthorization()))
		{
			return;
		}

		source.setAgreementAuthorizations(getAllAgreementAuthorizations(target.getAgreementAuthorization(), context));
	}

	private Set<AgrAgreementAuthorizationModel> getAllAgreementAuthorizations(
			final List<AgreementAuthorization> agreementAuthorizations, final MappingContext context)
	{
		final Set<AgrAgreementAuthorizationModel> result = new HashSet<>();
		agreementAuthorizations.forEach(agreementAuthorization -> {
			final AgrAgreementAuthorizationModel authorizationModel = (AgrAgreementAuthorizationModel) getAgrGenericService()
					.createItem(AgrAgreementAuthorizationModel.class);
			getMapperFacade().map(agreementAuthorization, authorizationModel, context);
			getAgrGenericService().saveItem(authorizationModel);
			result.add(authorizationModel);
		});
		return result;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}
}
