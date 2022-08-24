/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.europe1.model.PriceRowModel;


/**
 * @since 1810
 */
public interface TmaChargeValueSource
{
	Double getPrice(PriceRowModel price, String valueProviderParameter);
}
