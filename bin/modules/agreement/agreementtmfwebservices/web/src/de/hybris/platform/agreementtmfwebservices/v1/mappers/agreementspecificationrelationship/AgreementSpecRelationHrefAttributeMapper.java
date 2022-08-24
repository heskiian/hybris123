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
 * This attribute Mapper class maps data for href attribute between {@link AgrAgreementRelationshipModel} and
 * {@link AgreementSpecificationRelationship}
 *
 * @since 2108
 */
public class AgreementSpecRelationHrefAttributeMapper
		extends AgrAttributeMapper<AgrAgreementRelationshipModel, AgreementSpecificationRelationship>
{
	public AgreementSpecRelationHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementRelationshipModel source,
			final AgreementSpecificationRelationship target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(AgreementtmfwebservicesConstants.AGREEMENT_SPECIFICATION_RELATIONSHIP_API_URL + source.getId());
		}
	}
}
