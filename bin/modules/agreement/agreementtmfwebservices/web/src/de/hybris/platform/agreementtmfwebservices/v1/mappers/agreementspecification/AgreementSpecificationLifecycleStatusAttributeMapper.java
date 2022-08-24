/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecification;

import de.hybris.platform.agreementservices.enums.AgrAgreementLifecycleStatus;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.springframework.util.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for LifecycleStatus attribute between {@link AgrAgreementSpecificationModel} and {@link AgreementSpecification}
 *
 * @since 2111
 */
public class AgreementSpecificationLifecycleStatusAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecificationModel, AgreementSpecification>
{
	public AgreementSpecificationLifecycleStatusAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecificationModel source, final AgreementSpecification target,
			final MappingContext context)
	{
		//no implementation needed
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementSpecification target, final
	AgrAgreementSpecificationModel source, final MappingContext context)
	{
		if (StringUtils.isEmpty(target.getLifecycleStatus()))
		{
			return;
		}

		source.setLifecycleStatus(AgrAgreementLifecycleStatus.valueOf(target.getLifecycleStatus().toUpperCase()));
	}

}
