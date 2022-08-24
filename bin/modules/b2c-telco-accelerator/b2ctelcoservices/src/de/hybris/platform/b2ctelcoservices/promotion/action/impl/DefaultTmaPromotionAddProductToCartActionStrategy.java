/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.promotion.action.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCalculationService;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.TmaAbstractOrderPriceBuilder;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotionengineservices.action.impl.DefaultAddProductToCartActionStrategy;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderAddProductActionModel;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.FreeProductRAO;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Strategy to add the free product to cart by setting TUA specific attributes.
 *
 * @since 2003
 */
public class DefaultTmaPromotionAddProductToCartActionStrategy extends DefaultAddProductToCartActionStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTmaPromotionAddProductToCartActionStrategy.class);
	private TmaAbstractOrderPriceBuilder orderPriceBuilder;

	public DefaultTmaPromotionAddProductToCartActionStrategy(
			TmaAbstractOrderPriceBuilder orderPriceBuilder)
	{
		this.orderPriceBuilder = orderPriceBuilder;
	}

	@Override
	public List<PromotionResultModel> apply(AbstractRuleActionRAO action)
	{
		if (!(action instanceof FreeProductRAO))
		{
			return Collections.emptyList();
		}

		FreeProductRAO freeAction = (FreeProductRAO) action;

		if (!(freeAction.getAppliedToObject() instanceof CartRAO))
		{
			return Collections.emptyList();
		}

		if (freeAction.getAddedOrderEntry() == null 
				|| freeAction.getAddedOrderEntry().getProductCode() == null)
		{
			return Collections.emptyList();
		}

		ProductModel productModel;

		try
		{
			productModel = getProductService().getProductForCode(freeAction.getAddedOrderEntry().getProductCode());
		}
		catch (AmbiguousIdentifierException | UnknownIdentifierException var11)
		{
			LOG.error("cannot apply {}, product for code: {} cannot be retrieved due to exception {}.",
					this.getClass().getSimpleName(),
					freeAction.getAddedOrderEntry().getProductCode(), var11.getClass().getSimpleName(), var11);
			return Collections.emptyList();
		}

		if (productModel instanceof TmaProductOfferingModel)
		{
			TmaProductOfferingModel tmaProductModel = (TmaProductOfferingModel) productModel;

			if (tmaProductModel.getProductSpecification() != null
					&& tmaProductModel.getProductSpecification().getProductSpecificationTypes() != null)
			{
				LOG.error("cannot apply {}, product {} has a product specification", this.getClass().getSimpleName(),
						tmaProductModel.getCode());
				return Collections.emptyList();
			}
		}

		List<PromotionResultModel> appliedPromotions = super.apply(action);

		if (CollectionUtils.isEmpty(appliedPromotions))
		{
			return Collections.emptyList();
		}

		appliedPromotions.forEach(this::applyPromotion);

		return appliedPromotions;
	}

	protected void applyPromotion(PromotionResultModel appliedPromotion)
	{
		AbstractOrderModel orderModel = appliedPromotion.getOrder();

		final Set<ProductModel> productModels = new HashSet<>();

		appliedPromotion.getActions().forEach((AbstractPromotionActionModel productAction) -> productModels
				.add(((RuleBasedOrderAddProductActionModel) productAction).getProduct()));

		final List<AbstractOrderEntryModel> freeEntries = new ArrayList<>();
		productModels.forEach(
				productModel -> freeEntries.addAll(orderModel.getEntries().stream().filter(
						entryModel -> entryModel.getProduct().getCode().equals(productModel.getCode())
								&& entryModel.getProcessType() == null && entryModel.getSubscriptionInfo() == null
								&& entryModel.getBpo() == null).collect(Collectors.toList())));

		freeEntries.forEach(entry -> updateEntry(entry, orderModel));
	}

	protected void updateEntry(AbstractOrderEntryModel entryToUpdate, AbstractOrderModel order)
	{
		entryToUpdate.setProcessType(TmaProcessType.DEVICE_ONLY);

		final TmaAbstractOrderCompositePriceModel orderPrice = getModelService().create(TmaAbstractOrderCompositePriceModel.class);
		final Set<TmaAbstractOrderPriceModel> orderPriceSet = new HashSet<>();
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePriceModel = (TmaAbstractOrderOneTimeChargePriceModel) getOrderPriceBuilder()
				.buildPrice(0.0, order, null, TmaAbstractOrderPriceType.PRODUCT_PRICE);
		orderPriceSet.add(oneTimeChargePriceModel);
		orderPrice.setChildPrices(orderPriceSet);
		getModelService().save(orderPrice);

		entryToUpdate.setPrice(orderPrice);
		getModelService().save(entryToUpdate);
	}

	protected TmaAbstractOrderPriceBuilder getOrderPriceBuilder()
	{
		return orderPriceBuilder;
	}

	@Override
	public TmaCalculationService getCalculationService()
	{
		return (TmaCalculationService) super.getCalculationService();
	}
}
