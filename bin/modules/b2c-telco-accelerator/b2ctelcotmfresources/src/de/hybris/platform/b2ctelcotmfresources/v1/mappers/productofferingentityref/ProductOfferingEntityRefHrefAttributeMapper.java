/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfresources.v1.mappers.productofferingentityref;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcotmfresources.constants.B2ctelcotmfresourcesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.EntityRef;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaProductOfferingModel} and {@link EntityRef}
 *
 * @since 2105
 */
public class ProductOfferingEntityRefHrefAttributeMapper extends TmaAttributeMapper<TmaProductOfferingModel, EntityRef>
{
	private static final String BASE_SITE_ID_PARAMETER = "?baseSiteId=";

	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingModel source, final EntityRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			Collection<BaseStoreModel> baseStores = source.getCatalogVersion().getCatalog().getBaseStores();
			if (CollectionUtils.isNotEmpty(baseStores) && baseStores.stream().findFirst().isPresent())
			{
				final String baseStore = (baseStores.stream().findFirst().get().getUid());
				target.setHref(
						B2ctelcotmfresourcesConstants.PRODUCT_OFFERING_API_URL_V3 + source.getCode() + BASE_SITE_ID_PARAMETER + baseStore);
			}
			else
			{
				target.setHref(B2ctelcotmfresourcesConstants.PRODUCT_OFFERING_API_URL_V3 + source.getCode());
			}
		}
	}
}
