/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecificationrelationship;

import de.hybris.platform.agreementservices.model.AgrAgreementRelationshipModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationRelationship;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.springframework.util.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link AgrAgreementRelationshipModel} and
 * {@link AgreementSpecificationRelationship}
 *
 * @since 2108
 */
public class AgreementSpecRelationIdAttributeMapper extends AgrAttributeMapper<AgrAgreementRelationshipModel,
		AgreementSpecificationRelationship>
{
	public AgreementSpecRelationIdAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementRelationshipModel source,
			final AgreementSpecificationRelationship target, final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setId(source.getId());
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementSpecificationRelationship target,
			final AgrAgreementRelationshipModel source, final MappingContext context)
	{
		if (StringUtils.isEmpty(target.getId()))
		{
			return;
		}

		source.setId(target.getId());
	}
}
