/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.commercefacades.product.ProductVariantOption;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.converters.ConfigurablePopulator;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates each {@link VariantMatrixElementData} from {@link ProductData} with {@link VariantOptionData}.
 * VariantOptionData will be populated based on the options specified in productVariantOptionList.
 *
 * @since 1810
 */
public class TmaPoVariantOptionDataPopulator<SOURCE extends TmaProductOfferingModel, TARGET extends ProductData>
		extends AbstractProductPopulator<SOURCE, TARGET>
{
	private ConfigurablePopulator<TmaPoVariantModel, VariantOptionData, ProductVariantOption> variantOptionDataPopulator;
	private Collection<ProductVariantOption> productVariantOptionList;

	@Override
	public void populate(final SOURCE poModel, final TARGET productData)
	{
		if (poModel instanceof TmaSimpleProductOfferingModel)
		{
			final TmaSimpleProductOfferingModel spoModel = (TmaSimpleProductOfferingModel) poModel;
			getVariants(spoModel).stream().filter(variant -> productData.getVariantMatrix() != null).forEach(variant -> {
				productData.getVariantMatrix().forEach(parentNode -> populateNode(parentNode, variant));
			});
		}
	}

	protected void populateNode(final VariantMatrixElementData parentNode, final TmaPoVariantModel variant)
	{
		if (!parentNode.getIsLeaf())
		{
			parentNode.getElements().forEach(childNode -> populateNode(childNode, variant));
		}
		else if (variant.getCode().equals(parentNode.getVariantOption().getCode()))
		{
			getVariantOptionDataPopulator().populate(variant, parentNode.getVariantOption(), getProductVariantOptionList());
		}
		populateParent(parentNode);
	}

	protected Collection<TmaPoVariantModel> getVariants(final TmaSimpleProductOfferingModel spoModel)
	{
		Collection<TmaPoVariantModel> variants = Collections.emptyList();
		if (CollectionUtils.isNotEmpty(spoModel.getTmaPoVariants()))
		{
			variants = spoModel.getTmaPoVariants();
		}
		else if (spoModel instanceof TmaPoVariantModel)
		{
			variants = ((TmaPoVariantModel) spoModel).getTmaBasePo().getTmaPoVariants();
		}
		return variants;
	}

	protected void populateParent(final VariantMatrixElementData parent)
	{
		if (CollectionUtils.isNotEmpty(parent.getElements()) && parent.getVariantOption() != null)
		{
			final VariantMatrixElementData child = getChildForParentCode(parent);
			parent.getVariantOption().setUrl(child.getVariantOption().getUrl());
			parent.getVariantOption().setCode(child.getVariantOption().getCode());
			parent.getVariantOption().setVariantOptionQualifiers(child.getVariantOption().getVariantOptionQualifiers());
		}
	}

	protected VariantMatrixElementData getChildForParentCode(final VariantMatrixElementData parent)
	{
		for (final VariantMatrixElementData child : parent.getElements())
		{
			if (parent.getVariantOption().getCode().equals(child.getVariantOption().getCode()))
			{
				return child;
			}
		}
		throw new IllegalStateException("One of the child elements must have the same code as the parent.");
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

	public Collection<ProductVariantOption> getProductVariantOptionList()
	{
		return this.productVariantOptionList;
	}

	@Required
	public void setProductVariantOptionList(final Collection<ProductVariantOption> productVariantOptionList)
	{
		this.productVariantOptionList = productVariantOptionList;
	}
}
