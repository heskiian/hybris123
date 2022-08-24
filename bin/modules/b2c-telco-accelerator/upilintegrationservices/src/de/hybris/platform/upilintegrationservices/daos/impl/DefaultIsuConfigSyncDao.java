/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.upilintegrationservices.daos.IsuConfigSyncDao;
import de.hybris.platform.upilintegrationservices.enums.SemanticType;
import de.hybris.platform.upilintegrationservices.model.UpilSemanticsModel;

import java.util.List;


/**
 * Default implementation of the {@link IsuConfigSyncDao}.
 *
 * @since 1911
 */
public class DefaultIsuConfigSyncDao extends AbstractItemDao implements IsuConfigSyncDao
{

	private static final String FIND_UPILSEMANTICS_BY_SEMANTICNAMES_TYPE_CATALOGVERSION_QUERY = "SELECT {" + UpilSemanticsModel.PK
			+ "} FROM {" + UpilSemanticsModel._TYPECODE + "} WHERE {" + UpilSemanticsModel.CATALOGVERSION + "}=?catalogVersion AND {"
			+ UpilSemanticsModel.SEMANTICSNAME1 + "}=?semanticsName1 AND {" + UpilSemanticsModel.SEMANTICSNAME2
			+ "}=?semanticsName2 "
			+ "AND {" + UpilSemanticsModel.SEMANTICTYPE + "}= ({{ SELECT {pk} FROM {" + SemanticType._TYPECODE
			+ "} WHERE {code}=?code}})";

	private static final String CATALOG_VERSION = "catalogVersion";
	private static final String SEMANTICS_NAME1 = "semanticsName1";
	private static final String SEMANTICS_NAME2 = "semanticsName2";
	private static final String CODE = "code";

	@Override
	public List<UpilSemanticsModel> findUpilSemantics(final CatalogVersionModel catalogVersion, final Object semanticsName1,
			final Object semanticsName2, final String semanticType)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_UPILSEMANTICS_BY_SEMANTICNAMES_TYPE_CATALOGVERSION_QUERY);
		query.addQueryParameter(CATALOG_VERSION, catalogVersion);
		query.addQueryParameter(SEMANTICS_NAME1, semanticsName1);
		query.addQueryParameter(SEMANTICS_NAME2, semanticsName2);
		query.addQueryParameter(CODE, semanticType);
		return getFlexibleSearchService().<UpilSemanticsModel> search(query).getResult();
	}

	@Override
	public List<TmaProductSpecTypeModel> getTmaProductSpecTypeForCode(final String referenceType)
	{
		final TmaProductSpecTypeModel productSpecTypeModel = new TmaProductSpecTypeModel();
		productSpecTypeModel.setCode(referenceType);
		return getFlexibleSearchService().getModelsByExample(productSpecTypeModel);
	}
}
