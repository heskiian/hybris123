/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.dataimport.batch.price;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * Translator for performing the import and export operation of {@link PriceRowModel} through service layer.
 *
 * @since 1907
*/
public class TmaPriceRowTranslator extends AbstractSpecialValueTranslator
{
	private static final String PRICE_IMPORT_ADAPTOR_BEAN_NAME = "tmaPriceRowImportAdapter";
	private static final String PRICE_EXPORT_ADAPTOR_BEAN_NAME = "tmaPriceRowExportAdapter";
	private static final String MODEL_SERVICE_BEAN_NAME = "modelService";

	private ModelService modelService;
	private TmaPriceRowImportAdapter tmaPriceRowImportAdapter;
	private TmaPriceRowExportAdapter tmaPriceRowExportAdapter;

	@Override
	public void init(final SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		setTmaPriceRowImportAdapter((TmaPriceRowImportAdapter) Registry.getApplicationContext()
				.getBean(PRICE_IMPORT_ADAPTOR_BEAN_NAME));
		setTmaPriceRowExportAdapter((TmaPriceRowExportAdapter) Registry.getApplicationContext()
				.getBean(PRICE_EXPORT_ADAPTOR_BEAN_NAME));
		setModelService((ModelService) Registry.getApplicationContext().getBean(MODEL_SERVICE_BEAN_NAME));
		super.init(columnDescriptor);
		getTmaPriceRowImportAdapter().init(columnDescriptor);
		getTmaPriceRowExportAdapter().init(columnDescriptor);
	}

	@Override
	public void performImport(final String cellValue, final Item processedItem) throws ImpExException
	{
		final ProductModel product = getModelService().get(processedItem);
		if (!ObjectUtils.isEmpty(product))
		{
			final List<PriceRowModel> priceRowListFromImport = getTmaPriceRowImportAdapter().performImport(cellValue, processedItem);
			final Collection<PriceRowModel> existingPriceRowsForProduct = product.getEurope1Prices();
			final List<PriceRowModel> noModificationRequiredPriceList = getNoModificationRequiredPriceList(
					existingPriceRowsForProduct,
					priceRowListFromImport);
			existingPriceRowsForProduct.removeAll(noModificationRequiredPriceList);
			priceRowListFromImport.removeAll(noModificationRequiredPriceList);
			getModelService().removeAll(existingPriceRowsForProduct);
			getModelService().saveAll(priceRowListFromImport);
		}
	}

	@Override
	public String performExport(final Item item) throws ImpExException
	{
		final ProductModel product = getModelService().get(item);
		if (!ObjectUtils.isEmpty(product))
		{
			return getTmaPriceRowExportAdapter().performExport(product.getEurope1Prices());
		}
		return StringUtils.EMPTY;
	}

	private List<PriceRowModel> getNoModificationRequiredPriceList(final Collection<PriceRowModel> existingPriceRowsForProduct,
			final List<PriceRowModel> priceRowListFromImport)
	{
		final List<PriceRowModel> noModificationRequiredPriceList = new ArrayList<>();
		for (final PriceRowModel priceRow : priceRowListFromImport)
		{
			if (CollectionUtils.containsInstance(existingPriceRowsForProduct, priceRow))
			{
				noModificationRequiredPriceList.add(priceRow);
			}
		}
		return noModificationRequiredPriceList;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TmaPriceRowImportAdapter getTmaPriceRowImportAdapter()
	{
		return tmaPriceRowImportAdapter;
	}

	public void setTmaPriceRowImportAdapter(final TmaPriceRowImportAdapter tmaPriceRowImportAdapter)
	{
		this.tmaPriceRowImportAdapter = tmaPriceRowImportAdapter;
	}

	protected TmaPriceRowExportAdapter getTmaPriceRowExportAdapter()
	{
		return tmaPriceRowExportAdapter;
	}

	public void setTmaPriceRowExportAdapter(final TmaPriceRowExportAdapter tmaPriceRowExportAdapter)
	{
		this.tmaPriceRowExportAdapter = tmaPriceRowExportAdapter;
	}
}
