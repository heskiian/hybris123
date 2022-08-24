/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.productref;

import de.hybris.platform.agreementservices.model.AgrProductModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.ProductRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link AgrProductModel} and {@link ProductRef}
 *
 * @since 2108
 */
public class ProductRefHrefAttributeMapper
		extends AgrAttributeMapper<AgrProductModel, ProductRef>
{
	public ProductRefHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrProductModel source, final ProductRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(AgreementtmfwebservicesConstants.PRODUCT_API_URL + source.getId());
		}
	}
}
