/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementauthorization;

import de.hybris.platform.agreementservices.model.AgrAgreementAuthorizationModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementAuthorization;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link AgrAgreementAuthorizationModel} and {@link AgreementAuthorization}
 *
 * @since 2108
 */
public class AgreementAuthorizationAtTypeAttributeMapper
		extends AgrAttributeMapper<AgrAgreementAuthorizationModel, AgreementAuthorization>
{
	public AgreementAuthorizationAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource( final AgrAgreementAuthorizationModel source, final  AgreementAuthorization target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
