/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaOperationalProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;

import java.util.List;
import java.util.Optional;


/**
 * Data access object for operations related to the {@link ProductModel}.
 *
 * @since 2003
 */
public interface TmaProductDao extends ProductDao
{
	/**
	 * Returns the operational product corresponding to the specified action.
	 *
	 * @param action
	 *           the action that should be supported by the operational product.
	 * @return The first operational product found that supports the given action; Optional.empty() otherwise.
	 */
	Optional<TmaOperationalProductOfferingModel> getOperationalProductOffering(TmaSubscribedProductAction action);


	/**
	 * Returns product offering list for corresponding pattern and property
	 *
	 * @param property
	 *           on the attribute which the pattern is compared
	 * @param propertyValuePattern
	 *           string to get the list of {@link TmaProductOfferingModel}
	 * @return List of {@link TmaProductOfferingModel}
	 */
	List<TmaProductOfferingModel> findProductOfferingsByPattern(String property, String propertyValuePattern);


	/**
	 * Returns product group list for corresponding pattern and property
	 *
	 * @param property
	 *           on the attribute which the pattern is compared
	 * @param propertyValuePattern
	 *           string to get the list of {@link TmaProductOfferingGroupModel}
	 * @return List of {@link TmaProductOfferingGroupModel}
	 */
	List<TmaProductOfferingGroupModel> findProductOfferingGroupsByPattern(String property, String propertyValuePattern);


	/**
	 * Returns product offering list for corresponding catalog version
	 *
	 * @param catalogVersion
	 *           for which product offerings will be retrieved
	 * @return List of {@link TmaProductOfferingModel}
	 */
	List<TmaProductOfferingModel> getProductOfferings(CatalogVersionModel catalogVersion);
}
