/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Populates {@link TmaSubscriptionBaseData} from a {@link TmaSubscriptionBaseModel} entity.
 *
 * @since 6.6
 */
public class TmaSubscriptionBasePopulator implements Populator<TmaSubscriptionBaseModel, TmaSubscriptionBaseData>
{
	/**
	 * Separator
	 */
	private static final String SUBSCRIPTION_IDENTITY_BILLING_SYSTEM_SEPARATOR = "__";

	private final Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessRefConverter;
	private final Converter<TmaSubscriptionBaseModel, ProductData> tmaProductSubscriptionBaseConverter;

	public TmaSubscriptionBasePopulator(
			final Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessRefConverter,
			final Converter<TmaSubscriptionBaseModel, ProductData> tmaProductSubscriptionBaseConverter)
	{
		this.tmaSubscriptionAccessRefConverter = tmaSubscriptionAccessRefConverter;
		this.tmaProductSubscriptionBaseConverter = tmaProductSubscriptionBaseConverter;
	}

	@Override
	public void populate(final TmaSubscriptionBaseModel source, final TmaSubscriptionBaseData target)
	{
		if (StringUtils.isNotEmpty(source.getSubscriberIdentity()) && StringUtils.isNotEmpty(source.getBillingSystemId()))
		{
			target.setCode(
					source.getSubscriberIdentity() + SUBSCRIPTION_IDENTITY_BILLING_SYSTEM_SEPARATOR + source.getBillingSystemId());
		}
		target.setSubscriberIdentity(source.getSubscriberIdentity());
		target.setBillingSystemId(source.getBillingSystemId());
		target.setTmaBillingAccountData(createBillingAccountDataFromSource(source.getBillingAccount()));
		if (source.getSubscriptionAccesses() != null)
		{
			target.setSubscriptionAccesses(getTmaSubscriptionAccessRefConverter().convertAll(source.getSubscriptionAccesses()));
		}
		if (CollectionUtils.isNotEmpty(source.getSubscribedProducts()))
		{
			target.setProductOfferingRef(getTmaProductSubscriptionBaseConverter().convert(source));
		}
	}

	protected TmaBillingAccountData createBillingAccountDataFromSource(final TmaBillingAccountModel billingAccountModel)
	{
		if (billingAccountModel == null)
		{
			return null;
		}
		final TmaBillingAccountData billingAccountData = new TmaBillingAccountData();
		billingAccountData.setBillingAccountId(billingAccountModel.getBillingAccountId());
		billingAccountData.setBillingSystemId(billingAccountModel.getBillingSystemId());
		return billingAccountData;
	}

	protected Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> getTmaSubscriptionAccessRefConverter()
	{
		return tmaSubscriptionAccessRefConverter;
	}

	protected Converter<TmaSubscriptionBaseModel, ProductData> getTmaProductSubscriptionBaseConverter()
	{
		return tmaProductSubscriptionBaseConverter;
	}

}
