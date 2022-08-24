/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreement;

import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for agreement specification attribute between {@link AgrAgreementModel} and {@link Agreement}
 *
 * @since 2108
 */
public class AgreementAgrSpecificationAttributeMapper extends AgrAttributeMapper<AgrAgreementModel, Agreement>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementAgrSpecificationAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (ObjectUtils.isEmpty(source.getAgreementSpecification()))
		{
			return;
		}

		target.setAgreementSpecification(
				getMapperFacade().map(source.getAgreementSpecification(), AgreementSpecificationRef.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final Agreement target, final AgrAgreementModel source,
			final MappingContext context)
	{
		if (target.getAgreementSpecification() == null)
		{
			return;
		}

		AgrAgreementSpecificationModel agreementSpecificationModel = (AgrAgreementSpecificationModel) getAgrGenericService()
				.getItem(AgrAgreementSpecificationModel._TYPECODE, target.getAgreementSpecification().getId());
		if (agreementSpecificationModel == null)
		{
			agreementSpecificationModel = (AgrAgreementSpecificationModel) getAgrGenericService()
					.createItem(AgrAgreementSpecificationModel.class);
			agreementSpecificationModel.setId(target.getAgreementSpecification().getId());
		}

		getMapperFacade().map(target.getAgreementSpecification(), agreementSpecificationModel, context);
		getAgrGenericService().saveItem(agreementSpecificationModel);
		source.setAgreementSpecification(agreementSpecificationModel);
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
