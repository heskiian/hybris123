/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspeccharacteristicvalue;

import de.hybris.platform.agreementservices.enums.AgrAgreementSpecCharValueType;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicValueModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecCharacteristicValue;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valueType attribute between {@link AgrAgreementSpecCharacteristicValueModel} and
 * {@link AgreementSpecCharacteristicValue}
 *
 * @since 2111
 */
public class AgreementSpecCharacteristicValueValueTypeAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecCharacteristicValueModel, AgreementSpecCharacteristicValue>
{
	public AgreementSpecCharacteristicValueValueTypeAttributeMapper(final String sourceAttributeName,
			final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecCharacteristicValueModel source,
			final AgreementSpecCharacteristicValue target, final MappingContext context)
	{
		//no implementation needed
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementSpecCharacteristicValue target,
			final AgrAgreementSpecCharacteristicValueModel source, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getValueType()))
		{
			source.setValueType(AgrAgreementSpecCharValueType.valueOf(target.getValueType().toUpperCase()));
		}
	}

}

