/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.services.impl;

import de.hybris.platform.b2ctelcoservices.data.TmaRegionPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPriceContextService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;


/**
 * Default implementation of the {@link TmaPriceContextService}.
 *
 * @since 2007
 */
public class DefaultTmaPriceContextService implements TmaPriceContextService
{
	private final EnumerationService enumerationService;
	private final TmaPoService tmaPoService;
	private final CartService cartService;
	private final TmaAbstractOrderEntryService abstractOrderEntryService;

	public DefaultTmaPriceContextService(final EnumerationService enumerationService, final TmaPoService tmaPoService,
			final CartService cartService, final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.enumerationService = enumerationService;
		this.tmaPoService = tmaPoService;
		this.cartService = cartService;
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public TmaPriceContext createPriceContext(final CommerceCartParameter parameter)
	{
		final ProductModel productModel = parameter.getProduct();
		final TmaPriceContext priceContext;
		if (StringUtils.isNotEmpty(parameter.getBpoCode()))
		{
			priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
					.withProduct(getTmaPoService().getPoForCode(parameter.getBpoCode()))
					.withProcessTypes(getProcessTypes(parameter)).withSubscriptionTerms(getSubscriptionTerms(parameter))
					.withtAffectedProduct(productModel).withRegions(getRegions(parameter)).build();
		}
		else
		{
			priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder().withProduct(productModel)
					.withProcessTypes(getProcessTypes(parameter)).withSubscriptionTerms(getSubscriptionTerms(parameter))
					.withUser(parameter.getUser())
					.withRegions(getRegions(parameter)).build();
		}
		return priceContext;
	}

	@Override
	public TmaPriceContext createPriceContext(final AbstractOrderEntryModel entry)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("orderEntry cannot be null", entry);
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

		final UserModel customer = entry.getOrder().getUser();

		final ProductModel entryPo = entry.getProduct();
		final Optional<ProductModel> parentBundledProductOffering = entry.getMasterEntry() != null ?
				Optional.of(entry.getMasterEntry().getProduct()) : Optional.empty();

		if (parentBundledProductOffering.isPresent() && !(parentBundledProductOffering
				.get() instanceof TmaFixedBundledProductOfferingModel))
		{
			final TmaBundledProductOfferingModel rootBundledProductOffering = entry.getBpo();
			final Set<TmaProductOfferingModel> requiredProductOfferings = getAbstractOrderEntryService()
					.getAllChildEntries(entry.getMasterEntry()).stream()
					.map((AbstractOrderEntryModel e) -> (TmaProductOfferingModel) e.getProduct())
					.collect(Collectors.toSet());

			return TmaPriceContextBuilder.newTmaPriceContextBuilder()
					.withProduct(rootBundledProductOffering)
					.withProcessTypes(processTypes)
					.withSubscriptionTerms(subscriptionTerms)
					.withtAffectedProduct(entryPo)
					.withRequiredProducts(requiredProductOfferings)
					.withCurrency(entry.getOrder().getCurrency())
					.withRegions(regions)
					.withUser(customer)
					.build();
		}

		return TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(entryPo)
				.withProcessTypes(processTypes)
				.withSubscriptionTerms(subscriptionTerms)
				.withCurrency(entry.getOrder().getCurrency())
				.withRegions(regions)
				.withUser(customer)
				.build();
	}

	@Override
	public TmaPriceContext createPriceContext(final AbstractOrderEntryModel entry,
			final TmaBundledProductOfferingModel bundledProductOffering)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("orderEntry cannot be null", entry);

		final TmaPriceContext priceContext = createPriceContext(entry);

		if (bundledProductOffering == null)
		{
			return priceContext;
		}

		priceContext.setProduct(bundledProductOffering);
		priceContext.setAffectedProduct(entry.getProduct());

		return priceContext;
	}

	/**
	 * Determines the required products from an entry group.
	 *
	 * @param entry
	 * 		the entry starting from which other offerings part of the same group as the given affected product are identified
	 * @param affectedProduct
	 * 		the affected product
	 * @return set of required products.
	 * @deprecated since 2105.
	 */
	@Deprecated(since = "2105")
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
	 * Determines all offerings from the same entry group.
	 *
	 * @param entry
	 * 		given entry from which the entry group is determined
	 * @param abstractOrderEntryModels
	 * 		entries to be filtered to match the same entry group as the first given entry.
	 * @return offerings belonging to the same BPO and group as the given entry
	 */
	private Set<ProductModel> getProductOfferingsBelongingToTheSameBpoAndGroup(final AbstractOrderEntryModel entry,
			final List<AbstractOrderEntryModel> abstractOrderEntryModels)
	{
		return abstractOrderEntryModels.stream()
				.filter(cartEntry -> cartEntry.getBpo() != null && entry.getBpo().equals(cartEntry.getBpo()))
				.filter(
						cartEntry -> CollectionUtils.isEqualCollection(entry.getEntryGroupNumbers(), cartEntry.getEntryGroupNumbers()))
				.map(AbstractOrderEntryModel::getProduct).collect(Collectors.toSet());
	}

	private Set<RegionModel> getRegions(final CommerceCartParameter parameter)
	{
		if (CollectionUtils.isEmpty(parameter.getPlaces()))
		{
			return Collections.emptySet();
		}
		final Set<RegionModel> regions = new HashSet<>();
		parameter.getPlaces().stream().filter(TmaRegionPlace.class::isInstance).map(TmaRegionPlace.class::cast)
				.collect(Collectors.toList()).stream().map(TmaRegionPlace::getRegion).forEachOrdered(regions::add);
		return regions;
	}

	private Set<TmaProcessType> getProcessTypes(final CommerceCartParameter parameter)
	{
		return new HashSet<>(Arrays.asList(getEnumerationService().getEnumerationValue(TmaProcessType._TYPECODE,
				parameter.getProcessType())));
	}

	private Set<SubscriptionTermModel> getSubscriptionTerms(final CommerceCartParameter parameter)
	{
		final TmaCartSubscriptionInfoModel subscriptionInfo = parameter.getSubscriptionInfo();
		if (ObjectUtils.isEmpty(subscriptionInfo) || ObjectUtils.isEmpty(subscriptionInfo.getSubscriptionTerm()))
		{
			return Collections.emptySet();
		}
		final Set<SubscriptionTermModel> subscriptionTerms = new HashSet<>();
		subscriptionTerms.add(subscriptionInfo.getSubscriptionTerm());
		return subscriptionTerms;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
