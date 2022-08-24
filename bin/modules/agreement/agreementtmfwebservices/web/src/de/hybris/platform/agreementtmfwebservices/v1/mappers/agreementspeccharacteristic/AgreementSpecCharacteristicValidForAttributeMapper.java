/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspeccharacteristic;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecCharacteristic;
import de.hybris.platform.agreementtmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link AgrAgreementSpecCharacteristicModel} and {@link AgreementSpecCharacteristic}
 *
 * @since 2108
 */
public class AgreementSpecCharacteristicValidForAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecCharacteristicModel, AgreementSpecCharacteristic>
{
	public AgreementSpecCharacteristicValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecCharacteristicModel source,
			final AgreementSpecCharacteristic target, final MappingContext context)
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
	public void populateSourceAttributeFromTarget(final AgreementSpecCharacteristic target, final
	AgrAgreementSpecCharacteristicModel source, final MappingContext context)
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

