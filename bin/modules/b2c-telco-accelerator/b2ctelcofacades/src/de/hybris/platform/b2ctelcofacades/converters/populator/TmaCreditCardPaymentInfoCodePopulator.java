/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;


/**
 * Populates code attribute of {@link CCPaymentInfoData} with details from {@link CreditCardPaymentInfoModel}.
 *
 * @since 1911
 */
public class TmaCreditCardPaymentInfoCodePopulator implements Populator<CreditCardPaymentInfoModel, CCPaymentInfoData>
{

	@Override
	public void populate(final CreditCardPaymentInfoModel source, final CCPaymentInfoData target)
	{
		target.setCode(source.getCode());
	}

}
