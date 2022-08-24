/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.taxexemptioncertificate;

import de.hybris.platform.partyservices.model.PmTaxExemptionCertificateModel;
import de.hybris.platform.partytmfwebservices.v1.dto.TaxExemptionCertificate;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmTaxExemptionCertificateModel} and {@link TaxExemptionCertificate}
 *
 * @since 2108
 */
public class TaxExemptionCertificateAtTypeAttributeMapper
		extends PmAttributeMapper<PmTaxExemptionCertificateModel, TaxExemptionCertificate>
{
	public TaxExemptionCertificateAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmTaxExemptionCertificateModel source,
			final TaxExemptionCertificate target, final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAttype(target.getClass().getSimpleName());
	}
}
