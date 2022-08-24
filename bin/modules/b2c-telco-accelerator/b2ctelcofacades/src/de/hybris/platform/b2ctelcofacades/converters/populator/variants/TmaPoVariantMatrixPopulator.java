/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates the multi-dimensional variant tree for a given product offering.
 *
 * @since 1810
 */
public class TmaPoVariantMatrixPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends AbstractProductPopulator<SOURCE, TARGET>
{
	private Populator<VariantValueCategoryModel, VariantMatrixElementData> variantMatrixElementPopulator;
	private Comparator<VariantValueCategoryModel> valueCategoryComparator;


	@Override
	public void populate(final SOURCE productModel, final TARGET productData)
	{
		if (!(productModel instanceof TmaSimpleProductOfferingModel))
		{
			return;
		}
		final TmaSimpleProductOfferingModel spoModel = (TmaSimpleProductOfferingModel) productModel;
		final Collection<TmaPoVariantModel> variants = getVariants(spoModel);
		final boolean hasVariants = CollectionUtils.isNotEmpty(variants);
		productData.setMultidimensional(Boolean.valueOf(hasVariants));

		if (hasVariants)
		{
			final VariantMatrixElementData nodeZero = createEmptyNode();
			final TmaPoVariantModel starterVariant = getStarterVariant(spoModel, variants);
			createNodesForVariant(starterVariant, nodeZero);

			variants.stream().filter(v -> v instanceof TmaPoVariantModel).collect(Collectors.toList()).forEach(variant ->
			{
				if (!variant.getCode().equals(spoModel.getCode()))//don't process the first variant again
				{
					createNodesForVariant(variant, nodeZero);
				}
			});

			orderTree(nodeZero.getElements());
			productData.setVariantMatrix(nodeZero.getElements());
		}
	}

	protected Collection<TmaPoVariantModel> getVariants(final TmaSimpleProductOfferingModel poModel)
	{
		return (poModel instanceof TmaPoVariantModel) ? ((TmaPoVariantModel) poModel).getTmaBasePo().getTmaPoVariants()
				: poModel.getTmaPoVariants();
	}

	protected TmaPoVariantModel getStarterVariant(final TmaSimpleProductOfferingModel poModel,
			final Collection<TmaPoVariantModel> variants)
	{
		return (TmaPoVariantModel) (poModel instanceof TmaPoVariantModel ? poModel : variants.toArray()[0]);
	}

	protected VariantMatrixElementData createNode(final VariantMatrixElementData parent,
			final VariantValueCategoryModel valueCategory)
	{
		final VariantMatrixElementData matrixElement = createEmptyNode();
		if (parent != null)
		{
			matrixElement.setIsLeaf(Boolean.TRUE);
			matrixElement.setCode(valueCategory.getCode());
			getVariantMatrixElementPopulator().populate(valueCategory, matrixElement);

			parent.setIsLeaf(Boolean.FALSE);
			parent.getElements().add(matrixElement);
		}
		return matrixElement;
	}

	protected void createNodesForVariant(final TmaPoVariantModel variant, VariantMatrixElementData currentParentNode)
	{
		for (final VariantValueCategoryModel valueCategory : getVariantValuesCategories(variant))
		{
			final VariantMatrixElementData existingNode = getExistingNode(currentParentNode, valueCategory);
			if (existingNode == null)
			{
				final VariantMatrixElementData createdNode = createNode(currentParentNode, valueCategory);
				createdNode.getVariantOption().setCode(variant.getCode());
				createdNode.getVariantOption().setName(variant.getName());
				currentParentNode = createdNode;
			}
			else
			{
				currentParentNode = existingNode;
			}
		}
	}

	protected VariantMatrixElementData createEmptyNode()
	{
		final VariantMatrixElementData matrixElement = new VariantMatrixElementData();
		matrixElement.setElements(new ArrayList<>());
		return matrixElement;
	}

	protected List<VariantValueCategoryModel> getVariantValuesCategories(final TmaPoVariantModel poVariant)
	{
		final List<VariantValueCategoryModel> variantValueCategories = poVariant.getSupercategories().stream()
				.filter(categoryModel -> (categoryModel instanceof VariantValueCategoryModel))
				.map(categoryModel -> (VariantValueCategoryModel) categoryModel)
				.collect(Collectors.toList());
		Collections.sort(variantValueCategories, getValueCategoryComparator());
		return variantValueCategories;
	}

	protected VariantMatrixElementData getExistingNode(final VariantMatrixElementData parent,
			final VariantValueCategoryModel valueCategory)
	{
		for (final VariantMatrixElementData elementData : parent.getElements())
		{
			if (elementData.getCode().equals(valueCategory.getCode()))
			{
				return elementData;
			}
		}
		return null;
	}

	/**
	 * Sort the tree on each level, by tree element sequence. The method is recursive in a way lists will be sorted
	 * bottom-up (list
	 * of leaves will be sorted before of its parents).
	 */
	protected void orderTree(final List<VariantMatrixElementData> elementsList)
	{
		for (final VariantMatrixElementData element : elementsList)
		{
			if (CollectionUtils.isNotEmpty(element.getElements()))
			{
				orderTree(element.getElements());
			}
		}
		elementsList.sort(Comparator.comparingInt(v -> v.getVariantValueCategory().getSequence()));
	}

	protected Comparator<VariantValueCategoryModel> getValueCategoryComparator()
	{
		return valueCategoryComparator;
	}

	@Required
	public void setValueCategoryComparator(final Comparator<VariantValueCategoryModel> valueCategoryComparator)
	{
		this.valueCategoryComparator = valueCategoryComparator;
	}

	protected Populator<VariantValueCategoryModel, VariantMatrixElementData> getVariantMatrixElementPopulator()
	{
		return variantMatrixElementPopulator;
	}

	@Required
	public void setVariantMatrixElementPopulator(
			final Populator<VariantValueCategoryModel, VariantMatrixElementData> variantMatrixElementPopulator)
	{
		this.variantMatrixElementPopulator = variantMatrixElementPopulator;
	}
}
