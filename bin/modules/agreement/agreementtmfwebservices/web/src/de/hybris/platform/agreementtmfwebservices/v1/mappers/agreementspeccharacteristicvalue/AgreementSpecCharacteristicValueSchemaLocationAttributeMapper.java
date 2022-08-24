/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspeccharacteristicvalue;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicValueModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecCharacteristicValue;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schemaLocation attribute between {@link AgrAgreementSpecCharacteristicValueModel} and
 * {@link AgreementSpecCharacteristicValue}
 *
 * @since 2108
 */
public class AgreementSpecCharacteristicValueSchemaLocationAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecCharacteristicValueModel, AgreementSpecCharacteristicValue>
{
	public AgreementSpecCharacteristicValueSchemaLocationAttributeMapper(final String sourceAttributeName,
			final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecCharacteristicValueModel source,
			final AgreementSpecCharacteristicValue target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(AgreementtmfwebservicesConstants.AGR_API_SCHEMA);
		}
	}
}
