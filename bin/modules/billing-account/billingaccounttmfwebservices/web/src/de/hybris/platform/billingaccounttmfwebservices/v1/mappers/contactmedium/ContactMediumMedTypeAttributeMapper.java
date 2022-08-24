/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.contactmedium;

import de.hybris.platform.billingaccountservices.enums.BaContactMediumType;
import de.hybris.platform.billingaccountservices.model.BaContactMediumModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.ContactMedium;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for mediumType attribute between {@link BaContactMediumModel} and {@link ContactMedium}
 *
 * @since 2111
 */
public class ContactMediumMedTypeAttributeMapper extends BaAttributeMapper<BaContactMediumModel, ContactMedium>
{
	public ContactMediumMedTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaContactMediumModel source, final ContactMedium target,
			final MappingContext context)
	{
		// no implementation needed
	}

	@Override
	public void populateSourceAttributeFromTarget(final ContactMedium target, final BaContactMediumModel source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getMediumType()))
		{
			source.setMediumType(BaContactMediumType.valueOf(target.getMediumType()));
		}
	}

}
