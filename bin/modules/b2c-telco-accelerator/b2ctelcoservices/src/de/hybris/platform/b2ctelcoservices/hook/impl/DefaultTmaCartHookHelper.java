/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.b2ctelcoservices.hook.TmaCartHookHelper;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of {@link TmaCartHookHelper}.
 *
 * @since 1810
 */
public class DefaultTmaCartHookHelper implements TmaCartHookHelper
{
	private ModelService modelService;
	private CommerceCartCalculationStrategy calculationStrategy;
	private TmaAbstractOrderEntryService abstractOrderEntryService;

	public DefaultTmaCartHookHelper(final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public void invalidateBpoEntries(final CartModel cart, final EntryGroup entryGroup)
	{
		if (entryGroup == null)
		{
			return;
		}

		final Set<ItemModel> models = new HashSet<>();
		cart.getEntries().stream().filter(cartEntry -> cartEntry.getEntryGroupNumbers() != null
				&& cartEntry.getEntryGroupNumbers().contains(entryGroup.getGroupNumber())).forEach(cartEntry ->
		{
			cartEntry.setCalculated(Boolean.FALSE);
			cartEntry.getOrder().setCalculated(Boolean.FALSE);
			models.add(cartEntry);
		});
		if (!models.isEmpty())
		{
			final CommerceCartParameter calculationParameter = new CommerceCartParameter();
			calculationParameter.setCart(cart);
			getCalculationStrategy().calculateCart(calculationParameter);
			getModelService().saveAll(models);
			getModelService().save(cart);
		}
	}

	@Override
	public void recalculateEntries(final AbstractOrderEntryModel rootEntry)
	{
		final List<AbstractOrderEntryModel> childEntries = getAbstractOrderEntryService().getAllChildEntries(rootEntry);

		if (CollectionUtils.isEmpty(childEntries))
		{
			return;
		}

		childEntries.add(rootEntry);

		childEntries.forEach((AbstractOrderEntryModel entry) -> entry.setCalculated(Boolean.FALSE));

		final CommerceCartParameter calculationParameter = new CommerceCartParameter();
		calculationParameter.setCart((CartModel) rootEntry.getOrder());
		getCalculationStrategy().calculateCart(calculationParameter);
		getModelService().saveAll(childEntries);
		getModelService().save(rootEntry.getOrder());
	}

	protected void recalculateCart(final CartModel cartModel)
	{
		final CommerceCartParameter calculationParameter = new CommerceCartParameter();
		calculationParameter.setCart(cartModel);
		getCalculationStrategy().calculateCart(calculationParameter);
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	public CommerceCartCalculationStrategy getCalculationStrategy()
	{
		return calculationStrategy;
	}

	public void setCalculationStrategy(CommerceCartCalculationStrategy calculationStrategy)
	{
		this.calculationStrategy = calculationStrategy;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
