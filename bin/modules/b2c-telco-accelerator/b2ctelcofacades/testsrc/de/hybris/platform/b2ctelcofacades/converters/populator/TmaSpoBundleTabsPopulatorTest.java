/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.BpoData;
import de.hybris.platform.b2ctelcofacades.data.BundleTabData;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class TmaSpoBundleTabsPopulatorTest
{
	private TmaSimpleProductOfferingModel spo;
	private ProductData productData;
	private TmaSpoBundleTabsPopulator spoBundleTabsPopulator;
	private AbstractPopulatingConverter<TmaBundledProductOfferingModel, BpoData> bpoConverter;

	@Mock
	private TmaPoService poService;
	@Mock
	private TmaSubscriptionTermService subscriptionTermService;
	@Mock
	private Converter<ProductModel, ProductData> subscriptionProductConverter;
	@Mock
	private TmaPriceFacade priceFacade;
	@Mock
	private Converter<TermOfServiceFrequency, TermOfServiceFrequencyData> termOfServiceFrequencyConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		spo = new TmaSimpleProductOfferingModel();
		spo.setCode("spo1");
		productData = new ProductData();
		bpoConverter = new AbstractPopulatingConverter();
		bpoConverter.setTargetClass(BpoData.class);
		bpoConverter.setPopulators(Arrays.asList(new TmaBpoPopulator()));

		spoBundleTabsPopulator = new TmaSpoBundleTabsPopulator();
		spoBundleTabsPopulator.setTmaPoService(poService);
		spoBundleTabsPopulator.setTmaSubscriptionTermService(subscriptionTermService);
		spoBundleTabsPopulator.setBpoConverter(bpoConverter);
		spoBundleTabsPopulator.setTmaSubscriptionProductConverter(subscriptionProductConverter);
		spoBundleTabsPopulator.setTmaPriceFacade(priceFacade);
		spoBundleTabsPopulator.setTermOfServiceFrequencyConverter(termOfServiceFrequencyConverter);
	}

	@Test
	public void givenSpoInNoBpo_thenProductDataHasNoBundleTabs()
	{
		givenParents(spo);
		whenProductDataIsPopulated();
		thenProductDataContainsBundleTabs(0);
	}

	@Test
	public void givenSpoInOneBpo_thenProductDataHasOneBundleTab()
	{
		givenParents(spo, createBpo("bpo1", Optional.empty()));
		whenProductDataIsPopulated();
		thenProductDataContainsBundleTabs(1);
	}

	@Test
	public void givenSpoInMultipleBpos_thenProductDataHasMultipleBundleTabs()
	{
		final TmaBundledProductOfferingModel bpo1 = createBpo("bpo1", Optional.empty());
		final TmaBundledProductOfferingModel bpo11 = createBpo("bpo2", Optional.empty());
		final TmaBundledProductOfferingModel bpo21 = createBpo("bpo3", Optional.of(createGroup(new HashSet<>())));

		givenParents(spo, bpo21);
		givenParents(bpo21, bpo11, bpo1);
		givenSubscriptionTerms(spo, bpo21, new HashSet<>());
		whenProductDataIsPopulated();
		thenProductDataContainsBundleTabs(3);
	}

	@Test
	public void givenSpoWithSubscriptionTerms_thenProductDataHasBundleTabWithFrequencyTabs()
	{
		final TmaBundledProductOfferingModel bpo = createBpo("bpo1", Optional.of(
				createGroup(new HashSet<>(Arrays.asList(spo)))));
		final Set<SubscriptionTermModel> subscriptionTerms = createSubscriptionTerms(12, 24, 36);

		givenParents(spo, bpo);
		givenSubscriptionTerms(spo, bpo, subscriptionTerms);
		whenProductDataIsPopulated();
		thenProductDataContainsBundleTabs(1);
		thenBundleTabContainsFrequencyTabs(productData.getBundleTabs().get(0), 3);
	}

	private TmaProductOfferingGroupModel createGroup(final Set<TmaProductOfferingModel> childProductOfferings)
	{
		final TmaProductOfferingGroupModel group = new TmaProductOfferingGroupModel();
		group.setChildProductOfferings(childProductOfferings);
		return group;
	}

	private void givenParents(final TmaProductOfferingModel po, final TmaBundledProductOfferingModel... parents)
	{
		po.setParents(new HashSet<>(Arrays.asList(parents)));
		when(poService.getAllParents(po)).thenReturn(Arrays.stream(parents).collect(Collectors.toSet()));
	}

	private void givenSubscriptionTerms(final TmaSimpleProductOfferingModel spo, final TmaBundledProductOfferingModel bpo,
			final Set<SubscriptionTermModel> subscriptionTerms)
	{
		when(subscriptionTermService.getApplicableSubscriptionTerms(spo, bpo, TmaProcessType.ACQUISITION))
				.thenReturn(subscriptionTerms);
		when(subscriptionProductConverter.convert(spo)).thenReturn(new ProductData());
	}

	private void whenProductDataIsPopulated()
	{
		spoBundleTabsPopulator.populate(spo, productData);
	}

	private void thenProductDataContainsBundleTabs(final int bundleTabsCount)
	{
		Assert.assertEquals(bundleTabsCount, productData.getBundleTabs().size());
	}

	private void thenBundleTabContainsFrequencyTabs(final BundleTabData bundleTab, final int frequencyTabsCount)
	{
		Assert.assertEquals(frequencyTabsCount, bundleTab.getFrequencyTabs().size());
	}

	private TmaBundledProductOfferingModel createBpo(final String code,
			final Optional<TmaProductOfferingGroupModel> nextToProductGroup)
	{
		final TmaBundledProductOfferingModel bpo = new TmaBundledProductOfferingModel();
		setEnglishLocaleProvider(bpo);
		bpo.setCode(code);
		when(poService.getGroupNextToProductGroup(bpo, spo)).thenReturn(nextToProductGroup);
		return bpo;
	}

	private Set<SubscriptionTermModel> createSubscriptionTerms(final int... months)
	{
		final Set<SubscriptionTermModel> subscriptionTerms = new HashSet<>();
		for (final int monthNumber : months)
		{
			final SubscriptionTermModel subscriptionTerm = new SubscriptionTermModel();
			setEnglishLocaleProvider(subscriptionTerm);
			subscriptionTerm.setTermOfServiceFrequency(TermOfServiceFrequency.MONTHLY);
			subscriptionTerm.setTermOfServiceNumber(monthNumber);
			subscriptionTerm.setName(monthNumber + " months contract");
			subscriptionTerms.add(subscriptionTerm);

			final TermOfServiceFrequencyData termOfServiceFrequencyData = new TermOfServiceFrequencyData();
			termOfServiceFrequencyData.setCode(TermOfServiceFrequency.MONTHLY.getCode());
			termOfServiceFrequencyData.setName(monthNumber + " name");
			when(termOfServiceFrequencyConverter.convert(subscriptionTerm.getTermOfServiceFrequency()))
					.thenReturn(termOfServiceFrequencyData);
		}
		return subscriptionTerms;
	}

	private void setEnglishLocaleProvider(final AbstractItemModel item)
	{
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(item)).setLocaleProvider(new StubLocaleProvider(
				Locale.ENGLISH));
	}
}
