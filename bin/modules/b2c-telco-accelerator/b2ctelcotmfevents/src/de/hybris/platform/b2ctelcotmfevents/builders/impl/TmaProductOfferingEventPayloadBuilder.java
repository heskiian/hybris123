/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.builders.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcotmfevents.builders.TmaAbstractEventPayloadBuilder;
import de.hybris.platform.b2ctelcotmfevents.data.TmaAbstractEventPayload;
import de.hybris.platform.b2ctelcotmfevents.data.TmaProductOfferingEventPayload;
import de.hybris.platform.b2ctelcotmfevents.enums.TmaEventType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.time.TimeService;


/**
 * Builder responsible for creating {@link TmaProductOfferingEventPayload}s for tmf events.
 *
 * @since 2105
 */
public class TmaProductOfferingEventPayloadBuilder extends TmaAbstractEventPayloadBuilder
{

	public TmaProductOfferingEventPayloadBuilder(final TimeService timeService)
	{
		super(timeService);
	}

	@Override
	public TmaAbstractEventPayload build(final ItemModel item, final TmaEventType eventType)
	{
		final TmaProductOfferingEventPayload offeringEventPayload = new TmaProductOfferingEventPayload();
		offeringEventPayload.setOffering((TmaProductOfferingModel) item);
		setCommonFields(offeringEventPayload, eventType);
		return offeringEventPayload;
	}
}
