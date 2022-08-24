/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.BpoData;
import de.hybris.platform.b2ctelcofacades.data.BundleTabData;
import de.hybris.platform.b2ctelcofacades.data.FrequencyTabData;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceFacade;
import de.hybris.platform.b2ctelcofacades.product.FrequencyTabDataComparator;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Sets;


/**
 * Populates {@link ProductData} with bundle tabs, starting from the PO and going up through all the parents in the
 * hierarchy.
 *
 * @since 6.7
 */
public class TmaSpoBundleTabsPopulator extends AbstractProductPopulator<TmaProductOfferingModel, ProductData>
{
	private static final TmaProcessType DEFAULT_PROCESS_TYPE = TmaProcessType.ACQUISITION;

	private TmaPoService tmaPoService;
	private TmaSubscriptionTermService tmaSubscriptionTermService;
	private TmaPriceFacade tmaPriceFacade;
	private Converter<TmaBundledProductOfferingModel, BpoData> bpoConverter;
	private Converter<ProductModel, ProductData> tmaSubscriptionProductConverter;
	private Converter<TermOfServiceFrequency, TermOfServiceFrequencyData> termOfServiceFrequencyConverter;

	@Override
	public void populate(final TmaProductOfferingModel mainPo, final ProductData productData)
	{
		final List<BundleTabData> bundleTabs = new ArrayList<>();
		final Set<TmaBundledProductOfferingModel> directParents = mainPo.getParents();
		final Set<TmaBundledProductOfferingModel> indirectParents = new HashSet<>();

		directParents.forEach(bpo ->
		{
			if (bpo instanceof TmaFixedBundledProductOfferingModel)
			{
				return;
			}
			//keep only product offerings from the group next to product group to be shown on the bundle tab; display empty tab if
			//next group is not found
			final Optional<TmaProductOfferingGroupModel> groupNextToProductGroup = getTmaPoService().getGroupNextToProductGroup(bpo,
					mainPo);
			bpo.setProductOfferingGroups(groupNextToProductGroup.map(Arrays::asList).orElseGet(Collections::emptyList));
			bundleTabs.add(getPoBundleTabForBpo(mainPo, bpo));
			indirectParents.addAll(getTmaPoService().getAllParents(bpo));
		});

		indirectParents.forEach(bpo ->
		{
			if (bpo instanceof TmaFixedBundledProductOfferingModel)
			{
				return;
			}
			//no product offerings shall be displayed on bundle tab for indirect parents
			bpo.setProductOfferingGroups(Collections.emptyList());
			bundleTabs.add(getPoBundleTabForBpo(mainPo, bpo));
		});

		productData.setBundleTabs(bundleTabs);
	}

	/**
	 * Returns the populated Bundle Tab with information retrieved from the bpo.
	 *
	 * @param mainPo
	 *           po for which to retrieve the information
	 * @param bpo
	 *           bpo to retrieve information from
	 * @return populated {@link BundleTabData}
	 */
	private BundleTabData getPoBundleTabForBpo(final TmaProductOfferingModel mainPo, final TmaBundledProductOfferingModel bpo)
	{
		final BundleTabData bundleTab = new BundleTabData();
		bundleTab.setParentBpo(getBpoConverter().convert(bpo));
		bundleTab.setFrequencyTabs(getPoFrequencyTabsForBpo(mainPo, bpo));
		return bundleTab;
	}

	/**
	 * Returns the {@link FrequencyTabData} for the {@param mainPo} given, populated with data out of the {@param bpo}.
	 *
	 * @param mainPo
	 *           po for which to retrieve the information
	 * @param bpo
	 *           bpo to retrieve information from
	 * @return {@link FrequencyTabData} related to the {@param mainPo}
	 */
	private List<FrequencyTabData> getPoFrequencyTabsForBpo(final TmaProductOfferingModel mainPo,
			final TmaBundledProductOfferingModel bpo)
	{
		final Map<String, FrequencyTabData> frequencyTabs = new HashMap<>();
		final List<TmaProductOfferingModel> childProductOfferings = bpo.getProductOfferingGroups().stream()
				.flatMap(group -> group.getChildProductOfferings().stream())
				.collect(Collectors.toList());
		for (final TmaProductOfferingModel tabProductOffering : childProductOfferings)
		{
			final Set<SubscriptionTermModel> applicableSubscriptionTerms = getTmaSubscriptionTermService()
					.getApplicableSubscriptionTerms(tabProductOffering, bpo, TmaProcessType.ACQUISITION);
			if (!applicableSubscriptionTerms.isEmpty())
			{
				addFrequencyData(bpo, frequencyTabs, tabProductOffering, mainPo, applicableSubscriptionTerms);
			}
		}
		return sortFrequencies(frequencyTabs);
	}

	private void addFrequencyData(final TmaBundledProductOfferingModel parentBpo,
			final Map<String, FrequencyTabData> frequencyTabs, final TmaProductOfferingModel tabProductOffering,
			final TmaProductOfferingModel mainPo, final Set<SubscriptionTermModel> subscriptionTerms)
	{
		subscriptionTerms.forEach(subscriptionTerm ->
		{
			tabProductOffering.setSubscriptionTerm(subscriptionTerm);
			final ProductData subscriptionProductData = getTmaSubscriptionProductConverter().convert(tabProductOffering);
			setAdditionalSpoPriceInBpo(parentBpo, tabProductOffering, mainPo, subscriptionTerm, subscriptionProductData);
			setMainSpoPriceInBpo(parentBpo, tabProductOffering, mainPo, subscriptionTerm, subscriptionProductData);

			final FrequencyTabData frequencyTab = generateFrequencyTab(frequencyTabs, subscriptionTerm);
			frequencyTab.getProducts().add(subscriptionProductData);
			frequencyTabs.put(subscriptionTerm.getName(), frequencyTab);
		});
	}

