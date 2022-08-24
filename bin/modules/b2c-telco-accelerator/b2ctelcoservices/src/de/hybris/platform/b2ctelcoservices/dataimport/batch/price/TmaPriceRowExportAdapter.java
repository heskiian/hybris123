/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.dataimport.batch.price;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;


/**
 * Adapter to translate a PriceRow export.
 *
 * @since 1907
 */
public interface TmaPriceRowExportAdapter
{
	/**
	 * Initialize the values
	 * for locale, collectionValueDelimiter, dateFormat, numberFormat etc.
	 *
	 * @param columnDescriptor
	 * 		containing the values of above attributes given in impex
	 */
	void init(SpecialColumnDescriptor columnDescriptor);

	/**
	 * Export {@link PriceRowModel} for the product.
	 *
	 * @param value
	 * 		list of price row for the product
	 */
	public String performExport(Object value);
}
