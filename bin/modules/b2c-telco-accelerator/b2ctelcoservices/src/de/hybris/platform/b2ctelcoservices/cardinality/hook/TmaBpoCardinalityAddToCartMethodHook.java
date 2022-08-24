/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.cardinality.hook;

import de.hybris.platform.b2ctelcoservices.cardinality.TmaBpoCardinalityService;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.order.TmaEntryGroupService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;


/**
 * Hook for cardinality specific validation when adding item to cart
 *
 * @since 2011
 */
public class TmaBpoCardinalityAddToCartMethodHook implements CommerceAddToCartMethodHook
{
	private TmaBpoCardinalityService bpoCardinalityService;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private TmaEntryGroupService entryGroupService;

	private TmaAbstractOrderEntryService abstractOrderEntryService;

	private TmaPoService tmaPoService;

	public TmaBpoCardinalityAddToCartMethodHook(final TmaBpoCardinalityService bpoCardinalityService,
			final TmaEntryGroupService entryGroupService, final TmaAbstractOrderEntryService abstractOrderEntryService,
			final TmaPoService tmaPoService)
	{
		this.bpoCardinalityService = bpoCardinalityService;
		this.entryGroupService = entryGroupService;
		this.abstractOrderEntryService = abstractOrderEntryService;
		this.tmaPoService = tmaPoService;
	}

	@Override
	public void beforeAddToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		verifyCardinality(parameter);
	}

	@Override
	public void afterAddToCart(final CommerceCartParameter parameter, final CommerceCartModification result)
	{
		if (result.getQuantityAdded() <= 0 || result.getEntry() == null)
		{
			return;
		}

		final AbstractOrderEntryModel rootEntry = getAbstractOrderEntryService().getRootEntry(result.getEntry());

		if (rootEntry == null || !(rootEntry.getProduct() instanceof TmaBundledProductOfferingModel))
		{
			return;
		}

		final Map<AbstractOrderEntryModel, List<String>> validationMessagesMap = getBpoCardinalityService()
				.verifyCardinality(rootEntry);

		validationMessagesMap.forEach((AbstractOrderEntryModel key, List<String> value) ->
				getBpoCardinalityService().updateCardinalityValidationMessages(key, value));
	}

	/**
	 * Verifies if cardinality is fulfilled for {@link TmaFixedBundledProductOfferingModel} part of the request.
	 *
	 * @param parameter
	 * 		the request to be added to cart
	 * @throws CommerceCartModificationException
	 * 		in case of cardinality is not fulfilled
	 */
	protected void verifyCardinality(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		if (parameter.getProduct() instanceof TmaFixedBundledProductOfferingModel)
		{
			final TmaFixedBundledProductOfferingModel fixedBpo = (TmaFixedBundledProductOfferingModel) parameter.getProduct();
			final Map<TmaProductOfferingModel, Integer> childrenMap = retrieveChildrenWithQuantityMapping(parameter);

			if (!(childrenMap.keySet().size() == fixedBpo.getChildren().size() &&
					childrenMap.keySet().containsAll(fixedBpo.getChildren())))
			{
				throw new CommerceCartModificationException(
						"Not all children of product offering with code: " + fixedBpo.getCode() + " are part of the request.");
			}

			if (!getBpoCardinalityService().verifyCardinality(fixedBpo, childrenMap))
			{
				throw new CommerceCartModificationException(
						"Cardinality is not fulfilled for children of product offering with code: " + fixedBpo.getCode());
			}
		}
		if (!CollectionUtils.isEmpty(parameter.getChildren()))
		{
			for (CommerceCartParameter childParam : parameter.getChildren())
			{
				verifyCardinality(childParam);
			}
		}
	}

	/**
	 * Retrieves the product offerings found on the children of the cart parameter and their quantity.
	 *
	 * @param parameter
	 * 		the cart parameter.
	 * @return a map of product offerings and quantities.
	 */
	private Map<TmaProductOfferingModel, Integer> retrieveChildrenWithQuantityMapping(final CommerceCartParameter parameter)
	{
		final Map<TmaProductOfferingModel, Integer> childrenOfferings = new HashMap<>();
		if (CollectionUtils.isEmpty(parameter.getChildren()))
		{
			return childrenOfferings;
		}

		parameter.getChildren().stream().forEach(child -> {
			if (childrenOfferings.containsKey(child.getProduct()))
			{
				childrenOfferings.put((TmaProductOfferingModel) child.getProduct(),
						(int) (childrenOfferings.get(child.getProduct()) + child.getQuantity()));
			}
			else
			{
				childrenOfferings.put((TmaProductOfferingModel) child.getProduct(), (int) child.getQuantity());
			}
		});

		return childrenOfferings;
	}

	protected TmaBpoCardinalityService getBpoCardinalityService()
	{
		return bpoCardinalityService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected TmaEntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}
}
