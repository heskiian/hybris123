/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreement;

import de.hybris.platform.agreementservices.enums.AgrAgreementType;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for agreementType attribute between {@link AgrAgreementModel} and {@link Agreement}
 *
 * @since 2111
 */
public class AgreementTypeAttributeMapper extends AgrAttributeMapper<AgrAgreementModel, Agreement>
{
	public AgreementTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementModel source, final Agreement target,
			final MappingContext context)
	{
		//no implementation needed
	}

	@Override
	public void populateSourceAttributeFromTarget(final Agreement target, final AgrAgreementModel source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getAgreementType()))
		{
			source.setAgreementType(AgrAgreementType.valueOf(target.getAgreementType().toUpperCase()));
		}
	}
}
