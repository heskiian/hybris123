/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.characteristic;


import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharValueUseModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Characteristic;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link AgrAgreementSpecCharValueUseModel} and {@link Characteristic}
 *
 * @since 2108
 */
public class CharacteristicAtTypeAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecCharValueUseModel, Characteristic>
{
	public CharacteristicAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecCharValueUseModel source, final Characteristic target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
