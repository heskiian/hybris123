/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspeccharacteristicvalue;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicValueModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecCharacteristicValue;
import de.hybris.platform.agreementtmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link AgrAgreementSpecCharacteristicValueModel} and {@link AgreementSpecCharacteristicValue}
 *
 * @since 2108
 */
public class AgreementSpecCharacteristicValueValidForAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecCharacteristicValueModel, AgreementSpecCharacteristicValue>
{
	public AgreementSpecCharacteristicValueValidForAttributeMapper(final String sourceAttributeName,
			final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecCharacteristicValueModel source,
			final AgreementSpecCharacteristicValue target, final MappingContext context)
	{
		if (source.getStartDateTime() == null && source.getEndDateTime() == null)
		{
			return;
		}

		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getStartDateTime());
		timePeriod.setEndDateTime(source.getEndDateTime());
		target.setValidFor(timePeriod);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementSpecCharacteristicValue target, final
	AgrAgreementSpecCharacteristicValueModel source, final MappingContext context)
	{
		if (target.getValidFor() == null)
		{
			return;
		}

		final TimePeriod timePeriod = target.getValidFor();

		if (timePeriod.getStartDateTime() != null)
		{
			source.setStartDateTime(timePeriod.getStartDateTime());
		}

		if (timePeriod.getEndDateTime() != null)
		{
			source.setEndDateTime(timePeriod.getEndDateTime());
		}

	}
}
