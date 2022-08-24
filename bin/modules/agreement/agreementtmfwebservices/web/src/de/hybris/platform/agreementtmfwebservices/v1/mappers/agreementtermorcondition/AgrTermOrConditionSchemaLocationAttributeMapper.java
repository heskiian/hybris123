/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementtermorcondition;

import de.hybris.platform.agreementservices.model.AgrAgreementTermOrConditionModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementTermOrCondition;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link AgrAgreementTermOrConditionModel} and {@link AgreementTermOrCondition}
 *
 * @since 2108
 */
public class AgrTermOrConditionSchemaLocationAttributeMapper
		extends AgrAttributeMapper<AgrAgreementTermOrConditionModel, AgreementTermOrCondition>
{
	public AgrTermOrConditionSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementTermOrConditionModel source,
			final AgreementTermOrCondition target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(AgreementtmfwebservicesConstants.AGR_API_SCHEMA);
		}
	}
}
