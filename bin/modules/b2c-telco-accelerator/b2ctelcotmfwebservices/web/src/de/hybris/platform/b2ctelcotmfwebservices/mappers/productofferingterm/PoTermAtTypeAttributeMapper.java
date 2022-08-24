/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingterm;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingTerm;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attype attribute between {@link SubscriptionTermData} and {@link ProductOfferingTerm}
 *
 * @since 1903
 */
public class PoTermAtTypeAttributeMapper extends TmaAttributeMapper<SubscriptionTermData, ProductOfferingTerm>
{
	@Override
	public void populateTargetAttributeFromSource(final SubscriptionTermData source, final ProductOfferingTerm target,
			final MappingContext context)
	{
		target.setAttype(target.getClass().getSimpleName());
	}
}
