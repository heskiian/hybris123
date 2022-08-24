/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.converters.Populator;


/**
 * Populates {@link TmaSubscriptionBaseModel} from a {@link TmaSubscriptionBaseData} entity.
 *
 * @since 1907
 */
public class TmaSubscriptionBaseReversePopulator implements Populator<TmaSubscriptionBaseData, TmaSubscriptionBaseModel>
{
	 @Override
	 public void populate(final TmaSubscriptionBaseData source, final TmaSubscriptionBaseModel target)
	 {
		  target.setSubscriberIdentity(source.getSubscriberIdentity());
		  target.setBillingSystemId(source.getBillingSystemId());
		  target.setBillingAccount(createBillingAccountFromSource(source.getTmaBillingAccountData()));
	 }

	 /**
	  * Creates and returns a billing account model from billing account data.
	  *
	  * @param billingAccountData
	  * 		the billing account data used to populate the billing account model
	  * @return {@link TmaBillingAccountModel}
	  */
	 protected TmaBillingAccountModel createBillingAccountFromSource(final TmaBillingAccountData billingAccountData)
	 {
		  if (billingAccountData == null)
		  {
				return null;
		  }
		  final TmaBillingAccountModel billingAccountModel = new TmaBillingAccountModel();

		  billingAccountModel.setBillingAccountId(billingAccountData.getBillingAccountId());
		  billingAccountModel.setBillingSystemId(billingAccountData.getBillingSystemId());

		  return billingAccountModel;
	 }
}
