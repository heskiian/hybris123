/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.commercefacades.product.ProductVariantOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates the first element of variant matrix corresponding to the selected variant.
 *
 * @since 1810
 */

public class TmaPoVariantSelectedPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends TmaPoVariantMatrixPopulator<SOURCE, TARGET>
{
	private ConfigurablePopulator<TmaPoVariantModel, VariantOptionData, ProductVariantOption> variantOptionDataPopulator;
	private Collection<ProductVariantOption> productVariantOptionList;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData)
	{
		if (productModel instanceof TmaPoVariantModel)
		{
			final VariantMatrixElementData nodeZero = createEmptyNode();
			createNodesForVariant((TmaPoVariantModel) productModel, nodeZero);
			orderTree(nodeZero.getElements());
			productData.setVariantMatrix(nodeZero.getElements());
			productData.getVariantMatrix().forEach(parentNode -> populateNode(parentNode, (TmaPoVariantModel) productModel));
		}
	}

	protected void populateNode(final VariantMatrixElementData parentNode, final TmaPoVariantModel variant)
	{
		if (!parentNode.getIsLeaf())
		{
			getVariantOptionDataPopulator().populate(variant, parentNode.getVariantOption(), getProductVariantOptionList());
			parentNode.getElements().forEach(childNode -> populateNode(childNode, variant));

		}
		else if (variant.getCode().equals(parentNode.getVariantOption().getCode()))
		{
			getVariantOptionDataPopulator().populate(variant, parentNode.getVariantOption(), getProductVariantOptionList());
		}
	}

	protected ConfigurablePopulator<TmaPoVariantModel, VariantOptionData, ProductVariantOption> getVariantOptionDataPopulator()
	{
		return variantOptionDataPopulator;
	}

	@Required
	public void setVariantOptionDataPopulator(
			final ConfigurablePopulator<TmaPoVariantModel, VariantOptionData, ProductVariantOption> variantOptionDataPopulator)
	{
		this.variantOptionDataPopulator = variantOptionDataPopulator;
	}

	protected Collection<ProductVariantOption> getProductVariantOptionList()
	{
		return productVariantOptionList;
	}

	@Required
	public void setProductVariantOptionList(
			final Collection<ProductVariantOption> productVariantOptionList)
	{
		this.productVariantOptionList = productVariantOptionList;
	}
}
