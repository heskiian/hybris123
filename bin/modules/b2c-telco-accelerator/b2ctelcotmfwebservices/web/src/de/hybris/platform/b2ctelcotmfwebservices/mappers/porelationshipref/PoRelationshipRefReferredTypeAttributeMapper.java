/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.porelationshipref;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PoRelationshipRef;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.util.Config;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for referred type attribute between {@link ProductData} and {@link PoRelationshipRef}
 *
 * @since 1903
 */
public class PoRelationshipRefReferredTypeAttributeMapper extends TmaAttributeMapper<ProductData, PoRelationshipRef>
{

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final PoRelationshipRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setAtreferredType(Config.getParameter(B2ctelcotmfwebservicesConstants.TMA_PORELATIONSHIP_DEFAULT_REFERRED));
		}
	}
}
