/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.dataimport.batch.price;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


/**
 * Test for {@link TmaPriceRowTranslator}
 *
 * @since 1907
 */
@IntegrationTest
public class TmaPriceRowTranslatorTest extends ServicelayerTest
{
	private static final String USD = "USD";
	private static final String PIECES = "pieces";

	@Mock
	private Item item1;
	@Mock
	private Item item2;

	@Resource
	private ModelService modelService;
	@Resource
	private TmaPriceRowExportAdapter tmaPriceRowExportAdapter;
	@Resource
	private TmaPriceRowImportAdapter tmaPriceRowImportAdapter;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	UserService userService;
	@Resource
	UnitService unitService;
	@Resource
	ProductService productService;

	private TmaPriceRowTranslator translator;
	private ProductModel productModel2;
	private CurrencyModel currency;
	private UnitModel unit;


	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_pricerowimportexportdata.impex", "utf-8");
		final ProductModel productModel1 = productService.getProductForCode("testProduct1");
		item1 = modelService.getSource(productModel1);
		productModel2 = productService.getProductForCode("testProduct2");
		item2 = modelService.getSource(productModel2);
		currency = commonI18NService.getCurrency(USD);
		unit = unitService.getUnitForCode(PIECES);
		translator = new TmaPriceRowTranslator();
		translator.setModelService(modelService);
		translator.setTmaPriceRowExportAdapter(tmaPriceRowExportAdapter);
		translator.setTmaPriceRowImportAdapter(tmaPriceRowImportAdapter);
	}

	@Test
	public void testExport() throws ImpExException
	{
		assertEquals("10 pieces = 300 USD N ", translator.performExport(item1));
	}

	@Test
	public void testImport() throws Exception
	{
		translator.performImport("21 pieces = 10\\,00 " + USD + " N", item2);
		final Collection<PriceRowModel> rows = productModel2.getEurope1Prices();
		assertNotNull(rows);
		assertEquals(1, rows.size());
		final PriceRowModel row = rows.iterator().next();
		assertEquals(currency, row.getCurrency());
		assertEquals(null, row.getDateRange());
		assertEquals(10d, row.getPrice().doubleValue(), 0);
		assertEquals(21l, row.getMinqtd().longValue());
		assertEquals(unit, row.getUnit());
		assertNull(row.getUser());
		assertTrue(row.getNet());
		assertNull(row.getChannel());
	}
}
