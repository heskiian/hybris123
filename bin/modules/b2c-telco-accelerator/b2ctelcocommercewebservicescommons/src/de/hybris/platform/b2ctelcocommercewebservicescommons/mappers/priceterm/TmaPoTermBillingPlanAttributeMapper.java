/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.priceterm;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.BillingPlanWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for Billing Plan attribute between {@link SubscriptionTermData} and
 * {@link ProductOfferingTermWsDTO}
 *
 * @since 1911
 */
public class TmaPoTermBillingPlanAttributeMapper extends TmaAttributeMapper<SubscriptionTermData, ProductOfferingTermWsDTO>
{

	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final SubscriptionTermData source, final ProductOfferingTermWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (ObjectUtils.isEmpty(source.getBillingPlan()))
		{
			return;
		}
		final BillingPlanWsDTO billingPlan = getMapperFacade().map(source.getBillingPlan(), BillingPlanWsDTO.class, context);
		target.setBillingPlan(billingPlan);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
