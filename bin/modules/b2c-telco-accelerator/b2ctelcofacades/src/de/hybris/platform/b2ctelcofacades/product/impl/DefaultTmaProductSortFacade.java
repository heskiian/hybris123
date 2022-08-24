/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.platform.b2ctelcofacades.product.TmaProductSortFacade;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaProductSortFacade}.
 *
 * @since 1907
 */
public class DefaultTmaProductSortFacade implements TmaProductSortFacade
{
	 /**
	  * Regexp for checking if sort option value is correct
	  */
	 private static final String SORT_OPTION_REGEXP = "^[+-]?.*$";

	 /**
	  * Sort option map
	  */
	 private Map<String, String> sortOptionMap;

	 @Override
	 public String decodeSortOption(final List<String> sortOption)
	 {
		  final String firstSortOption = sortOption.get(0);

		  if (!firstSortOption.matches(SORT_OPTION_REGEXP))
		  {
				throw new IllegalArgumentException("Invalid sort option: '" + firstSortOption + "'.");
		  }

		  if (!getSortOptionMap().containsKey(firstSortOption))
		  {
				throw new IllegalArgumentException("Sort option '" + firstSortOption + "' not supported.");
		  }

		  return getSortOptionMap().get(firstSortOption);
	 }

	 protected Map<String, String> getSortOptionMap()
	 {
		  return sortOptionMap;
	 }

	 @Required
	 public void setSortOptionMap(Map<String, String> sortOptionMap)
	 {
		  this.sortOptionMap = sortOptionMap;
	 }
}
