/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfresources.v1.mappers.productofferingpriceref;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcotmfresources.constants.B2ctelcotmfresourcesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOfferingPriceRef;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaProductOfferingPriceModel} and {@link ProductOfferingPriceRef}
 *
 * @since 2105
 */
public class PopRefHrefAttributeMapper extends TmaAttributeMapper<TmaProductOfferingPriceModel, ProductOfferingPriceRef>
{
	private static final String BASE_SITE_ID_PARAMETER = "?baseSiteId=";

	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceModel source, final ProductOfferingPriceRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			final Collection<BaseStoreModel> baseStores = source.getCatalogVersion().getCatalog().getBaseStores();
			if (CollectionUtils.isNotEmpty(baseStores) && baseStores.stream().findFirst().isPresent())
			{
				final String baseStore = (baseStores.stream().findFirst().get().getUid());
				target.setHref(
						B2ctelcotmfresourcesConstants.PRODUCT_OFFERING_PRICE_API_URL_V3 + source.getId() + BASE_SITE_ID_PARAMETER
								+ baseStore);
			}
			else
			{
				target.setHref(B2ctelcotmfresourcesConstants.PRODUCT_OFFERING_PRICE_API_URL_V3 + source.getId());
			}
		}
	}
}
