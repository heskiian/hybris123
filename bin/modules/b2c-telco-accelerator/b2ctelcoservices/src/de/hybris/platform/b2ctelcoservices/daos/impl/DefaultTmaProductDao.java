/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2ctelcoservices.daos.TmaProductDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaOperationalProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.product.daos.impl.DefaultProductDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;
import java.util.Optional;


/**
 * Default implementation of {@link TmaProductDao}.
 *
 * @since 2003.
 */
public class DefaultTmaProductDao extends DefaultProductDao implements TmaProductDao
{


	private static final String FIND_OPERATIONAL_PRODUCT = "SELECT {p:" + TmaOperationalProductOfferingModel.PK + "} " + "FROM {"
			+ TmaOperationalProductOfferingModel._TYPECODE + " AS p "
			+ "JOIN TmaOperationalPo2TmaSubscribedProductActionRelation AS r ON {p." + TmaOperationalProductOfferingModel.PK
			+ "}={r.source} " + "JOIN " + TmaSubscribedProductAction._TYPECODE + " as a ON {r.target}={a.pk}} " + "WHERE {a.code}=?"
			+ TmaOperationalProductOfferingModel.ACTIONS;

	private static final String FIND_PRODUCT_PATTERN = "SELECT {p:" + TmaProductOfferingModel.PK + "}" + "FROM {"
			+ TmaProductOfferingModel._TYPECODE + " AS p } " + "WHERE {p.property} LIKE  (?pattern)";

	private static final String FIND_PRODUCT_GROUP_PATTERN = "SELECT {p:" + TmaProductOfferingGroupModel.PK + "}" + "FROM {"
			+ TmaProductOfferingGroupModel._TYPECODE + " AS p } " + "WHERE {p.property} LIKE  (?pattern) ";

	protected static final String PRODUCT_OFFERING_QUERY = "SELECT {" + TmaProductOfferingModel.PK + "} FROM {"
			+ TmaProductOfferingModel._TYPECODE + "} WHERE {" + TmaProductOfferingModel.CATALOGVERSION + "} IN (?catalogVersions)";

	public DefaultTmaProductDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public Optional<TmaOperationalProductOfferingModel> getOperationalProductOffering(final TmaSubscribedProductAction action)
	{
		validateParameterNotNull(action, "Action must not be null!");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_OPERATIONAL_PRODUCT);
		query.addQueryParameter(TmaOperationalProductOfferingModel.ACTIONS, action.getCode());
		final SearchResult<TmaOperationalProductOfferingModel> result = getFlexibleSearchService().search(query);
		if (result.getCount() > 0)
		{
			final TmaOperationalProductOfferingModel operationalProduct = result.getResult().get(0);
			return Optional.ofNullable(operationalProduct);
		}
		return Optional.empty();
	}

	@Override
	public List<TmaProductOfferingModel> findProductOfferingsByPattern(final String property, final String propertyValuePattern)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRODUCT_PATTERN.replace("property", property));
		query.addQueryParameter("pattern", "%" + propertyValuePattern + "%");

		return getFlexibleSearchService().<TmaProductOfferingModel> search(query).getResult();
	}

	@Override
	public List<TmaProductOfferingGroupModel> findProductOfferingGroupsByPattern(final String property,
			final String propertyValuePattern)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRODUCT_GROUP_PATTERN.replace("property", property));
		query.addQueryParameter("pattern", "%" + propertyValuePattern + "%");

		return getFlexibleSearchService().<TmaProductOfferingGroupModel> search(query).getResult();
	}

	@Override
	public List<TmaProductOfferingModel> getProductOfferings(final CatalogVersionModel catalogVersion)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(PRODUCT_OFFERING_QUERY);
		query.addQueryParameter("catalogVersions", catalogVersion);

		return getFlexibleSearchService().<TmaProductOfferingModel> search(query).getResult();
	}

}
