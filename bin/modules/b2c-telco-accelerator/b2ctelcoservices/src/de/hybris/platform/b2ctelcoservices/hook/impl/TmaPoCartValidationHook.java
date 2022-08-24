/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.b2ctelcoservices.order.TmaCartService;
import de.hybris.platform.b2ctelcoservices.order.impl.TmaCommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.hooks.CartValidationHook;
import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Cart validation hook used to disable checkout if there is any invalid entry group.
 *
 * @since 6.7
 */
public class TmaPoCartValidationHook implements CartValidationHook
{
	private EntryGroupService entryGroupService;
	private TmaCartService tmaCartService;

	@Override
	public void beforeValidateCart(final CommerceCartParameter parameter, final List<CommerceCartModification> modifications)
	{
		//do nothing
	}

	@Override
	public void afterValidateCart(final CommerceCartParameter parameter, final List<CommerceCartModification> modifications)
	{
		if (modifications == null || parameter == null)
		{
			return;
		}
		if (parameter.getCart() == null)
		{
			return;
		}

		final boolean cartHasInvalidProcess = tmaCartService.validateCartForEligibilityRules(parameter.getCart());
		if (cartHasInvalidProcess)
		{
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode(TmaCommerceCartModificationStatus.ELIGIBILTY_ERROR.getCode());
			modifications.add(modification);
		}

		if (CollectionUtils.isNotEmpty(parameter.getCart().getCartValidations()))
		{
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode(TmaCommerceCartModificationStatus.COMPATIBILITY_ERROR.name());
			modifications.add(modification);
		}
		if (parameter.getCart().getEntryGroups() == null)
		{
			return;
		}

		final Set<Integer> invalidGroups = parameter.getCart().getEntryGroups().stream()
				.filter(root -> GroupType.B2CTELCO_BPO.equals(root.getGroupType()))
				.filter(group -> group.getErroneous() != null && group.getErroneous()).map(EntryGroup::getGroupNumber)
				.collect(Collectors.toSet());

		if (invalidGroups.isEmpty())
		{
			return;
		}
		final CommerceCartModification modification = new CommerceCartModification();
		modification.setStatusCode(CommerceCartModificationStatus.ENTRY_GROUP_ERROR);
		modification.setEntryGroupNumbers(invalidGroups);
		modifications.add(modification);

	}

	protected EntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	public void setEntryGroupService(final EntryGroupService entryGroupService)
	{
		this.entryGroupService = entryGroupService;
	}

	public TmaCartService getTmaCartService()
	{
		return tmaCartService;
	}

	@Required
	public void setTmaCartService(final TmaCartService tmaCartService)
	{
		this.tmaCartService = tmaCartService;
	}

}
