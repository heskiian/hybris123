/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.taxexemptioncertificate;

import de.hybris.platform.partyservices.model.PmTaxExemptionCertificateModel;
import de.hybris.platform.partytmfwebservices.v1.dto.TaxDefinition;
import de.hybris.platform.partytmfwebservices.v1.dto.TaxExemptionCertificate;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for tax definition attribute between {@link PmTaxExemptionCertificateModel} and {@link TaxExemptionCertificate}
 *
 * @since 2108
 */
public class TaxExemptionCertificateTaxDefinitionAttributeMapper
		extends PmAttributeMapper<PmTaxExemptionCertificateModel, TaxExemptionCertificate>
{
	private MapperFacade mapperFacade;

	public TaxExemptionCertificateTaxDefinitionAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmTaxExemptionCertificateModel source,
			final TaxExemptionCertificate target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getTaxDefinitions()))
		{
			return;
		}

		target.setTaxDefinition(getMapperFacade().mapAsList(source.getTaxDefinitions(), TaxDefinition.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
