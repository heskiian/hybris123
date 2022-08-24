/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.provider;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.provider.impl.ItemIdentityProvider;

import java.util.Iterator;
import java.util.Set;


/**
 * Uses Pk instead of UniqueAttributes -> as the set of attributes defined as unique on priceRow will not uniquely identify a price.
 * Also in case the attribute value is null the {@link ItemIdentityProvider} throws a NPE.
 *
 * @since 1810
 */
public class TmaItemIdentityProvider extends ItemIdentityProvider
{
	private static final String SEPARATOR = "/";

	protected String prepareCatalogAwareItemIdentifier(ItemModel item)
	{
		String catalogContainerAttr = getCatalogTypeService().getCatalogVersionContainerAttribute(item.getItemtype());
		CatalogVersionModel catalogVersion = getModelService().getAttributeValue(item, catalogContainerAttr);

		return catalogVersion.getCatalog().getId() + SEPARATOR +
				catalogVersion.getVersion() + SEPARATOR +
				item.getItemtype() + SEPARATOR +
				item.getPk();
	}
}
