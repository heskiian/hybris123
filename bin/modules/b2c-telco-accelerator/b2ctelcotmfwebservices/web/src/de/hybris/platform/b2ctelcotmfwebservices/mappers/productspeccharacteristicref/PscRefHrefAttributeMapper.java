/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspeccharacteristicref;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristicRef;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for href attribute between {@link
 * TmaProductSpecCharacteristicData} and {@link ProductSpecCharacteristicRef}
 *
 * @since 1907
 */
public class PscRefHrefAttributeMapper extends TmaAttributeMapper<TmaProductSpecCharacteristicData, ProductSpecCharacteristicRef>
{

	@Override
	public void populateTargetAttributeFromSource(TmaProductSpecCharacteristicData source, ProductSpecCharacteristicRef target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_SPEC_CHARACTERISTIC_API_URL + source.getId());
		}
	}
}
