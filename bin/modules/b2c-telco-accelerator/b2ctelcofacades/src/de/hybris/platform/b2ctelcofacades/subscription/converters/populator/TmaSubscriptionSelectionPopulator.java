/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionSelectionData;
import de.hybris.platform.b2ctelcoservices.enums.TmaServiceType;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAgreementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.converters.Populator;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Populates {@link TmaSubscriptionSelectionData} from a {@link TmaSubscriptionBaseModel} entity.
 *
 * @since 6.7
 */
public class TmaSubscriptionSelectionPopulator implements Populator<TmaSubscriptionBaseModel, TmaSubscriptionSelectionData>
{
	@Override
	public void populate(final TmaSubscriptionBaseModel source, final TmaSubscriptionSelectionData target)
	{
		populatePoCodes(source, target);
		target.setSubscriberIdentity(source.getSubscriberIdentity());
		populateBillingAgreement(source, target);
	}

	private void populatePoCodes(final TmaSubscriptionBaseModel source, final TmaSubscriptionSelectionData target)
	{
		final List<TmaSubscribedProductModel> tariffPlans = source.getSubscribedProducts().stream()
				.filter(s -> TmaServiceType.TARIFF_PLAN.equals(s.getServiceType())).collect(Collectors.toList());
		if (tariffPlans.isEmpty())
		{
			target.setProductCode("");
			target.setBpoCode("");
		}
		else
		{
			final TmaSubscribedProductModel mainTariff = tariffPlans.get(0);
			target.setProductCode(mainTariff.getProductCode() != null ? mainTariff.getProductCode() : "");
			target.setBpoCode(mainTariff.getBundledProductCode() != null ? mainTariff.getBundledProductCode() : "");
			target.setBillingSystemId(source.getBillingSystemId());
		}
	}

	private void populateBillingAgreement(final TmaSubscriptionBaseModel source, final TmaSubscriptionSelectionData target)
	{
		final TmaBillingAgreementModel billingAgreement = source.getBillAgreement();
		if (billingAgreement != null)
		{
			target.setBillingAgreementId(billingAgreement.getId());
		}
	}
}
