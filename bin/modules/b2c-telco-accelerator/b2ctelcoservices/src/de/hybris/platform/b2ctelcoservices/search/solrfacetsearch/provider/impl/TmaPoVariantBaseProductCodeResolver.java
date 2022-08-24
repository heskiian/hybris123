/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;


/**
 * Field Value Provider for indexing base product code of the product offering variant.
 *
 * @since 1810
 */
public class TmaPoVariantBaseProductCodeResolver extends TmaAbstractPoVariantBaseProductDetailsResolver
{
	@Override
	protected Object getValue(final TmaSimpleProductOfferingModel baseSpo)
	{
		return baseSpo.getCode();
	}
}
