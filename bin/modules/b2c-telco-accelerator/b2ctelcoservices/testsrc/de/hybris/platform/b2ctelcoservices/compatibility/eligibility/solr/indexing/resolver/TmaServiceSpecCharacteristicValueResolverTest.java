/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecCharacteristicValueModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for {@link TmaServiceSpecCharacteristicValueResolver}
 *
 * @since 2102
 *
 */

@UnitTest
public class TmaServiceSpecCharacteristicValueResolverTest
{
	private static final String DATA_5G = "data_5g";

	@Mock
	private TmaSpoSource spoSource;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private InputDocument inputDocument;
	@Mock
	private IndexedProperty indexedProp;
	@Mock
	private IndexerBatchContext indexerBatchCtx;
	@Mock
	private IndexConfig indexConfig;
	@Mock
	private FacetSearchConfig facetSearchConfig;

	private TmaServiceSpecCharacteristicValueResolver tmaServiceSpecCharacteristicValueResolver;
	private Collection<LanguageModel> languageModel;
	private TmaProductOfferingModel productOfferingModel;
	private Set<TmaProductSpecCharacteristicValueModel> pscv;
	private Set<TmaServiceSpecCharacteristicValueModel> sscv;
	private TmaProductSpecCharacteristicValueModel prodSpecCharVal;
	private TmaServiceSpecCharacteristicValueModel serviceSpcCharVal;
	private UsageUnitModel usageUnit;
	private TmaServiceSpecCharacteristicModel tmaServiceSpecChar;
	private PriceRowModel priceRowModel;
	private LanguageModel model;
	private TmaProductSpecCharValueUseModel prodSpecCharValUse;
	private Set<TmaProductSpecCharValueUseModel> pscvu;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		tmaServiceSpecCharacteristicValueResolver = new TmaServiceSpecCharacteristicValueResolver(commonI18NService, spoSource);
		languageModel = new ArrayList<>();
		productOfferingModel = new TmaProductOfferingModel();
		pscv = new HashSet<>();
		pscvu = new HashSet<>();
		sscv = new HashSet<>();
		prodSpecCharVal = new TmaProductSpecCharacteristicValueModel();
		prodSpecCharValUse = new TmaProductSpecCharValueUseModel();
		serviceSpcCharVal = new TmaServiceSpecCharacteristicValueModel();
		usageUnit = new UsageUnitModel();
		tmaServiceSpecChar = new TmaServiceSpecCharacteristicModel();
		priceRowModel = new PriceRowModel();
		priceRowModel.setProduct(productOfferingModel);
		model = new LanguageModel();
	}

	@Test
	public void testAddFieldValues() throws FieldValueProviderException
	{
		model.setIsocode("en");
		languageModel.add(model);
		indexConfig.setLanguages(languageModel);
		facetSearchConfig.setIndexConfig(indexConfig);
		usageUnit.setName("GB", Locale.ENGLISH);
		tmaServiceSpecChar.setId(DATA_5G);
		serviceSpcCharVal.setValue("30", Locale.ENGLISH);
		serviceSpcCharVal.setServiceSpecCharacteristic(tmaServiceSpecChar);
		serviceSpcCharVal.setUnitOfMeasure(usageUnit);
		sscv.add(serviceSpcCharVal);
		prodSpecCharVal.setServiceSpecCharacteristicValues(sscv);
		prodSpecCharVal.setId(DATA_5G);
		pscv.add(prodSpecCharVal);
		prodSpecCharValUse.setProductSpecCharacteristicValues(pscv);
		pscvu.add(prodSpecCharValUse);
		productOfferingModel.setProductSpecCharValueUses(pscvu);

		Mockito.when(spoSource.getProduct(any())).thenReturn(productOfferingModel);
		Mockito.when(indexedProp.getValueProviderParameter()).thenReturn(DATA_5G);
		Mockito.when(indexedProp.getName()).thenReturn("datavolume_sscv");
		Mockito.when(indexedProp.getType()).thenReturn("string");
		Mockito.when(indexedProp.isLocalized()).thenReturn(true);
		Mockito.when(indexConfig.getLanguages()).thenReturn(languageModel);
		Mockito.when(facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);
		Mockito.when(indexerBatchCtx.getFacetSearchConfig()).thenReturn(facetSearchConfig);
		Mockito.when(commonI18NService.getLocaleForLanguage(model)).thenReturn(Locale.ENGLISH);
		tmaServiceSpecCharacteristicValueResolver.addFieldValues(inputDocument, indexerBatchCtx, indexedProp, priceRowModel, null);
		Mockito.verify(inputDocument, times(1)).addField(anyString(), anyObject());
	}
}
