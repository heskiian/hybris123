/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecification;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for lastModified attribute between {@link AgrAgreementSpecificationModel} and
 * {@link AgreementSpecification}
 *
 * @since 2108
 */
public class AgreementSpecificationLastModifiedAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecificationModel, AgreementSpecification>
{
	public AgreementSpecificationLastModifiedAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecificationModel source, final AgreementSpecification target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getModifiedtime()))
		{
			return;
		}
		target.setLastUpdate(source.getModifiedtime());
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementSpecification target, final
	AgrAgreementSpecificationModel source, final MappingContext context)
	{
		if (target.getLastUpdate() == null)
		{
			return;
		}

		source.setModifiedtime(target.getLastUpdate());
	}

}
