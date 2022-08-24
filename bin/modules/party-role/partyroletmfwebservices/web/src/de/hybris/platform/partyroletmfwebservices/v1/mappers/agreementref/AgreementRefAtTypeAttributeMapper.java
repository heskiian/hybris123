/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.agreementref;

import de.hybris.platform.partyroleservices.model.PrBusinessInteractionModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.AgreementRef;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PrBusinessInteractionModel} and {@link AgreementRef}
 *
 * @since 2108
 */
public class AgreementRefAtTypeAttributeMapper extends PrAttributeMapper<PrBusinessInteractionModel, AgreementRef>
{
	public AgreementRefAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrBusinessInteractionModel source, final AgreementRef target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAttype(target.getClass().getSimpleName());
	}
}
