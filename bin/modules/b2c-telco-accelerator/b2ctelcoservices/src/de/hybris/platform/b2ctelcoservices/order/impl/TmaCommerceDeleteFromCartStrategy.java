/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceRemoveEntryGroupStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.RemoveEntryGroupParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.order.EntryGroup;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Cart strategy implementation for deleting product offering from cart.
 *
 * @since 1907
 * @deprecated since 2102. Use {@link TmaDeleteFromCartStrategy} instead
 */
@Deprecated(since = "2102")
public class TmaCommerceDeleteFromCartStrategy extends DefaultCommerceRemoveEntryGroupStrategy implements TmaCartStrategy
{
	private static final long DEFAULT_ENTRY_NUMBER = -1;

	@Override
	public List<CommerceCartModification> processCartAction(final List<CommerceCartParameter> commerceCartParameterList)
			throws CommerceCartModificationException
	{
		final List<CommerceCartModification> commerceCartModifications = new ArrayList<>();

		for (final CommerceCartParameter commerceCartParameter : commerceCartParameterList)
		{
			validateEntryGroupNumber(commerceCartParameter);
			validateEntryNumber(commerceCartParameter);
			commerceCartParameter.setQuantity(0);
			if (isBpoProduct(commerceCartParameter))
			{
				final RemoveEntryGroupParameter removeEntryGroupParameter = createRemoveEntryGroupParameter(commerceCartParameter);
				commerceCartModifications.add(removeEntryGroup(removeEntryGroupParameter));
				continue;
			}

			commerceCartModifications.add(getUpdateCartEntryStrategy().updateQuantityForCartEntry(commerceCartParameter));
		}

		return commerceCartModifications;
	}

	/**
	 * Checks whether the {@link CommerceCartParameter} represents an action for a BPO.
	 *
	 * @param commerceCartParameter
	 * 		the information based on which the remove action will be performed
	 * @return <code>true</code> if the product from the commerceCartParameter is a BPO, <code>false</code> otherwise
	 */
	private boolean isBpoProduct(final CommerceCartParameter commerceCartParameter)
	{
		return commerceCartParameter.getEntryNumber() == DEFAULT_ENTRY_NUMBER;
	}

	/**
	 * @param parameter
	 * 		contains attributes used for cart entry updates
	 * @throws CommerceCartModificationException
	 * 		if the cart does not have the entry group number provided in the parameter
	 */
	private void validateEntryNumber(CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		final int entryNumber = (int) parameter.getEntryNumber();
		if (entryNumber == DEFAULT_ENTRY_NUMBER)
		{
			return;
		}

		final List<AbstractOrderEntryModel> cartEntryModelList = parameter.getCart().getEntries();

		if (CollectionUtils.isEmpty(cartEntryModelList))
		{
			return;
		}

		if (cartEntryModelList.size() <= entryNumber)
		{
			throw new CommerceCartModificationException(
					"Entry #" + entryNumber + " was not found in cart " + parameter.getCart().getCode() + ".");
		}

		for (int entryGroupNumber : parameter.getEntryGroupNumbers())
		{
			if (!cartEntryModelList.get(entryNumber).getEntryGroupNumbers().contains(entryGroupNumber))
			{
				throw new CommerceCartModificationException(
						"The entry #" + entryNumber + " is not a part of entry group #" + entryGroupNumber + " in cart " + parameter
								.getCart().getCode() + ".");
			}
		}

	}

	/**
	 * Checks if cart has PO with entry group number provided in parameter.
	 *
	 * @param parameter
	 * 		contains attributes used for cart entry updates
	 * @throws CommerceCartModificationException
	 * 		if the cart does not have the entry group number provided in the parameter
	 */
	protected void validateEntryGroupNumber(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		if (CollectionUtils.isEmpty(parameter.getEntryGroupNumbers()))
		{
			return;
		}

		for (int entryGroupNumber : parameter.getEntryGroupNumbers())
		{
			final List<EntryGroup> entryGroupList = parameter.getCart().getEntryGroups();
			if (CollectionUtils.isEmpty(entryGroupList) || !entryGroupList.stream()
					.filter(o -> o.getGroupNumber().equals(entryGroupNumber)).findFirst().isPresent())
			{
				throw new CommerceCartModificationException(
						"Entry group #" + entryGroupNumber + " was not found in cart " + parameter.getCart().getCode() + ".");
			}
		}
	}

	/**
	 * Created a {@link RemoveEntryGroupParameter} from {@link CommerceCartParameter}
	 *
	 * @param commerceCartParameter
	 * 		the source from which the removeCartEntryGroupParameter will be populated
	 * @return {@link RemoveEntryGroupParameter}
	 */
	private RemoveEntryGroupParameter createRemoveEntryGroupParameter(final CommerceCartParameter commerceCartParameter)
	{
		final RemoveEntryGroupParameter removeEntryGroupParameter = new RemoveEntryGroupParameter();

		removeEntryGroupParameter.setCart(commerceCartParameter.getCart());
		removeEntryGroupParameter.setEnableHooks(commerceCartParameter.isEnableHooks());
		removeEntryGroupParameter.setEntryGroupNumber(commerceCartParameter.getEntryGroupNumbers().iterator().next());

		return removeEntryGroupParameter;
	}
}
