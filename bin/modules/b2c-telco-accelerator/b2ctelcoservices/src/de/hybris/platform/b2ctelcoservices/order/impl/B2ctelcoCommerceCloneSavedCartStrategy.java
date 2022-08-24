/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartStrategy;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCloneSavedCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.CollectionUtils;


/**
 * B2ctelcoCommerceCloneSavedCartStrategy, an extension of {@link DefaultCommerceCloneSavedCartStrategy} to create clone
 * of cart provided by user.
 *
 * @since 1911
 */
public class B2ctelcoCommerceCloneSavedCartStrategy extends DefaultCommerceCloneSavedCartStrategy implements TmaCartStrategy
{

	private final CommerceSaveCartStrategy commerceSaveCartStrategy;
	private final CommerceCartService commerceCartService;
	private final KeyGenerator keyGenerator;

	public B2ctelcoCommerceCloneSavedCartStrategy(final CommerceSaveCartStrategy commerceSaveCartStrategy,
			final CommerceCartService commerceCartService, final KeyGenerator keyGenerator)
	{
		this.commerceSaveCartStrategy = commerceSaveCartStrategy;
		this.commerceCartService = commerceCartService;
		this.keyGenerator = keyGenerator;
	}

	/**
	 * Clones the the given cart with given name and description
	 *
	 * @param commerceCartParameterList
	 * 		A list of parameter objects containing all the attributes needed for clone saved cart
	 * @return List of {@link CommerceCartModification}
	 * @throws CommerceCartModificationException
	 * 		the commerce cart modification exception
	 */
	@Override
	public List<CommerceCartModification> processCartAction(final List<CommerceCartParameter> commerceCartParameterList)
			throws CommerceCartModificationException
	{
		final List<CommerceCartModification> commerceCartModificationList = new ArrayList<>();
		final CommerceCartParameter parameter = commerceCartParameterList.get(0);
		final CommerceSaveCartParameter commerceSaveCartParameter = new CommerceSaveCartParameter();
		commerceSaveCartParameter.setName(parameter.getCloneCartName());
		commerceSaveCartParameter.setDescription(parameter.getCloneCartDescription());
		commerceSaveCartParameter
				.setCart(getCommerceCartService().getCartForCodeAndUser(parameter.getCloneCartID(), parameter.getUser()));
		commerceSaveCartParameter.setEnableHooks(parameter.isEnableHooks());

		try
		{
			final CartModel clonedCart = cloneSavedCart(commerceSaveCartParameter).getSavedCart();
			commerceSaveCartParameter.setCart(clonedCart);
			final CommerceSaveCartResult result = getCommerceSaveCartStrategy().saveCart(commerceSaveCartParameter);

			final CommerceCartModification commerceCartModification = new CommerceCartModification();
			commerceCartModification.setClonedCartId(result.getSavedCart().getCode());
			commerceCartModificationList.add(commerceCartModification);
		}
		catch (final CommerceSaveCartException ex)
		{
			throw new CommerceCartModificationException("Could not clone cart " + ex.getMessage(), ex);
		}

		return commerceCartModificationList;
	}

	@Override
	public CommerceSaveCartResult cloneSavedCart(final CommerceSaveCartParameter parameter) throws CommerceSaveCartException
	{
		final CommerceSaveCartResult cloneCartResult = new CommerceSaveCartResult();

		this.beforeCloneSaveCart(parameter);

		final CartModel clonedCart = getCartService().clone(null, null, parameter.getCart(), null);
		clonedCart.setPaymentTransactions(null);
		clonedCart.setCode(null);
		cloneCartResult.setSavedCart(clonedCart);

		processValidationMessages(clonedCart.getEntries());

		getModelService().save(clonedCart);

		this.afterCloneSaveCart(parameter, cloneCartResult);

		return cloneCartResult;
	}

	/**
	 * Processes validation messages, sets a new id on each of them so they won't clash with the original ones.
	 *
	 * @param entries cart entries
	 */
	protected void processValidationMessages(final Collection<AbstractOrderEntryModel> entries)
	{
		if (CollectionUtils.isEmpty(entries))
		{
			return;
		}

		entries.forEach(entry -> {
			final Set<TmaCartValidationModel> cartEntryValidations = ((CartEntryModel) entry).getCartEntryValidations();

			if (CollectionUtils.isEmpty(cartEntryValidations))
			{
				return;
			}

			final Set<TmaCartValidationModel> preparedValidations = prepareValidations(cartEntryValidations);
			((CartEntryModel) entry).setCartEntryValidations(preparedValidations);
			if (entry.getChildEntries() != null)
			{
				processValidationMessages(entry.getChildEntries());
			}
		});
	}

	/**
	 * Generates and sets a new id on each of the validations received.
	 *
	 * @param cartEntryValidations list of validations
	 */
	protected Set<TmaCartValidationModel> prepareValidations(final Set<TmaCartValidationModel> cartEntryValidations)
	{
		if (CollectionUtils.isEmpty(cartEntryValidations))
		{
			return new HashSet<>();
		}

		final Set<TmaCartValidationModel> preparedValidations = new HashSet<>();

		cartEntryValidations.forEach(validation -> {
			validation.setId((String) getKeyGenerator().generate());
			preparedValidations.add(validation);
		});

		return preparedValidations;
	}

	protected CommerceSaveCartStrategy getCommerceSaveCartStrategy()
	{
		return commerceSaveCartStrategy;
	}

	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

}
