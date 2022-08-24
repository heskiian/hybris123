/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.model;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import org.springframework.util.ObjectUtils;


/**
 * AttributeHandler for dynamic attribute productSpecType. Product Specification
 * Type For The Subscription Price Plan.
 * 
 * @since 1911
 */
public class TmaProductSpecTypeAttrHandler
		extends AbstractDynamicAttributeHandler<TmaProductSpecTypeModel, SubscriptionPricePlanModel>
{
	@Override
	public TmaProductSpecTypeModel get(final SubscriptionPricePlanModel subscriptionPricePlan)
	{

		final TmaProductSpecTypeModel productSpecType = null;
		if (ObjectUtils.isEmpty(subscriptionPricePlan))
		{
			return productSpecType;
		}
		else
		{
			final TmaProductOfferingModel product = (TmaProductOfferingModel) subscriptionPricePlan.getProduct();
			if (product instanceof TmaBundledProductOfferingModel)
			{
				return getProductType(subscriptionPricePlan.getAffectedProductOffering());
			}
			else
			{
				return getProductType(product);
			}
		}
	}

	private TmaProductSpecTypeModel getProductType(final TmaProductOfferingModel product)
	{
		TmaProductSpecTypeModel productSpecType = null;
		final TmaProductSpecificationModel poSpecification = product.getProductSpecification();
		if (!ObjectUtils.isEmpty(poSpecification) && !ObjectUtils.isEmpty(poSpecification.getType()))
		{
			productSpecType = poSpecification.getType();
		}
		return productSpecType;
	}

	@Override
	public void set(final SubscriptionPricePlanModel model, final TmaProductSpecTypeModel productSpecType)
	{
		throw new UnsupportedOperationException();
	}
}
