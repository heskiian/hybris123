/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.impl;

import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaCartValidationBuilder;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaChecklistPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Strategy to resolve an {@link TmaPolicyActionModel} against a list of {@link TmaPolicyContext}
 *
 * @since 1911
 */
public abstract class DefaultTmaAbstractChecklistActionResolver implements TmaChecklistActionResolver
{
	private ModelService modelService;
	private TmaCartValidationBuilder cartValidationBuilder;

	public DefaultTmaAbstractChecklistActionResolver(ModelService modelService,
			TmaCartValidationBuilder cartValidationBuilder)
	{
		this.modelService = modelService;
		this.cartValidationBuilder = cartValidationBuilder;
	}

	@Override
	public void resolveAction(final TmaPolicyActionModel action, final List<TmaPolicyContext> contexts)
	{
		contexts.stream()
				.map(context -> (CartEntryModel) context.getOrderEntry())
				.forEach(cartEntry -> {
					if (!isActionFulfilled(action, cartEntry))
					{
						final Set<TmaCartValidationModel> entryValidations = new HashSet<>(cartEntry.getCartEntryValidations());
						String code = String.valueOf(((TmaChecklistPolicyStatementModel) action.getStatement()).getType());
						final Set<TmaCartValidationModel> cartEntryValidations = getCartValidationBuilder()
								.createCartValidations(code, Collections.singletonList(getInvalidMessage()));
						entryValidations.addAll(cartEntryValidations);
						cartEntry.setCartEntryValidations(entryValidations);
						getModelService().save(cartEntry);
					}
				});
	}

	/**
	 * Verifies if the action for the given cart entry is fulfilled or not
	 *
	 * @param action
	 * 		the action to be verified
	 * @param cartEntry
	 * 		the given cart entry
	 * @return true if the action for the given car entry is fulfilled, otherwise false
	 */
	protected abstract boolean isActionFulfilled(final TmaPolicyActionModel action, final CartEntryModel cartEntry);

	/**
	 * Retrieves the invalid message to be set on the validation object
	 *
	 * @return the invalid message
	 */
	protected abstract String getInvalidMessage();

	protected ModelService getModelService()
	{
		return modelService;
	}

	public TmaCartValidationBuilder getCartValidationBuilder()
	{
		return cartValidationBuilder;
	}
}
