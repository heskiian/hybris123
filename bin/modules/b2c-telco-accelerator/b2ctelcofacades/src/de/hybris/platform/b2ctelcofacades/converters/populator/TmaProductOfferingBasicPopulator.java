/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;



/**
 * Populates {@link ProductData} with basic attributes from {@link TmaProductOfferingModel}.
 *
 * @since 1810
 */
public class TmaProductOfferingBasicPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		ProductBasicPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE productModel, final TARGET productData)
	{
		super.populate(productModel, productData);
		productData.setApprovalStatus(productModel.getApprovalStatus().getCode());
		productData.setModifiedTime(productModel.getModifiedtime());
		productData.setOnlineDate(productModel.getOnlineDate());
		productData.setOfflineDate(productModel.getOfflineDate());
		productData.setItemType(productModel.getItemtype());

		if (productModel instanceof TmaPoVariantModel)
		{
			final TmaPoVariantModel variantProduct = (TmaPoVariantModel) productModel;
			productData.setBaseProduct(variantProduct.getTmaBasePo() != null ? variantProduct.getTmaBasePo().getCode() : null);
			productData.setBaseProductName(variantProduct.getTmaBasePo() != null ? variantProduct.getTmaBasePo().getName() : null);
		}
	}
}
