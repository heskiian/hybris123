/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.*;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Search query condition builder for {@link SubscriptionPricePlanModel#CURRENCY} based on received {@link TmaPriceContext}.
 *
 * @since 1810
 */
public class TmaSppCurrencyConditionBuilder extends AbstractSubscriptionPricePlanConditionBuilder<TmaPriceContext>
{
	private CommonI18NService i18NService;

	@Override
	protected boolean shouldApplyCondition(final TmaPriceContext priceContext)
	{
		return priceContext != null;
	}

	@Override
	protected GenericConditionList createQueryConditions(final GenericQuery query, final TmaPriceContext priceContext)
	{
		final GenericSearchField currencyField = new GenericSearchField(null,
				PriceRowModel.CURRENCY);
		final GenericCondition currencyCondition = GenericCondition
				.createConditionForValueComparison(currencyField, Operator.EQUAL,
						priceContext.getCurrency() != null ? priceContext.getCurrency() : getI18NService().getCurrentCurrency(),
						CurrencyModel._TYPECODE);

		return GenericConditionList.createConditionList(currencyCondition);
	}

	protected CommonI18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final CommonI18NService i18NService)
	{
		this.i18NService = i18NService;
	}

}
