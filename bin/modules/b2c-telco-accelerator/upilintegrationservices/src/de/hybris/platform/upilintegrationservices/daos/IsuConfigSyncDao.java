/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.daos;

import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.upilintegrationservices.enums.SemanticType;
import de.hybris.platform.upilintegrationservices.model.UpilSemanticsModel;

import java.util.List;


/**
 * Data Access object for operations related to the ISU configurations sync with Hybris.
 *
 * @since 1911
 */
public interface IsuConfigSyncDao
{
	/**
	 * Returns list of {@link UpilSemanticsModel}s based on the catalogVersion, semanticsName1, semanticsName2 and
	 * semanticType.
	 *
	 * @param catalogVersion
	 *           Catalog version
	 * @param semanticsName1
	 *           SemanticsName1 to get the {@link UpilSemanticsModel}
	 * @param semanticsName2
	 *           SemanticsName2 to get the {@link UpilSemanticsModel}
	 * @param semanticType
	 *           code of the {@link SemanticType}
	 * @return {@link List} of {@link UpilSemanticsModel}s
	 */
	List<UpilSemanticsModel> findUpilSemantics(final CatalogVersionModel catalogVersion, final Object semanticsName1,
			final Object semanticsName2, final String semanticType);

	/**
	 * Returns list of {@link TmaProductSpecTypeModel}s based on the code.
	 *
	 * @param referenceType
	 *           the code of the {@link TmaProductSpecTypeModel}
	 * @return {@link List} of {@link TmaProductSpecTypeModel}s
	 */
	List<TmaProductSpecTypeModel> getTmaProductSpecTypeForCode(final String referenceType);
}
