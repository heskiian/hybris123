/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.party;

import de.hybris.platform.partyservices.model.PmPartyModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Party;
import de.hybris.platform.partytmfwebservices.v1.dto.TaxExemptionCertificate;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for tax exemption certificate attribute between {@link PmPartyModel} and {@link Party}
 *
 * @since 2108
 */
public class PartyTaxExemptionCertificateAttributeMapper extends PmAttributeMapper<PmPartyModel, Party>
{
	private MapperFacade mapperFacade;

	public PartyTaxExemptionCertificateAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmPartyModel source, final Party target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getTaxExemptionCertificates()))
		{
			return;
		}

		target.setTaxExemptionCertificate(
				getMapperFacade().mapAsList(source.getTaxExemptionCertificates(), TaxExemptionCertificate.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
