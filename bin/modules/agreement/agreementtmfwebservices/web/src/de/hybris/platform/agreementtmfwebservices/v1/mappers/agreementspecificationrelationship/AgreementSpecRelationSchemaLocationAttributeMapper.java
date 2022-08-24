/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecificationrelationship;

import de.hybris.platform.agreementservices.model.AgrAgreementRelationshipModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationRelationship;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schemaLocation attribute between {@link AgrAgreementRelationshipModel} and
 * {@link AgreementSpecificationRelationship}
 *
 * @since 2108
 */
public class AgreementSpecRelationSchemaLocationAttributeMapper
		extends AgrAttributeMapper<AgrAgreementRelationshipModel, AgreementSpecificationRelationship>
{
	public AgreementSpecRelationSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementRelationshipModel source,
			final AgreementSpecificationRelationship target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.atschemaLocation(AgreementtmfwebservicesConstants.AGR_API_SCHEMA);
		}
	}
}
