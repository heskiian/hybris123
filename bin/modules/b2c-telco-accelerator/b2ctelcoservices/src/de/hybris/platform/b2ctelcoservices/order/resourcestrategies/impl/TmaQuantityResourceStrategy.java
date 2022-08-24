/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.dao.CartEntryDao;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.strategies.ModifiableChecker;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.comparators.ComparableComparator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Resource strategy implementation. Validates and updates quantity.
 *
 * @since 1911
 */
public class TmaQuantityResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private static final long DEFAULT_ENTRY_NUMBER = -1;
	private static final long DEFAULT_FORCE_IN_STOCK_MAX_QUANTITY = 9999;


	private CommerceStockService commerceStockService;
	private CartService cartService;
	private ModelService modelService;
	private BaseStoreService baseStoreService;
	private ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker;
	private CartEntryDao cartEntryDao;
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;
	private TmaPoService poService;

	public TmaQuantityResourceStrategy(CommerceStockService commerceStockService, CartService cartService,
			ModelService modelService, BaseStoreService baseStoreService,
			ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker, CartEntryDao cartEntryDao,
			CommerceCartCalculationStrategy commerceCartCalculationStrategy, TmaPoService poService)
	{
		this.commerceStockService = commerceStockService;
		this.cartService = cartService;
		this.modelService = modelService;
		this.baseStoreService = baseStoreService;
		this.entryOrderChecker = entryOrderChecker;
		this.cartEntryDao = cartEntryDao;
		this.commerceCartCalculationStrategy = commerceCartCalculationStrategy;
		this.poService = poService;
	}

	@Override
	public TmaCartValidationResult validateResource(CommerceCartParameter commerceCartParameter)
	{
		final TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(commerceCartParameter);

		if (commerceCartParameter.getNewQuantity() == null)
		{
			return result;
		}

		if (commerceCartParameter.getNewQuantity() < 0)
		{
			markResultAsInvalid(result, "New quantity must not be less than zero");
			return result;
		}

		if (hasFixedBpoParentEntry(commerceCartParameter))
		{
			markResultAsInvalid(result, "Quantity for fixed bundled product offering component cannot be updated.");
			return result;
		}

		Optional<AbstractOrderEntryModel> entry = Optional.empty();
		boolean entryHasSubscriptionInfo = false;

		if (commerceCartParameter.getEntryNumber() != -1)
		{
			entry = commerceCartParameter.getCart().getEntries().stream()
					.filter(e -> commerceCartParameter.getEntryNumber() == e.getEntryNumber().longValue())
					.findFirst();
			entryHasSubscriptionInfo = entry.isPresent() && entry.get().getSubscriptionInfo() != null;
		}

		final ProductModel product = commerceCartParameter.getProduct();

		boolean quantityIsGreaterThanOne = commerceCartParameter.getNewQuantity() > 1;

		if (quantityIsGreaterThanOne && entry.isPresent() && isFixedBpoComponent(entry.get()))
		{
			markResultAsInvalid(result, "Quantity for fixed bundled product offering component cannot be updated.");
			return result;
		}

		if (quantityIsGreaterThanOne && product instanceof TmaBundledProductOfferingModel)
		{
			markResultAsInvalid(result, "Invalid quantity");
			return result;
		}


		boolean entryHasSubscriptionActionConfigured =
				entryHasSubscriptionInfo && entry.get().getSubscriptionInfo().getSubscribedProductAction() != null;

		boolean subscriptionInfoExistent = commerceCartParameter.getSubscribedProduct() != null;

		if (quantityIsGreaterThanOne && (hasConfigurableProdSpecCharOrValueUse(product) || subscriptionInfoExistent
				|| entryHasSubscriptionActionConfigured))
		{
			markResultAsInvalid(result, "Invalid quantity");
			return result;
		}
		return result;
	}

	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification) throws CommerceCartModificationException
	{
		if (commerceCartParameter.getNewQuantity() == null)
		{
			return;
		}

		if (!isQuantityUpdateNeeded(commerceCartParameter, commerceCartModification))
		{
			return;
		}

		if (commerceCartParameter.getEntryNumber() == DEFAULT_ENTRY_NUMBER)
		{
			return;
		}

		if (isBpo(commerceCartParameter))
		{
			throw new CommerceCartModificationException("Can't update quantity for bundled product offering in cart "
					+ commerceCartParameter.getCart().getCode() + ".");
		}

		updateQuantityForCartEntry(commerceCartParameter, commerceCartModification);
	}

	private boolean isQuantityUpdateNeeded(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
	{
		return commerceCartParameter.getNewQuantity() == 0 ||
				commerceCartModification.getQuantity() != commerceCartParameter.getNewQuantity();
	}

	protected void updateQuantityForCartEntry(final CommerceCartParameter parameters,
			final CommerceCartModification commerceCartModification) throws CommerceCartModificationException
	{
		final CartModel cartModel = parameters.getCart();
		final long newQuantity = parameters.getNewQuantity();
		final long entryNumber = parameters.getEntryNumber();

		validateParameterNotNull(cartModel, "Cart model cannot be null");

		final AbstractOrderEntryModel entryToUpdate = getCartService().getEntryForNumber(cartModel, (int) entryNumber);

		if (entryToUpdate == null)
		{
			throw new CommerceCartModificationException("Unknown entry number");
		}

		if (!isOrderEntryUpdatable(entryToUpdate))
		{
			throw new CommerceCartModificationException("Entry is not updatable");
		}

		final Integer maxOrderQuantity = entryToUpdate.getProduct().getMaxOrderQuantity();
		// Work out how many we want to add (could be negative if we are
		// removing items)
		final long quantityToAdd = newQuantity - entryToUpdate.getQuantity();

		// So now work out what the maximum allowed to be added is (note that
		// this may be negative!)
		final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, entryToUpdate.getProduct(),
				quantityToAdd, entryToUpdate.getDeliveryPointOfService());
		//Now do the actual cartModification
		modifyEntry(cartModel, entryToUpdate, actualAllowedQuantityChange, newQuantity, maxOrderQuantity, commerceCartModification);
	}

	protected boolean isOrderEntryUpdatable(final AbstractOrderEntryModel entryToUpdate)
	{
		return getEntryOrderChecker().canModify(entryToUpdate);
	}

	protected void modifyEntry(final CartModel cartModel, final AbstractOrderEntryModel entryToUpdate,
			final long actualAllowedQuantityChange, final long newQuantity, final Integer maxOrderQuantity,
			final CommerceCartModification commerceCartModification)
	{
		// Now work out how many that leaves us with on this entry
		final long entryNewQuantity = entryToUpdate.getQuantity() + actualAllowedQuantityChange;

		if (entryNewQuantity <= 0)
		{
			removeEntryFromCart(cartModel, entryToUpdate, newQuantity, commerceCartModification);
			return;
		}

		// Adjust the entry quantity to the new value
		entryToUpdate.setQuantity(entryNewQuantity);
		modelService.save(entryToUpdate);
		modelService.refresh(cartModel);
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		getCommerceCartCalculationStrategy().calculateCart(parameter);
		modelService.refresh(entryToUpdate);

		// Return the modification data
		commerceCartModification.setQuantityAdded(actualAllowedQuantityChange);
		commerceCartModification.setEntry(entryToUpdate);
		commerceCartModification.setQuantity(entryNewQuantity);
		commerceCartModification.setParentEntry(entryToUpdate.getMasterEntry());

		if (isMaxOrderQuantitySet(maxOrderQuantity) && entryNewQuantity == maxOrderQuantity.longValue())
		{
			commerceCartModification.setStatusCode(CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED);
		}
		else if (newQuantity == entryNewQuantity)
		{
			commerceCartModification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
		}
		else
		{
			commerceCartModification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
		}
	}

	protected void removeEntryFromCart(CartModel cartModel, AbstractOrderEntryModel entryToUpdate, long newQuantity,
			CommerceCartModification commerceCartModification)
	{
		final CartEntryModel entry = new CartEntryModel()
		{
			@Override
			public Double getBasePrice()
			{
				return null;
			}

			@Override
			public Double getTotalPrice()
			{
				return null;
			}
		};
		entry.setOrder(cartModel);
		entry.setProduct(entryToUpdate.getProduct());

		final CommerceCartModification modification = removeEntry(entryToUpdate, cartModel);
		getModelService().refresh(cartModel);
		normalizeEntryNumbers(cartModel);

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		getCommerceCartCalculationStrategy().calculateCart(parameter);

		// Return an empty modification
		commerceCartModification.setEntry(entry);
		commerceCartModification.setQuantity(0);
		commerceCartModification.setParentEntry(modification.getParentEntry());
		// We removed all the quantity from this row
		commerceCartModification.setQuantityAdded(-entryToUpdate.getQuantity());

		if (newQuantity == 0)
		{
			commerceCartModification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
		}
		else
		{
			commerceCartModification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
		}
	}

	protected CommerceCartModification removeEntry(final AbstractOrderEntryModel entry, final AbstractOrderModel abstractOrder)
	{
		final CommerceCartModification commerceCartModification = new CommerceCartModification();

		if (entry == null)
		{
			return commerceCartModification;
		}

		final AbstractOrderEntryModel parentEntry = entry.getMasterEntry();
		commerceCartModification.setParentEntry(parentEntry);

		getModelService().remove(entry);
		getModelService().refresh(abstractOrder);

		if (parentEntry != null)
		{
			getModelService().refresh(parentEntry);
		}

		if (parentEntry != null && CollectionUtils.isEmpty(parentEntry.getChildEntries()))
		{
			commerceCartModification.setParentEntry(removeEntry(parentEntry, abstractOrder).getParentEntry());
		}

		return commerceCartModification;
	}

	protected long getAllowedCartAdjustmentForProduct(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final PointOfServiceModel pointOfServiceModel)
	{
		final long cartLevel = checkCartLevel(productModel, cartModel, pointOfServiceModel);
		final long stockLevel = getAvailableStockLevel(productModel, pointOfServiceModel);

		// How many will we have in our cart if we add quantity
		final long newTotalQuantity = cartLevel + quantityToAdd;

		// Now limit that to the total available in stock
		final long newTotalQuantityAfterStockLimit = Math.min(newTotalQuantity, stockLevel);

		// So now work out what the maximum allowed to be added is (note that
		// this may be negative!)
		final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();

		if (isMaxOrderQuantitySet(maxOrderQuantity))
		{
			final long newTotalQuantityAfterProductMaxOrder = Math
					.min(newTotalQuantityAfterStockLimit, maxOrderQuantity.longValue());
			return newTotalQuantityAfterProductMaxOrder - cartLevel;
		}
		return newTotalQuantityAfterStockLimit - cartLevel;
	}

	protected long checkCartLevel(final ProductModel productModel, final CartModel cartModel,
			final PointOfServiceModel pointOfServiceModel)
	{
		long cartLevel = 0;
		if (pointOfServiceModel != null)
		{
			for (final CartEntryModel entryModel : getCartEntryDao().findEntriesByProductAndPointOfService(cartModel, productModel,
					pointOfServiceModel))
			{
				cartLevel += entryModel.getQuantity() != null ? entryModel.getQuantity() : 0;
			}
			return cartLevel;

		}

		for (final CartEntryModel entryModel : getCartService().getEntriesForProduct(cartModel, productModel))
		{
			if (entryModel.getDeliveryPointOfService() == null)
			{
				cartLevel += entryModel.getQuantity() != null ? entryModel.getQuantity() : 0;
			}
		}
		return cartLevel;
	}

	protected boolean isMaxOrderQuantitySet(final Integer maxOrderQuantity)
	{
		return maxOrderQuantity != null;
	}

	protected void normalizeEntryNumbers(final CartModel cartModel)
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<>(cartModel.getEntries());
		Collections.sort(entries, new BeanComparator(AbstractOrderEntryModel.ENTRYNUMBER, new ComparableComparator()));
		for (int i = 0; i < entries.size(); i++)
		{
			entries.get(i).setEntryNumber(i);
			getModelService().save(entries.get(i));
		}
	}

	protected long getAvailableStockLevel(final ProductModel productModel, final PointOfServiceModel pointOfServiceModel)
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		if (!getCommerceStockService().isStockSystemEnabled(baseStore))
		{
			return DEFAULT_FORCE_IN_STOCK_MAX_QUANTITY;
		}

		final Long availableStockLevel;

		if (pointOfServiceModel == null)
		{
			availableStockLevel = getCommerceStockService().getStockLevelForProductAndBaseStore(productModel, baseStore);
		}
		else
		{
			availableStockLevel = getCommerceStockService().getStockLevelForProductAndPointOfService(productModel,
					pointOfServiceModel);
		}

		if (availableStockLevel == null)
		{
			return DEFAULT_FORCE_IN_STOCK_MAX_QUANTITY;
		}

		return availableStockLevel;
	}

	private boolean isBpo(final CommerceCartParameter commerceCartParameter) throws CommerceCartModificationException
	{
		final AbstractOrderEntryModel entryModel = commerceCartParameter.getCart().getEntries().stream()
				.filter((AbstractOrderEntryModel entry) -> entry.getEntryNumber() == commerceCartParameter.getEntryNumber())
				.findFirst().orElseThrow(() -> new CommerceCartModificationException(
						"Entry with #" + commerceCartParameter.getEntryNumber() + " does not exist in cart '" + commerceCartParameter
								.getCart().getCode() + "'."));

		return entryModel.getProduct() instanceof TmaBundledProductOfferingModel;
	}

	private boolean hasConfigurableProdSpecCharOrValueUse(final ProductModel product)
	{
		boolean productHasConfigurablePsc =
				product instanceof TmaProductOfferingModel && CollectionUtils
						.isNotEmpty(getPoService().getConfigurableProductSpecCharacteristics((TmaProductOfferingModel) product));

		boolean productHasConfigurablePscvu =
				product instanceof TmaProductOfferingModel && CollectionUtils
						.isNotEmpty(getPoService().getConfigurableProductSpecCharValueUses((TmaProductOfferingModel) product));
		return productHasConfigurablePsc || productHasConfigurablePscvu;
	}

	private boolean isFixedBpoComponent(final AbstractOrderEntryModel cartEntry)
	{
		return cartEntry.getMasterEntry() != null && cartEntry.getMasterEntry()
				.getProduct() instanceof TmaFixedBundledProductOfferingModel;
	}

	private boolean hasFixedBpoParentEntry(final CommerceCartParameter commerceCartParameter)
	{
		final AbstractOrderEntryModel parentEntry = commerceCartParameter.getParentEntry();
		return parentEntry != null && parentEntry.getProduct() instanceof TmaFixedBundledProductOfferingModel;
	}

	private void markResultAsInvalid(final TmaCartValidationResult result, final String message)
	{
		result.setValid(false);
		result.setMessage(message);
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	protected ModifiableChecker<AbstractOrderEntryModel> getEntryOrderChecker()
	{
		return entryOrderChecker;
	}

	protected CartEntryDao getCartEntryDao()
	{
		return cartEntryDao;
	}

	protected CommerceCartCalculationStrategy getCommerceCartCalculationStrategy()
	{
		return commerceCartCalculationStrategy;
	}

	protected TmaPoService getPoService()
	{
		return poService;
	}

}