	private void setMainSpoPriceInBpo(final TmaBundledProductOfferingModel parentBpo,
			final TmaProductOfferingModel tabProductOffering,
			final TmaProductOfferingModel mainPo, final SubscriptionTermModel subscriptionTerm,
			final ProductData subscriptionProductData)
	{
		final PriceData mainSpoPriceInBpo = getApplicablePrice(parentBpo, tabProductOffering, mainPo, subscriptionTerm);
		if (mainSpoPriceInBpo == null)
		{
			return;
		}
		subscriptionProductData.setMainSpoPriceInBpo(mainSpoPriceInBpo);
	}

	private void setAdditionalSpoPriceInBpo(final TmaBundledProductOfferingModel parentBpo,
			final TmaProductOfferingModel tabProductOffering,
			final TmaProductOfferingModel mainPo, final SubscriptionTermModel subscriptionTerm,
			final ProductData subscriptionProductData)
	{
		final PriceData additionalSpoPriceInBpo = getApplicablePrice(parentBpo, mainPo, tabProductOffering, subscriptionTerm);
		if (additionalSpoPriceInBpo == null)
		{
			return;
		}
		subscriptionProductData.setAdditionalSpoPriceInBpo(additionalSpoPriceInBpo);
	}

	private PriceData getApplicablePrice(TmaBundledProductOfferingModel parentBpo, TmaProductOfferingModel tabProductOffering,
			TmaProductOfferingModel mainPo, SubscriptionTermModel subscriptionTerm)
	{
		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(parentBpo)
				.withProcessTypes(new HashSet<>(Collections.singletonList(DEFAULT_PROCESS_TYPE)))
				.withSubscriptionTerms(new HashSet<>(Collections.singletonList(subscriptionTerm)))
				.withtAffectedProduct(mainPo)
				.withRequiredProducts(Sets.newHashSet(tabProductOffering))
				.build();

		return getTmaPriceFacade().getBestApplicablePrice(priceContext);
	}

	private FrequencyTabData generateFrequencyTab(final Map<String, FrequencyTabData> frequencyTabs,
			final SubscriptionTermModel subscriptionTerm)
	{
		FrequencyTabData frequencyTab = frequencyTabs.get(subscriptionTerm.getName());
		if (frequencyTab == null)
		{
			frequencyTab = new FrequencyTabData();
			frequencyTab.setTermOfServiceFrequency(
					getTermOfServiceFrequencyConverter().convert(subscriptionTerm.getTermOfServiceFrequency()));
			frequencyTab.setTermOfServiceNumber(
					subscriptionTerm.getTermOfServiceNumber() == null ? 0 : subscriptionTerm.getTermOfServiceNumber());
			frequencyTab.setSubscriptionTermName(subscriptionTerm.getName());
			frequencyTab.setProducts(new ArrayList<>());
		}
		return frequencyTab;
	}

	private List<FrequencyTabData> sortFrequencies(final Map<String, FrequencyTabData> frequencyTabs)
	{
		final List<FrequencyTabData> sortedFrequencies = new ArrayList<>(frequencyTabs.values());
		sortedFrequencies.sort(new FrequencyTabDataComparator());
		Collections.reverse(sortedFrequencies);
		return sortedFrequencies;
	}

	protected Converter<TmaBundledProductOfferingModel, BpoData> getBpoConverter()
	{
		return bpoConverter;
	}

	@Required
	public void setBpoConverter(final Converter<TmaBundledProductOfferingModel, BpoData> bpoConverter)
	{
		this.bpoConverter = bpoConverter;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	@Required
	public void setTmaPoService(final TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	protected TmaSubscriptionTermService getTmaSubscriptionTermService()
	{
		return tmaSubscriptionTermService;
	}

	@Required
	public void setTmaSubscriptionTermService(
			final TmaSubscriptionTermService tmaSubscriptionTermService)
	{
		this.tmaSubscriptionTermService = tmaSubscriptionTermService;
	}

	protected Converter<ProductModel, ProductData> getTmaSubscriptionProductConverter()
	{
		return tmaSubscriptionProductConverter;
	}

	@Required
	public void setTmaSubscriptionProductConverter(final Converter<ProductModel, ProductData> tmaSubscriptionProductConverter)
	{
		this.tmaSubscriptionProductConverter = tmaSubscriptionProductConverter;
	}

	protected Converter<TermOfServiceFrequency, TermOfServiceFrequencyData> getTermOfServiceFrequencyConverter()
	{
		return termOfServiceFrequencyConverter;
	}

	@Required
	public void setTermOfServiceFrequencyConverter(
			final Converter<TermOfServiceFrequency, TermOfServiceFrequencyData> termOfServiceFrequencyConverter)
	{
		this.termOfServiceFrequencyConverter = termOfServiceFrequencyConverter;
	}

	protected TmaPriceFacade getTmaPriceFacade()
	{
		return tmaPriceFacade;
	}

	@Required
	public void setTmaPriceFacade(final TmaPriceFacade tmaPriceFacade)
	{
		this.tmaPriceFacade = tmaPriceFacade;
	}

}
