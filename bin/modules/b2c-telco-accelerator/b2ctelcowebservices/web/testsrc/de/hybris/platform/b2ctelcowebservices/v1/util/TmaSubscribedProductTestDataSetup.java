/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v1.util;

import de.hybris.platform.b2ctelcofacades.data.SubscriptionStatusData;
import de.hybris.platform.b2ctelcofacades.data.TmaServiceTypeData;
import de.hybris.platform.b2ctelcowebservices.dto.TmaSubscribedProductWsDto;
import de.hybris.platform.b2ctelcowebservices.dto.UpdatableTmaSubscribedProductWsDto;



/**
 * Setup class for generating test data related to the subscribed product functionality.
 */
public class TmaSubscribedProductTestDataSetup
{
	private static final String TEST_NAME = "TEST_NAME";
	private static final String TEST_PRODUCT_CODE = "TEST_PRODUCT_CODE";
	private static final TmaServiceTypeData TEST_SERVICE_TYPE_DATA = TmaServiceTypeData.TARIFF_PLAN;
	private static final String TEST_BILLING_SYSTEM_ID = "IN";
	private static final String TEST_BILLING_SUBSCRIPTION_ID = "TEST_CREATE_BILLING_SUBSCRIPTION_ID";
	private static final SubscriptionStatusData TEST_SUBSCRIPTION_STATUS = SubscriptionStatusData.ACTIVE;
	private static final String TEST_RENEWAL_TYPE = "NONE";
	private static final boolean TEST_CANCELLABLE_FLAG = false;
	private static final String TEST_BILLING_FREQUENCY = "monthly";
	private static final int TEST_CONTRACT_DURATION = 2;
	private static final String TEST_CONTRACT_FREQUENCY = "year";
	private static final String SUBSCRIPTION_BASE_ID = "testSubscriptionBase_ws";

	public static TmaSubscribedProductWsDto generateTmaSubscribedProductWsDto(TmaSubscribedProductWsDto subscribedProductWsDto)
	{
		subscribedProductWsDto.setBillingSystemId(TEST_BILLING_SYSTEM_ID);
		subscribedProductWsDto.setBillingSubscriptionId(TEST_BILLING_SUBSCRIPTION_ID);
		return (TmaSubscribedProductWsDto) generateUpdatableTmaSubscribedProductWsDto(subscribedProductWsDto);
	}

	public static UpdatableTmaSubscribedProductWsDto generateUpdatableTmaSubscribedProductWsDto(
			UpdatableTmaSubscribedProductWsDto updatableTmaSubscribedProductWsDto)
	{
		updatableTmaSubscribedProductWsDto.setName(TEST_NAME);
		updatableTmaSubscribedProductWsDto.setProductCode(TEST_PRODUCT_CODE);
		updatableTmaSubscribedProductWsDto.setServiceType(TEST_SERVICE_TYPE_DATA);
		updatableTmaSubscribedProductWsDto.setSubscriptionStatus(TEST_SUBSCRIPTION_STATUS);
		updatableTmaSubscribedProductWsDto.setRenewalType(TEST_RENEWAL_TYPE);
		updatableTmaSubscribedProductWsDto.setCancellable(TEST_CANCELLABLE_FLAG);
		updatableTmaSubscribedProductWsDto.setBillingFrequency(TEST_BILLING_FREQUENCY);
		updatableTmaSubscribedProductWsDto.setContractDuration(TEST_CONTRACT_DURATION);
		updatableTmaSubscribedProductWsDto.setContractFrequency(TEST_CONTRACT_FREQUENCY);
		updatableTmaSubscribedProductWsDto.setSubscriptionBaseId(SUBSCRIPTION_BASE_ID);
		return updatableTmaSubscribedProductWsDto;
	}
}
