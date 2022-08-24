/*
 *	Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.productoffering.populator;

import de.hybris.platform.b2ctelcofacades.data.ProductOfferingSearchContextData;
import de.hybris.platform.b2ctelcoservices.data.TmaProductOfferingSearchContext;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Product Offering Search Context Populator for converting the ProductOfferingSearchContextData into
 * TmaProductOfferingSearchContext.
 *
 * @since 1907
 */
public class CommerceProductOfferingSearchContextPopulator
		implements Populator<ProductOfferingSearchContextData, TmaProductOfferingSearchContext>
{
	@Override
	public void populate(final ProductOfferingSearchContextData source, final TmaProductOfferingSearchContext target)
			throws ConversionException
	{
		target.setCatalog(source.getCatalog());
		target.setVersion(source.getVersion());
		target.setTimestamp(source.getTimestamp());
		target.setPageSize(source.getPageSize());
		target.setCurrentPage(source.getCurrentPage());
	}

}
