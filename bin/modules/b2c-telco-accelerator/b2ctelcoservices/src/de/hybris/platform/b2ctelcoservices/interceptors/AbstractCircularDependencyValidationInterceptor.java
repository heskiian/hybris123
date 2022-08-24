/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;


import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Validation Interceptor making sure that no circular dependency can be formed within a give composite Item.
 *
 * @param <P>
 *           stands for PARENT_ITEM
 * @param <C>
 *           stands for COMPOSITE_ITEM
 * @since 6.7
 */
public abstract class AbstractCircularDependencyValidationInterceptor<P extends ItemModel, C extends P>
		implements ValidateInterceptor<C>
{

	/**
	 * Extracts the children of the item given
	 *
	 * @param modifiedItem
	 *           item that has been modified
	 * @return {@link Set<P>}
	 */
	public abstract Set<P> getChildren(final C modifiedItem);

	/**
	 * Extracts item's parent items
	 *
	 * @param existingItem
	 *           existing item to be validated
	 * @return {@link Set<C>}
	 */
	public abstract Set<C> getParents(final C existingItem);

	/**
	 * Filters composite items from the set of composite and simple items
	 *
	 * @param entries
	 *           set of children items to be filtered
	 * @return {@link Set<C>}
	 */
	public abstract Set<C> filterCompositeItem(final Set<P> entries);

	/**
	 * Validates the modified composite attributes of the {@param modifiedItem} given, by searching for circular
	 * dependencies within the current configuration.
	 *
	 * @param modifiedItem
	 *           item that have been modified, to be validated
	 * @throws InterceptorException
	 *            if any circular dependency can be found within the given structure
	 */
	protected void validateModifiedItem(final C modifiedItem) throws InterceptorException
	{
		final Set<C> children = filterCompositeItem(getChildren(modifiedItem));

		for (final C existingChildItem : children)
		{
			validateModifiedItemAgainstExistingChildren(modifiedItem, existingChildItem);

			for (final C existingParentItem : getParents(existingChildItem))
			{
				final boolean isInitialParentChildRelationship = existingParentItem.equals(modifiedItem);
				if (isInitialParentChildRelationship)
				{
					continue;
				}
				validateModifiedItemAgainstExistingParentItem(modifiedItem, existingParentItem);

			}
		}
	}

	/**
	 * Validates the parent item of the {@param modifiedItem} given, by searching for circular dependencies within the
	 * current configuration.
	 *
	 * @param modifiedItem
	 *           item for which the parent item have been modified, to be validated
	 * @throws InterceptorException
	 *            if any circular dependency can be found within the given structure
	 */
	protected void validateModifiedParentItem(final C modifiedItem) throws InterceptorException
	{
		for (final C parentItemToBeChecked : CollectionUtils.emptyIfNull(getParents(modifiedItem)))
		{
			validateModifiedItemAgainstExistingParentItem(modifiedItem, parentItemToBeChecked);

			final Set<C> childItemsForParentToBeChecked = filterCompositeItem(getChildren(parentItemToBeChecked));
			for (final C childItemToBeChecked : childItemsForParentToBeChecked)
			{
				final boolean isInitialParentChildRelationship = childItemToBeChecked.equals(modifiedItem);
				if (isInitialParentChildRelationship)
				{
					continue;
				}
				validateModifiedItemAgainstExistingChildren(modifiedItem, childItemToBeChecked);
			}
		}
	}

	/**
	 * Validates the {@param modifiedItem} against the already configured child item of the {@param existingItem}. If any
	 * of the existing children equals the {@param modifiedItem}, a cyclic dependency has been detected.
	 *
	 * @param modifiedItem
	 *           modified item to be validated against the existing configuration
	 * @param existingItem
	 *           already configured item against which the {@param modifiedItem} is validated
	 * @throws InterceptorException
	 *            if a cyclic dependency has been found, meaning that any of the child item of the {@param existingItem}
	 *            equals the {@param modifiedItem}
	 */
	private void validateModifiedItemAgainstExistingChildren(final C modifiedItem,
			final C existingItem) throws InterceptorException
	{
		checkItemsAreNotEqual(modifiedItem, existingItem);
		final Set<C> existingChildItems = filterCompositeItem(getChildren(existingItem));
		for (final C childItem : existingChildItems)
		{
			validateModifiedItemAgainstExistingChildren(modifiedItem, childItem);
		}
	}

	/**
	 * Validates the {@param modifiedItem} against the already configured parent item of the {@param existingItem}. If
	 * any of the existing parent equals the {@param modifiedItem}, a cyclic dependency has been detected.
	 *
	 * @param modifiedItem
	 *           modified modifiedItem to be validated against the existing configuration
	 * @param existingItem
	 *           already configured modifiedItem against which the {@param modifiedItem} is validated
	 * @throws InterceptorException
	 *            if a cyclic dependency has been found, meaning that any of the parent item of the {@param modifiedItem}
	 *            equals the {@param existingItem}
	 */
	private void validateModifiedItemAgainstExistingParentItem(final C modifiedItem,
			final C existingItem) throws InterceptorException
	{
		checkItemsAreNotEqual(modifiedItem, existingItem);
		for (final C parent : getParents(existingItem))
		{
			validateModifiedItemAgainstExistingParentItem(modifiedItem, parent);
		}
	}

	private void checkItemsAreNotEqual(final C item1, final C item2)
			throws InterceptorException
	{
		if (item2.equals(item1))
		{
			throwInterceptorExceptionForMessage("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate " + C._TYPECODE + " reference");
		}
	}

	private void throwInterceptorExceptionForMessage(final String message) throws InterceptorException
	{
		final InterceptorException interceptorException = new InterceptorException(message);
		interceptorException.setInterceptor(this);
		throw interceptorException;
	}
}
