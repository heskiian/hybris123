/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementitem;

import de.hybris.platform.agreementservices.model.AgrAgreementItemModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementItem;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link AgrAgreementItemModel} and {@link AgreementItem}
 *
 * @since 2108
 */
public class AgreementItemAtTypeAttributeMapper extends AgrAttributeMapper<AgrAgreementItemModel, AgreementItem>
{
	public AgreementItemAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementItemModel source, final AgreementItem target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
