/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Search query condition builder for {@link PriceRowModel#PRODUCT} based on received {@link TmaPriceContext}.
 *
 * @since 1810
 */
public class TmaSppProductConditionBuilder extends AbstractSubscriptionPricePlanConditionBuilder<TmaPriceContext>
{
	@Override
	protected boolean shouldApplyCondition(final TmaPriceContext priceContext)
	{
		return priceContext != null && priceContext.getProduct() != null;
	}

	@Override
	protected GenericConditionList createQueryConditions(final GenericQuery query, final TmaPriceContext priceContext)
	{
		if (productIsBpo(priceContext.getProduct()))
		{
			return createBpoCondition(priceContext.getProduct());
		}

		return createSpoCondition(priceContext.getProduct());
	}

	protected GenericConditionList createBpoCondition(final ProductModel productOffering)
	{
		final List<GenericCondition> productConditions = new ArrayList<>();
		final GenericSearchField productField = new GenericSearchField(null, PriceRowModel.PRODUCT);
		final GenericCondition productCondition = createProductCondition(productOffering);

		productConditions.add(productCondition);
		appendBpoProductTreeToQuery(productOffering, productConditions, productField);

		return GenericConditionList.or(productConditions);
	}

	protected GenericConditionList createSpoCondition(final ProductModel productOffering)
	{
		final GenericCondition productCondition = createProductCondition(productOffering);
		return GenericConditionList.createConditionList(productCondition);
	}

	protected GenericCondition createProductCondition(final ProductModel productOffering)
	{
		final List<GenericCondition> productConditions = new ArrayList<>();
		final GenericSearchField productField = new GenericSearchField(null, PriceRowModel.PRODUCT);
		final GenericSearchField productIdField = new GenericSearchField(null, PriceRowModel.PRODUCTID);

		final GenericCondition productCondition = GenericCondition
				.createConditionForValueComparison(productField, Operator.EQUAL, productOffering);
		final GenericCondition productIdCondition = GenericCondition
				.createConditionForValueComparison(productIdField, Operator.EQUAL, productOffering.getCode());

		productConditions.add(productCondition);
		productConditions.add(productIdCondition);

		return GenericConditionList.or(productConditions);
	}

	private void appendBpoProductTreeToQuery(final ProductModel startingPo,
			final List<GenericCondition> productConditions, final GenericSearchField productField)
	{
		if (startingPo == null || !productIsBpo(startingPo))
		{
			return;
		}

		for (final TmaProductOfferingModel po : ((TmaBundledProductOfferingModel) startingPo).getChildren())
		{
			if (po instanceof TmaSimpleProductOfferingModel || po instanceof TmaFixedBundledProductOfferingModel)
			{
				// SPOs and Fixed BPOs cannot contain Price Overrides
				continue;
			}
			productConditions.add(GenericCondition
					.createConditionForValueComparison(productField, Operator.EQUAL, po, PriceRowModel.PRODUCT));

			appendBpoProductTreeToQuery(po, productConditions, productField);
		}
	}
}
