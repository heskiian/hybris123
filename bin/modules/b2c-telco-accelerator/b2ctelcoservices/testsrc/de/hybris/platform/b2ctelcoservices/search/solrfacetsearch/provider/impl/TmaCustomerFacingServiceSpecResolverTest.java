/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.b2ctelcoservices.model.TmaCustomerFacingServiceSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecCharacteristicValueModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for {@link TmaCustomerFacingServiceSpecResolver}
 *
 * @since 2102
 *
 */
@UnitTest
public class TmaCustomerFacingServiceSpecResolverTest
{
	@Mock
	private TmaSpoSource spoSource;
	@Mock
	private InputDocument inputDocument;
	@Mock
	private IndexedProperty indexedProp;

	private TmaProductOfferingModel productOfferingModel;
	private Set<TmaProductSpecCharacteristicValueModel> pscv;
	private Set<TmaServiceSpecCharacteristicValueModel> sscv;
	private TmaProductSpecCharacteristicValueModel prodSpecCharVal;
	private TmaServiceSpecCharacteristicValueModel serviceSpcCharVal;
	private UsageUnitModel usageUnit;
	private TmaServiceSpecCharacteristicModel tmaServiceSpecChar;
	private PriceRowModel priceRowModel;
	private TmaProductSpecificationModel productSpecificationModel;
	private Set<TmaCustomerFacingServiceSpecModel> cfsSpecs;
	private TmaCustomerFacingServiceSpecModel cfsSpec;
	private TmaCustomerFacingServiceSpecResolver customerFacingServiceSpecResolver;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		customerFacingServiceSpecResolver = new TmaCustomerFacingServiceSpecResolver(spoSource);
		productOfferingModel = new TmaProductOfferingModel();
		pscv = new HashSet<>();
		sscv = new HashSet<>();
		prodSpecCharVal = new TmaProductSpecCharacteristicValueModel();
		serviceSpcCharVal = new TmaServiceSpecCharacteristicValueModel();
		usageUnit = new UsageUnitModel();
		tmaServiceSpecChar = new TmaServiceSpecCharacteristicModel();
		priceRowModel = new PriceRowModel();
		priceRowModel.setProduct(productOfferingModel);
		productSpecificationModel = new TmaProductSpecificationModel();
		cfsSpecs = new HashSet<>();
		cfsSpec = new TmaCustomerFacingServiceSpecModel();
	}

	@Test
	public void testAddFieldValues() throws FieldValueProviderException
	{
		final InputDocument inputDocument = Mockito.mock(InputDocument.class);
		final IndexedProperty indexedProperty = Mockito.mock(IndexedProperty.class);
		cfsSpec.setId("voice_svc");
		cfsSpecs.add(cfsSpec);
		productSpecificationModel.setCfsSpecs(cfsSpecs);
		productOfferingModel.setProductSpecification(productSpecificationModel);
		Mockito.when(spoSource.getProduct(priceRowModel)).thenReturn(productOfferingModel);
		customerFacingServiceSpecResolver.addFieldValues(inputDocument, null, indexedProperty, priceRowModel, null);
		verify(inputDocument, times(1)).addField(Mockito.anyString(), Mockito.anyString());
	}
}
