/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaGenericSearchProcessor;
import de.hybris.platform.b2ctelcoservices.daos.TmaSearchQueryException;
import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionPricePlanDao;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductPriceClassModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link TmaSubscriptionPricePlanDao}.
 *
 * @since 6.7
 */
public class DefaultTmaSubscriptionPricePlanDao implements TmaSubscriptionPricePlanDao
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTmaSubscriptionPricePlanDao.class);

	private GenericSearchService genericSearchService;
	private TmaGenericSearchProcessor priceRowProcessor;
	private TmaGenericSearchProcessor subscriptionPricePlanProcessor;
	/**
	 * @deprecated since 2003. Use instead tmaSubscriptionPricePlanSearchProcessor
	 */
	@Deprecated(since = "2003")
	private TmaGenericSearchProcessor tmaSppFilterSearchProcessor;
	/**
	 * @deprecated since 2003. Use instead tmaPriceRowSearchProcessor
	 */
	@Deprecated(since = "2003")
	private TmaGenericSearchProcessor tmaPriceRowFilterSearchProcessor;

	@Override
	public Set<PriceRowModel> findAllApplicablePricesForContext(final TmaPriceContext priceContext)
	{
		return findPriceRows(priceContext, false);
	}

	@Override
	public Set<SubscriptionPricePlanModel> findApplicableSubscriptionPricePlans(final TmaPriceContext priceContext)
	{
		final GenericQuery query = new GenericQuery(SubscriptionPricePlanModel._TYPECODE);
		try
		{
			getSubscriptionPricePlanProcessor().enhanceQuery(query, priceContext);
		}
		catch (final TmaSearchQueryException exception)
		{
			LOG.debug(MessageFormat.format(
					"The search query cannot be enhanced... returning empty results for product: {0} and affected product: {1}",
					priceContext.getProduct(), priceContext.getAffectedProduct()), exception);
			return Collections.emptySet();
		}

		final HashSet<SubscriptionPricePlanModel> poPrices = new HashSet<>(
				getGenericSearchService().<SubscriptionPricePlanModel>search(query).getResult());
		if (CollectionUtils.isNotEmpty(poPrices) && shouldFilterByRequiredProducts(priceContext))
		{
			return filterByRequiredProducts(poPrices, priceContext.getRequiredProducts());
		}
		return poPrices;
	}

	@Override
	public Set<PriceRowModel> findApplicablePriceRows(final TmaPriceContext priceContext)
	{
		return findPriceRows(priceContext, true);
	}

	@Override
	public Set<PriceRowModel> filterPricesForContext(final TmaPriceContext priceContext)
	{
		final Set<SubscriptionPricePlanModel> subscriptionPricePlans = filterSubscriptionPricePlans(priceContext);
		final Set<PriceRowModel> priceRows = filterPriceRows(priceContext);
		final HashSet<PriceRowModel> resultingPrices = new HashSet<>(subscriptionPricePlans);
		resultingPrices.addAll(priceRows);
		return resultingPrices;
	}

	/**
	 * Determines whether the filtering based on {@link TmaPriceContext#getRequiredProducts()} should be applied considering
	 * the following rules:
	 * <ul>
	 * <li>the {@link TmaPriceContext#getProduct()} is a {@link TmaBundledProductOfferingModel}</li>
	 * <li>the {@link TmaPriceContext#getRequiredProducts()} is a non-null object (empty set is considered as non-null
	 * object)</li>
	 * </ul>
	 *
	 * @param priceContext
	 * 		the price context for which the decision to apply the filtering is made
	 * @return <code>true</code> is case the above rules are applied, <code>false</code> otherwise
	 */
	protected boolean shouldFilterByRequiredProducts(final TmaPriceContext priceContext)
	{
		return priceContext.getProduct() instanceof TmaBundledProductOfferingModel &&
				priceContext.getRequiredProducts() != null;
	}

	/**
	 * Returns the prices on which the required products set match the required products sent as parameter, otherwise
	 * returns the prices on which the required price class set match the required price class retrieved from the
	 * required products sent
	 *
	 * @param poPrices
	 * 		the prices to be filtered
	 * @param requiredProducts
	 * 		the required products to be compared with the ones set on the prices
	 * @return the filtered prices
	 * @deprecated since 2007. Use instead filterAfterRequiredProducts(Set<PDTRowModel>, Set<ProductModel>)
	 */
	@Deprecated(since = "2007")
	protected Set<SubscriptionPricePlanModel> filterByRequiredProducts(final Set<SubscriptionPricePlanModel> poPrices,
			final Set<ProductModel> requiredProducts)
	{
		return poPrices.stream()
				.filter(poPrice -> doesProductFilteringMatch(poPrice, requiredProducts)).collect(Collectors.toSet());
	}

	/**
	 * Retrieves all instances of {@link PriceRowModel}s exclusively if type exclusive is true
	 * otherwise all instances of {@link PriceRowModel}s and subtypes, for the {@param priceContext} provided.
	 * In case of BPO with affected product it retrieves only price overrides.
	 *
	 * @param priceContext
	 * 		priceContext to retrieve the configuration from
	 * @param typeExclusive
	 * 		true if exclusive type should be returned, otherwise false
	 * @return {@link PriceRowModel}s having the configuration provided by the {@param priceContext}
	 */
	protected Set<PriceRowModel> findPriceRows(final TmaPriceContext priceContext, boolean typeExclusive)
	{
		final GenericQuery query = new GenericQuery(PriceRowModel._TYPECODE, typeExclusive);
		try
		{
			getPriceRowProcessor().enhanceQuery(query, priceContext);
			enhanceQueryWithSortingCondition(query);
		}
		catch (final TmaSearchQueryException exception)
		{
			LOG.debug("The search query cannot be enhanced... returning empty results!", exception);
			return Collections.emptySet();
		}

		final Set<PriceRowModel> poPrices = new LinkedHashSet<>(
				getGenericSearchService().<PriceRowModel>search(query).getResult());
		if (CollectionUtils.isNotEmpty(poPrices) && shouldFilterByRequiredProducts(priceContext))
		{
			return filterAfterRequiredProducts(poPrices, priceContext.getRequiredProducts());
		}
		return poPrices;
	}

	/**
	 * Enhance the query with a condition to sort descending after priority
	 *
	 * @param query
	 * 		the query to be enhanced
	 */
	protected void enhanceQueryWithSortingCondition(final GenericQuery query)
	{
		final GenericSearchField prioritySearchField = new GenericSearchField(null, PriceRowModel.PRIORITY);
		query.addOrderBy(new GenericSearchOrderBy(prioritySearchField, false));
	}

	/**
	 * Returns the price rows on which the required products set match the required products sent as parameter, otherwise
	 * returns the price rows on which the required price class set match the required price class retrieved from the
	 * required products sent
	 *
	 * @param priceRows
	 * 		the price rows to be filtered
	 * @param requiredProducts
	 * 		the required products to be compared with the ones set on the prices
	 * @return the filtered prices
	 */
	protected Set<PriceRowModel> filterAfterRequiredProducts(final Set<PriceRowModel> priceRows,
			final Set<ProductModel> requiredProducts)
	{
		return priceRows.stream().filter(price -> doesProductFilteringMatch(price, requiredProducts))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	 * @deprecated since 2003. Use instead
	 * {@link TmaSubscriptionPricePlanDao#findApplicableSubscriptionPricePlans(priceContext)}
	 */
	@Deprecated(since = "2003")
	protected Set<SubscriptionPricePlanModel> filterSubscriptionPricePlans(final TmaPriceContext priceContext)
	{
		final GenericQuery query = new GenericQuery(SubscriptionPricePlanModel._TYPECODE);
		try
		{
			getTmaSppFilterSearchProcessor().enhanceQuery(query, priceContext);
		}
		catch (final TmaSearchQueryException exception)
		{
			LOG.error("The search query cannot be enhanced... returning empty results!", exception);
			return Collections.emptySet();
		}

		return new HashSet<>(getGenericSearchService().<SubscriptionPricePlanModel>search(query).getResult());

	}

	/**
	 * @deprecated since 2003. Use instead
	 * {@link TmaSubscriptionPricePlanDao#findApplicablePriceRows(priceContext)}
	 */
	@Deprecated(since = "2003")
	protected Set<PriceRowModel> filterPriceRows(final TmaPriceContext priceContext)
	{
		final GenericQuery query = new GenericQuery(PriceRowModel._TYPECODE, true);
		try
		{
			getTmaPriceRowFilterSearchProcessor().enhanceQuery(query, priceContext);
		}
		catch (final TmaSearchQueryException exception)
		{
			LOG.error("The search query cannot be enhanced... returning empty results!", exception);
			return Collections.emptySet();
		}

		return new HashSet<>(getGenericSearchService().<PriceRowModel>search(query).getResult());
	}

	private boolean doesProductFilteringMatch(final PriceRowModel poPrice, final Set<ProductModel> requiredProducts)
	{
		final Set<ProductModel> requiredPriceProductOffering = poPrice.getRequiredProductOfferings();
		final Set<TmaProductPriceClassModel> requiredPriceClasses = requiredProducts.stream()
				.map(ProductModel::getProductPriceClass).collect(Collectors.toSet());
		final Set<TmaProductPriceClassModel> requiredPriceProductClasses = poPrice.getRequiredProductClasses();

		return doesElementsMatch(requiredProducts, requiredPriceProductOffering)
				&& doesElementsMatch(requiredPriceClasses, requiredPriceProductClasses);
	}

	/**
	 * Verifies if the given sets match by verifying if the first set contains all elements of the second set.
	 *
	 * @param containingSet
	 * 		set which should contain all elements for the match to apply
	 * @param containedSet
	 * 		set to be contained in the {@param containingSet} for the match to apply
	 * @return true if the sets partially match and false otherwise
	 */
	private boolean doesElementsMatch(final Set<?> containingSet, final Set<?> containedSet)
	{
		return containingSet.containsAll(containedSet);
	}

	protected GenericSearchService getGenericSearchService()
	{
		return genericSearchService;
	}

	@Required
	public void setGenericSearchService(final GenericSearchService genericSearchService)
	{
		this.genericSearchService = genericSearchService;
	}

	protected TmaGenericSearchProcessor getSubscriptionPricePlanProcessor()
	{
		return subscriptionPricePlanProcessor;
	}

	@Required
	public void setSubscriptionPricePlanProcessor(final TmaGenericSearchProcessor subscriptionPricePlanProcessor)
	{
		this.subscriptionPricePlanProcessor = subscriptionPricePlanProcessor;
	}

	protected TmaGenericSearchProcessor getPriceRowProcessor()
	{
		return priceRowProcessor;
	}

	@Required
	public void setPriceRowProcessor(final TmaGenericSearchProcessor priceRowProcessor)
	{
		this.priceRowProcessor = priceRowProcessor;
	}

	protected TmaGenericSearchProcessor getTmaSppFilterSearchProcessor()
	{
		return tmaSppFilterSearchProcessor;
	}

	@Required
	public void setTmaSppFilterSearchProcessor(final TmaGenericSearchProcessor tmaSppFilterSearchProcessor)
	{
		this.tmaSppFilterSearchProcessor = tmaSppFilterSearchProcessor;
	}

	protected TmaGenericSearchProcessor getTmaPriceRowFilterSearchProcessor()
	{
		return tmaPriceRowFilterSearchProcessor;
	}

	@Required
	public void setTmaPriceRowFilterSearchProcessor(final TmaGenericSearchProcessor tmaPriceRowFilterSearchProcessor)
	{
		this.tmaPriceRowFilterSearchProcessor = tmaPriceRowFilterSearchProcessor;
	}
}
