/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.contactmedium;

import de.hybris.platform.partyroleservices.model.PrContactMediumModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.ContactMedium;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PrContactMediumModel} and {@link ContactMedium}
 *
 * @since 2108
 */
public class ContactMediumAtTypeAttributeMapper extends PrAttributeMapper<PrContactMediumModel, ContactMedium>
{
	public ContactMediumAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrContactMediumModel source, final ContactMedium target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
