/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.product;

import java.util.List;


/**
 * Facade handling product sort operations.
 *
 * @since 1907
 */
public interface TmaProductSortFacade
{
	 /**
	  * Returns the code of the sort option based on the parameter provided.
	  *
	  * @param sortOption
	  * 		list of sort option
	  * @return the sort option code
	  */
	 String decodeSortOption(List<String> sortOption);
}
