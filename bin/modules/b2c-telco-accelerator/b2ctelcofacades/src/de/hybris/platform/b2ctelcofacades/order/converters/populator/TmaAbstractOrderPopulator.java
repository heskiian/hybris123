/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populator for populating cart and order data.
 *
 * @param <SOURCE>
 * 		subclass of AbstractOrderModel
 * @param <TARGET>
 * 		subclass of AbstractOrderData
 * @since 2102
 */
public class TmaAbstractOrderPopulator<SOURCE extends AbstractOrderModel,
		TARGET extends AbstractOrderData> extends AbstractOrderPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE abstractOrderModel, final TARGET abstractOrderData)
	{
		validateParameterNotNullStandardMessage("source", abstractOrderModel);
		validateParameterNotNullStandardMessage("target", abstractOrderData);

		addPrincipalInformation(abstractOrderModel, abstractOrderData);
		addPaymentInformation(abstractOrderModel, abstractOrderData);
		addDeliveryAddress(abstractOrderModel, abstractOrderData);
		addDeliveryMethod(abstractOrderModel, abstractOrderData);

		if (abstractOrderModel.getPaymentAddress() != null)
		{
			abstractOrderData.setBillingAddress(getAddressConverter().convert(abstractOrderModel.getPaymentAddress()));
		}

		abstractOrderData.setTotalUnitCount(calcTotalUnitCount(abstractOrderModel));
	}

	@Override
	protected Integer calcTotalUnitCount(final AbstractOrderModel source)
	{
		int totalUnitCount = 0;
		final List<AbstractOrderEntryModel> spoEntries = source.getEntries().stream()
				.filter((AbstractOrderEntryModel entry) -> CollectionUtils.isEmpty(entry.getChildEntries()) &&
						!(entry.getProduct() instanceof TmaBundledProductOfferingModel))
				.collect(Collectors.toList());

		for (final AbstractOrderEntryModel orderEntryModel : spoEntries)
		{
			totalUnitCount += orderEntryModel.getQuantity().intValue();
		}

		return totalUnitCount;
	}
}
