/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreement;

import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for completion data attribute between {@link AgrAgreementModel} and {@link Agreement}
 *
 * @since 2108
 */
public class AgreementCompletionDateAttributeMapper extends AgrAttributeMapper<AgrAgreementModel, Agreement>
{
	public AgreementCompletionDateAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementModel source, final Agreement target,
			final MappingContext context)
	{
		if (source.getCompletionStartDateTime() == null && source.getCompletionEndDateTime() == null)
		{
			return;
		}

		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getCompletionStartDateTime());
		timePeriod.setEndDateTime(source.getCompletionEndDateTime());
		target.setCompletionDate(timePeriod);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Agreement target, final AgrAgreementModel source,
			final MappingContext context)
	{
		if (target.getCompletionDate() == null)
		{
			return;
		}

		final TimePeriod timePeriod = target.getCompletionDate();

		if (timePeriod.getStartDateTime() != null)
		{
			source.setCompletionStartDateTime(timePeriod.getStartDateTime());
		}

		if (timePeriod.getEndDateTime() != null)
		{
			source.setCompletionEndDateTime(timePeriod.getEndDateTime());
		}

	}

}
