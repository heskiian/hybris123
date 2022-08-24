/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.productofferingref;

import de.hybris.platform.agreementservices.model.AgrProductOfferingModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.ProductOfferingRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referred type attribute between {@link AgrProductOfferingModel} and {@link ProductOfferingRef}
 *
 * @since 2108
 */
public class ProductOfferingRefReferredTypeAttributeMapper
		extends AgrAttributeMapper<AgrProductOfferingModel, ProductOfferingRef>
{
	public ProductOfferingRefReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrProductOfferingModel source, final ProductOfferingRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(Config.getParameter(AgreementtmfwebservicesConstants.PRODUCT_OFFERING_DEFAULT_REFERRED));
		}
	}
}
