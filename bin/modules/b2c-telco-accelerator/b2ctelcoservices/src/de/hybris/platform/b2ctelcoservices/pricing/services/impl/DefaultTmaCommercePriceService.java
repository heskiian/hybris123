/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionPricePlanDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.pricing.comparator.PriceRowModelComparator;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPriceContextService;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPriceSelector;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;
import de.hybris.platform.subscriptionservices.price.impl.DefaultSubscriptionCommercePriceService;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;


/**
 * Default implementation of the {@link TmaCommercePriceService}.
 *
 * @since 6.7
 */
public class DefaultTmaCommercePriceService extends DefaultSubscriptionCommercePriceService implements TmaCommercePriceService
{
	private static final String ORDER_ENTRY_CANNOT_BE_NULL = "orderEntry cannot be null";
	private TmaSubscriptionPricePlanDao subscriptionPricePlanDao;
	private PriceRowModelComparator priceRowComparator;
	private TmaPriceContextService tmaPriceContextService;
	private TmaPriceSelector tmaPriceSelector;
	/**
	 * @deprecated since 2007. Use tmaPriceSelector instead.
	 */
	@Deprecated(since = "2007")
	private DefaultTmaMinimumPriceSelector minimumPriceSelector;

	private TmaAbstractOrderEntryService abstractOrderEntryService;

	public DefaultTmaCommercePriceService(final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public PriceRowModel getMinimumPrice(final TmaPriceContext priceContext)
	{
		if (shouldConsiderPriceOverrides(priceContext.getProduct()))
		{
			return getMinimumPriceSelector().selectPrice(findApplicableStandalonePricesForBpoContext(priceContext),
					getSubscriptionPricePlanDao().findAllApplicablePricesForContext(priceContext));
		}
		else
		{
			return getMinimumPriceSelector()
					.selectPrice(getSubscriptionPricePlanDao().findAllApplicablePricesForContext(priceContext), new HashSet<>());
		}
	}

	@Override
	public PriceRowModel getBestApplicablePrice(final TmaPriceContext priceContext)
	{
		if (shouldConsiderPriceOverrides(priceContext.getProduct()))
		{
			return getTmaPriceSelector().selectPrice(findApplicableStandalonePricesForBpoContext(priceContext),
					getSubscriptionPricePlanDao().findAllApplicablePricesForContext(priceContext));
		}
		else
		{
			return getTmaPriceSelector()
					.selectPrice(getSubscriptionPricePlanDao().findAllApplicablePricesForContext(priceContext), new HashSet<>());
		}
	}

	@Override
	public Double getMinimumPriceValue(final TmaPriceContext priceContext)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("priceContext cannot be null", priceContext);

		final PriceRowModel minimumPrice = getMinimumPrice(priceContext);
		if (minimumPrice == null)
		{
			return null;
		}

		if (!(minimumPrice instanceof SubscriptionPricePlanModel))
		{
			return minimumPrice.getPrice();
		}

		final SubscriptionPricePlanModel subscriptionPricePlan = (SubscriptionPricePlanModel) minimumPrice;
		final Collection<RecurringChargeEntryModel> recurringCharges = subscriptionPricePlan.getRecurringChargeEntries();
		if (CollectionUtils.isNotEmpty(recurringCharges))
		{
			return recurringCharges.iterator().next().getPrice();
		}

		final Double priceValue = subscriptionPricePlan.getPrice();
		if (priceValue != null)
		{
			return priceValue;
		}

		final Collection<OneTimeChargeEntryModel> oneTimeCharges = subscriptionPricePlan.getOneTimeChargeEntries();
		if (CollectionUtils.isNotEmpty(oneTimeCharges))
		{
			return oneTimeCharges.iterator().next().getPrice();
		}

		return null;
	}

	@Override
	public PriceRowModel getMinimumPrice(final AbstractOrderEntryModel orderEntryModel)
	{
		ServicesUtil.validateParameterNotNullStandardMessage(ORDER_ENTRY_CANNOT_BE_NULL, orderEntryModel);

		final TmaPriceContext priceContext = getTmaPriceContextService().createPriceContext(orderEntryModel);
		return getMinimumPrice(priceContext);
	}

	@Override
	public PriceRowModel getBestApplicablePrice(final AbstractOrderEntryModel orderEntryModel)
	{
		ServicesUtil.validateParameterNotNullStandardMessage(ORDER_ENTRY_CANNOT_BE_NULL, orderEntryModel);

		final TmaPriceContext priceContext = getTmaPriceContextService().createPriceContext(orderEntryModel);
		return getBestApplicablePrice(priceContext);
	}

	@Override
	public Set<PriceRowModel> filterPricesbyPriceContext(final TmaPriceContext priceContext)
	{
		return getSubscriptionPricePlanDao().findAllApplicablePricesForContext(priceContext);
	}

	private Set<PriceRowModel> findApplicableStandalonePricesForBpoContext(final TmaPriceContext priceContext)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("priceContext cannot be null", priceContext);

