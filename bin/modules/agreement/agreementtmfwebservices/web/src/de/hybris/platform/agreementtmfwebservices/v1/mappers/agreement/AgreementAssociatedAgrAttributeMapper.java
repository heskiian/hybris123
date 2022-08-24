/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreement;

import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for associated agreement attribute between {@link AgrAgreementModel} and {@link Agreement}
 *
 * @since 2108
 */
public class AgreementAssociatedAgrAttributeMapper extends AgrAttributeMapper<AgrAgreementModel, Agreement>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementAssociatedAgrAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (CollectionUtils.isEmpty(source.getAssociatedAgreements()))
		{
			return;
		}

		final List<AgreementRef> agreementRefList = source.getAssociatedAgreements().stream()
				.map(agreementModel -> getMapperFacade().map(agreementModel, AgreementRef.class, context))
				.collect(Collectors.toList());

		target.setAssociatedAgreement(agreementRefList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Agreement target, final AgrAgreementModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getAssociatedAgreement()))
		{
			return;
		}

		source.setAssociatedAgreements(getAllAssociatedAgreements(target.getAssociatedAgreement(), context));
	}

	private Set<AgrAgreementModel> getAllAssociatedAgreements(final List<AgreementRef> associatedAgreements,
			final MappingContext context)
	{
		final Set<AgrAgreementModel> result = new HashSet<>();
		associatedAgreements.forEach(agreementRef -> {
			AgrAgreementModel agreementModel = (AgrAgreementModel) getAgrGenericService()
					.getItem(AgrAgreementModel._TYPECODE, agreementRef.getId());
			if (agreementModel == null)
			{
				agreementModel = (AgrAgreementModel) getAgrGenericService()
						.createItem(AgrAgreementModel.class);
			}
			getMapperFacade().map(agreementRef, agreementModel, context);
			getAgrGenericService().saveItem(agreementModel);
			result.add(agreementModel);
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
