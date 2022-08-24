/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.contact;

import de.hybris.platform.billingaccountservices.enums.BaContactType;
import de.hybris.platform.billingaccountservices.model.BaContactModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Contact;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for contactType attribute between {@link BaContactModel} and {@link Contact}
 *
 * @since 2111
 */
public class ContactContTypeAttributeMapper extends BaAttributeMapper<BaContactModel, Contact>
{
	public ContactContTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaContactModel source, final Contact target,
			final MappingContext context)
	{
		// no implementation needed
	}

	@Override
	public void populateSourceAttributeFromTarget(final Contact target, final BaContactModel source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getContactType()))
		{
			source.setContactType(BaContactType.valueOf(target.getContactType().toUpperCase()));
		}
	}
}
