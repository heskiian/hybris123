/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementref;

import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link AgrAgreementModel} and {@link AgreementRef}
 *
 * @since 2108
 */
public class AgreementRefHrefAttributeMapper
		extends AgrAttributeMapper<AgrAgreementModel, AgreementRef>
{
	public AgreementRefHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementModel source,
			final AgreementRef target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(AgreementtmfwebservicesConstants.AGREEMENT_API_URL + source.getId());
		}
	}
}

