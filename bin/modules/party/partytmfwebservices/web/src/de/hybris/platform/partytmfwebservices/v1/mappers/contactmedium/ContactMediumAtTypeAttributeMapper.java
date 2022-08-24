/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.contactmedium;

import de.hybris.platform.partyservices.model.PmContactMediumModel;
import de.hybris.platform.partytmfwebservices.v1.dto.ContactMedium;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmContactMediumModel} and {@link ContactMedium}
 *
 * @since 2108
 */
public class ContactMediumAtTypeAttributeMapper extends PmAttributeMapper<PmContactMediumModel, ContactMedium>
{
	public ContactMediumAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmContactMediumModel source, final ContactMedium target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAttype(target.getClass().getSimpleName());
	}
}
