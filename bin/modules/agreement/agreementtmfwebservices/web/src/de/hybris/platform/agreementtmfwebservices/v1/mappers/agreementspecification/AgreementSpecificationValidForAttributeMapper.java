/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecification;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link AgrAgreementSpecificationModel} and {@link AgreementSpecification}
 *
 * @since 2108
 */
public class AgreementSpecificationValidForAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecificationModel, AgreementSpecification>
{
	public AgreementSpecificationValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecificationModel source, final AgreementSpecification target,
			final MappingContext context)
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
	public void populateSourceAttributeFromTarget(final AgreementSpecification target, final
	AgrAgreementSpecificationModel source, final MappingContext context)
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
