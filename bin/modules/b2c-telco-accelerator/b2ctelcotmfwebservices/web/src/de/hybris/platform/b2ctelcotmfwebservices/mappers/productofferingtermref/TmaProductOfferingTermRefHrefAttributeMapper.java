/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingtermref;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingTermRef;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link SubscriptionTermData} and {@link ProductOfferingTermRef}
 *
 * @since 1907
 */
public class TmaProductOfferingTermRefHrefAttributeMapper extends TmaAttributeMapper<SubscriptionTermData,
	 ProductOfferingTermRef>
{
	 @Override
	 public void populateTargetAttributeFromSource(SubscriptionTermData source, ProductOfferingTermRef target,
		  MappingContext context)
	 {
		  if (StringUtils.isNotEmpty(source.getId()))
		  {
				target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_TERM_API_URL + source.getId());
		  }

	 }
}