		// create a new TmaPriceContext to retrieve also standalone prices
		final TmaPriceContext standalonePriceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(getProduct(priceContext))
				.withProcessTypes(priceContext.getProcessTypes())
				.withSubscriptionTerms(priceContext.getSubscriptionTerms())
				.withRegions(priceContext.getRegions())
				.build();
		return getSubscriptionPricePlanDao()
				.findAllApplicablePricesForContext(standalonePriceContext);
	}

	/**
	 * Creates a {@link TmaPriceContext} based on the attributes values set on the order entry. If a bundle product
	 * offering exists on the order entry then a bundle product offering context is created, otherwise a simple price
	 * context is created
	 *
	 * @param entry
	 * 		order entry for which the context is created
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 * @deprecated since 2007. Use instead {@link TmaPriceContextService#createPriceContext(AbstractOrderEntryModel)}
	 */
	@Deprecated(since = "2007")
	protected TmaPriceContext createPriceContext(final AbstractOrderEntryModel entry)
	{
		ServicesUtil.validateParameterNotNullStandardMessage(ORDER_ENTRY_CANNOT_BE_NULL, entry);
		final Set<SubscriptionTermModel> subscriptionTerms = new HashSet<>();
		final SubscriptionTermModel subscriptionTerm = getSubscriptionTermForEntry(entry);
		if (subscriptionTerm != null)
		{
			subscriptionTerms.add(subscriptionTerm);
		}

		final HashSet<TmaProcessType> processTypes = new HashSet<>();
		if (entry.getProcessType() != null)
		{
			processTypes.add(entry.getProcessType());
		}

		final HashSet<RegionModel> regions = new HashSet<>();
		if (!ObjectUtils.isEmpty(entry.getRegion()))
		{
			regions.add(entry.getRegion());
		}

		final ProductModel entryPo = entry.getProduct();
		final TmaBundledProductOfferingModel bundledProductOffering = entry.getBpo();

		if (bundledProductOffering != null)
		{
			final Set<TmaProductOfferingModel> requiredProductOfferings = getRequiredProductsFromCart(entry, entryPo);
			return TmaPriceContextBuilder.newTmaPriceContextBuilder()
					.withProduct(bundledProductOffering)
					.withProcessTypes(processTypes)
					.withSubscriptionTerms(subscriptionTerms)
					.withtAffectedProduct(entryPo)
					.withRequiredProducts(requiredProductOfferings)
					.withCurrency(entry.getOrder().getCurrency())
					.withRegions(regions)
					.build();
		}
		return TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(entryPo)
				.withProcessTypes(processTypes)
				.withSubscriptionTerms(subscriptionTerms)
				.withCurrency(entry.getOrder().getCurrency())
				.withRegions(regions)
				.build();
	}

	/**
	 * Retrieves all required product offerings found on the current order entry, in order for a price to apply .
	 *
	 * @param entry
	 * 		order entry to retrieve the product for which the price should be configured, as
	 *      {@link TmaPriceContext#getProduct()}.
	 * @param affectedProduct
	 * 		product for which the price will be modified
	 * @return required products necessary for the new price to apply to the {@param affectedProduct}
	 * @deprecated since 2007.
	 */
	@Deprecated(since = "2007")
	protected Set<TmaProductOfferingModel> getRequiredProductsFromCart(final AbstractOrderEntryModel entry,
			final ProductModel affectedProduct)
	{
		if (entry.getBpo() == null)
		{
			return new HashSet<>();
		}

		final AbstractOrderModel order = entry.getOrder();
		final List<AbstractOrderEntryModel> abstractOrderEntryModels = order.getEntries();
		final Set<ProductModel> filteredProductOfferings = getProductOfferingsBelongingToTheSameBpoAndGroup(entry,
				abstractOrderEntryModels);

		return filteredProductOfferings.stream()
				.filter(productOffering -> productOffering instanceof TmaProductOfferingModel
						&& !productOffering.getCode().equals(affectedProduct.getCode()))
				.map(productOffering -> (TmaProductOfferingModel) productOffering).collect(Collectors.toSet());
	}

	private SubscriptionTermModel getSubscriptionTermForEntry(final AbstractOrderEntryModel entry)
	{
		return entry.getSubscriptionInfo() != null ? entry.getSubscriptionInfo().getSubscriptionTerm() : null;
	}

	/**
	 * Filters a given list of entries to retrieve all offerings belonging to the same BPO and entry group as the given entry.
	 *
	 * @param entry
	 * 		entry from which the BPO and entry group is determined
	 * @param abstractOrderEntryModels
	 * 		list of entries to be filtered to identify which ones belong to the same BPO and entry group as the given entry
	 * @return offerings from cart entries belonging to the same BPO and entry group as the given entry.
	 */
	private Set<ProductModel> getProductOfferingsBelongingToTheSameBpoAndGroup(final AbstractOrderEntryModel entry,
			final List<AbstractOrderEntryModel> abstractOrderEntryModels)
	{
		return abstractOrderEntryModels.stream()
				.filter(cartEntry -> cartEntry.getBpo() != null && entry.getBpo().equals(cartEntry.getBpo()))
				.filter(cartEntry -> entry.getEntryGroupNumbers().equals(cartEntry.getEntryGroupNumbers()))
				.map(AbstractOrderEntryModel::getProduct).collect(Collectors.toSet());
	}

	/**
	 * Retrieves all override {@link SubscriptionPricePlanModel}s for the {@link TmaPriceContext} provided.
	 *
	 * @param priceContext
	 * 		priceContext to retrieve the configuration from
	 * @return all {@link SubscriptionPricePlanModel}s having the configuration as on the {@param priceContext} provided
	 * or
	 * empty set in case no price overrides are found.
	 * @deprecated since 2007. Use instead  {@link TmaSubscriptionPricePlanDao#findAllApplicablePricesForContext(TmaPriceContext)}
	 */
	@Deprecated(since = "2007")
	protected Set<SubscriptionPricePlanModel> getPriceOverrides(final TmaPriceContext priceContext)
	{
		final ProductModel contextProduct = priceContext.getProduct();
		if (!shouldConsiderPriceOverrides(contextProduct))
		{
			return new HashSet<>();
		}

		if (priceContext.getAffectedProduct() == null)
		{
			throw new IllegalArgumentException(MessageFormat.format(
					"Cannot retrieve price override for context: " + "Missing affected product configured on BPO with code [{0}]",
					contextProduct.getCode()));
		}

		return getSubscriptionPricePlanDao().findApplicableSubscriptionPricePlans(priceContext);
	}

	/**
	 * Returns the product for which to calculate the price. If the {@link TmaPriceContext#getProduct()} is a BPO, the price
	 * will
	 * be computed for the {@link TmaPriceContext#getAffectedProduct()}.
	 *
	 * @param priceContext
	 * 		price context to retrieve the product from
	 * @return product for which the price should be computed
	 */
	protected ProductModel getProduct(final TmaPriceContext priceContext)
	{
		final ProductModel contextProduct = priceContext.getProduct();
		return contextProduct instanceof TmaFixedBundledProductOfferingModel
				|| contextProduct instanceof TmaSimpleProductOfferingModel ? contextProduct : priceContext.getAffectedProduct();
	}

	/**
	 * Retrieves  standalone prices for given context.
	 *
	 * @param priceContext
	 * 		the context based on which prices are retrieved
	 * @return {@link PriceRowModel}s having the configuration as on the {@param priceContext} provided or empty set in case no
	 * price overrides are found.
	 * @deprecated since 2105. Use instead  {@link TmaSubscriptionPricePlanDao#findAllApplicablePricesForContext(TmaPriceContext)}
	 */
	@Deprecated(since = "2105")
	protected Set<PriceRowModel> getStandalonePrices(final TmaPriceContext priceContext)
	{
		final Set<SubscriptionPricePlanModel> subscriptionPricePlans = getSubscriptionPricePlanDao()
				.findApplicableSubscriptionPricePlans(priceContext);
		final Set<PriceRowModel> priceRows = getSubscriptionPricePlanDao().findApplicablePriceRows(priceContext);

		final HashSet<PriceRowModel> resultingPrices = new HashSet<>(subscriptionPricePlans);
		resultingPrices.addAll(priceRows);

		return resultingPrices;
	}

	private boolean shouldConsiderPriceOverrides(final ProductModel poModel)
	{
		return poModel instanceof TmaBundledProductOfferingModel && !(poModel instanceof TmaFixedBundledProductOfferingModel);
	}

	protected TmaSubscriptionPricePlanDao getSubscriptionPricePlanDao()
	{
		return subscriptionPricePlanDao;
	}

	@Required
	public void setSubscriptionPricePlanDao(final TmaSubscriptionPricePlanDao subscriptionPricePlanDao)
	{
		this.subscriptionPricePlanDao = subscriptionPricePlanDao;
	}

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	protected PriceRowModelComparator getPriceRowComparator()
	{
		return priceRowComparator;
	}

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	@Required
	public void setPriceRowComparator(final PriceRowModelComparator priceRowComparator)
	{
		this.priceRowComparator = priceRowComparator;
	}

	protected TmaPriceContextService getTmaPriceContextService()
	{
		return tmaPriceContextService;
	}

	public void setTmaPriceContextService(final TmaPriceContextService tmaPriceContextService)
	{
		this.tmaPriceContextService = tmaPriceContextService;
	}

	protected TmaPriceSelector getTmaPriceSelector()
	{
		return tmaPriceSelector;
	}

	public void setTmaPriceSelector(TmaPriceSelector tmaPriceSelector)
	{
		this.tmaPriceSelector = tmaPriceSelector;
	}

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	public void setMinimumPriceSelector(DefaultTmaMinimumPriceSelector minimumPriceSelector)
	{
		this.minimumPriceSelector = minimumPriceSelector;
	}

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	protected DefaultTmaMinimumPriceSelector getMinimumPriceSelector()
	{
		return minimumPriceSelector;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
