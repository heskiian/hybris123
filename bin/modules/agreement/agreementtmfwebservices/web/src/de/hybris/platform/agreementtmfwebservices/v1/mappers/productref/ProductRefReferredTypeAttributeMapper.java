/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.productref;

import de.hybris.platform.agreementservices.model.AgrProductModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.ProductRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referred type attribute between {@link AgrProductModel} and {@link ProductRef}
 *
 * @since 2108
 */
public class ProductRefReferredTypeAttributeMapper
		extends AgrAttributeMapper<AgrProductModel, ProductRef>
{
	public ProductRefReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrProductModel source, final ProductRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(Config.getParameter(AgreementtmfwebservicesConstants.PRODUCT_DEFAULT_REFERRED));
		}
	}
}
