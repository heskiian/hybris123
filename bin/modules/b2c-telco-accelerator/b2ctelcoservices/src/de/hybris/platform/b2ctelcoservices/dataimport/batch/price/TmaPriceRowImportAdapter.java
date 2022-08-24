/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.dataimport.batch.price;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.jalo.Item;

import java.util.List;


/**
 * Adapter to translate a PriceRow import.
 *
 * @since 1907
 */
public interface TmaPriceRowImportAdapter
{

	/**
	 * Initialize the values
	 * for locale, globalAdd, globalRemove, collectionValueDelimiter, dateFormat, numberFormat etc.
	 *
	 * @param columnDescriptor
	 * 		containing the values of above attributes given in impex
	 */
	void init(SpecialColumnDescriptor columnDescriptor);

	/**
	 * Import a {@link PriceRowModel}.
	 *
	 * @param cellValue
	 * 		the value from impex for price attribute
	 * @param processedItem
	 * 		the item from impex
	 */
	List<PriceRowModel> performImport(String cellValue, Item processedItem);
}
