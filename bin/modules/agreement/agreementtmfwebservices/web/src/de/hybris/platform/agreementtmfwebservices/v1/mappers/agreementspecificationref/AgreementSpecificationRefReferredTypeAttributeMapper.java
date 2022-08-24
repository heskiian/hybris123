/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecificationref;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referred type attribute between {@link AgrAgreementSpecificationModel} and {@link AgreementSpecificationRef}
 *
 * @since 2108
 */
public class AgreementSpecificationRefReferredTypeAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecificationModel, AgreementSpecificationRef>
{
	public AgreementSpecificationRefReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecificationModel source,
			final AgreementSpecificationRef target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(Config.getParameter(AgreementtmfwebservicesConstants.AGREEMENT_SPECIFICATION_DEFAULT_REFERRED));
		}
	}
}
