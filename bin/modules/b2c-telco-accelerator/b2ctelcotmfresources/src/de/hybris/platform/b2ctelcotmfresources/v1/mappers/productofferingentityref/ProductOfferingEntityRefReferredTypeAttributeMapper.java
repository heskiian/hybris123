/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfresources.v1.mappers.productofferingentityref;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcotmfresources.constants.B2ctelcotmfresourcesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.EntityRef;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link TmaProductOfferingModel} and
 * {@link EntityRef}
 *
 * @since 2105
 */
public class ProductOfferingEntityRefReferredTypeAttributeMapper extends TmaAttributeMapper<TmaProductOfferingModel, EntityRef>
{

	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingModel source, final EntityRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setAtreferredType(Config.getParameter(B2ctelcotmfresourcesConstants.TMA_PRODUCT_OFFERING_DEFAULT_REFERRED));
		}
	}
}
