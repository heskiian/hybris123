/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.contactmedium;

import de.hybris.platform.billingaccountservices.model.BaContactMediumModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.ContactMedium;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link BaContactMediumModel} and {@link ContactMedium}
 *
 * @since 2105
 */
public class ContactMediumValidForAttributeMapper extends BaAttributeMapper<BaContactMediumModel, ContactMedium>
{
	public ContactMediumValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaContactMediumModel source, final ContactMedium target,
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
	public void populateSourceAttributeFromTarget(final ContactMedium target, final BaContactMediumModel source,
			final MappingContext context)
	{
		if (target.getValidFor() == null)
		{
			return;
		}

		source.setStartDateTime(target.getValidFor().getStartDateTime());
		source.setEndDateTime(target.getValidFor().getEndDateTime());
	}
}
