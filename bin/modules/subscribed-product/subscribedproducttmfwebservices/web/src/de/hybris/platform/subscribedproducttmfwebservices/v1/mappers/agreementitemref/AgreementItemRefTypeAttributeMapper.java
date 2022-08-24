/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.agreementitemref;

import de.hybris.platform.subscribedproductservices.model.SpiAgreementItemModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.AgreementItemRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link SpiAgreementItemModel} and
 * {@link AgreementItemRef}
 *
 * @since 2105
 */
public class AgreementItemRefTypeAttributeMapper extends SpiAttributeMapper<SpiAgreementItemModel, AgreementItemRef>
{
	public AgreementItemRefTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiAgreementItemModel source,
			final AgreementItemRef target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(SubscribedproducttmfwebservicesConstants.SPI_AGREEMENT_ITEM_REFERRED_TYPE);
		}
	}
}
