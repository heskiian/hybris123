/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility;

import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.order.EntryGroup;

import java.util.List;


/**
 * Service used to handle an invalid {@link TmaPolicyActionModel}.
 */
public interface TmaPolicyActionResolver
{
	/**
	 * Handles a compatibility policy action based on its type.
	 *
	 * @param orderModel
	 * 		the current order
	 * @param entryGroup
	 * 		cart entry group considered invalid
	 * @param policyActions
	 * 		failed policy actions
	 * @deprecated since 2102 - use {@link #processPolicyActions(AbstractOrderModel, CartEntryModel, List)} instead
	 */
	@Deprecated(since = "2102")
	void processPolicyActions(final AbstractOrderModel orderModel, final EntryGroup entryGroup,
			final List<TmaPolicyActionModel> policyActions);

	/**
	 * Handles a compatibility policy action based on its type.
	 *
	 * @param orderModel
	 * 		the current order
	 * @param parentEntryModel
	 * 		the parent entry representing the invalid group
	 * @param policyActions
	 * 		failed policy actions
	 */
	void processPolicyActions(final AbstractOrderModel orderModel, final CartEntryModel parentEntryModel,
			final List<TmaPolicyActionModel> policyActions);
}
