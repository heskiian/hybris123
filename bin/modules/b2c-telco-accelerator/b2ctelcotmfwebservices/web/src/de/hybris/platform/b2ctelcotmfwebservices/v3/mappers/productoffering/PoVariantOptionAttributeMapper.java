/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.VariantOption;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for variant option attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoVariantOptionAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getBaseProduct()))
		{
			return;
		}

		final List<VariantOption> variantOptions = new ArrayList<>();
		processVariantOptions(source.getCode(), source.getVariantMatrix(), variantOptions);
		target.setVariantOption(variantOptions);
	}

	private void processVariantOptions(String code, List<VariantMatrixElementData> elements, List<VariantOption> variantOptions)
	{
		for (VariantMatrixElementData variant : elements)
		{
			if (!StringUtils.equals(code, variant.getVariantOption().getCode()))
			{
				continue;
			}

			final VariantOption variantOption = new VariantOption();

			variantOption.setId(variant.getCode());
			variantOption.setVariantCategory(variant.getParentVariantCategory().getName());
			variantOption.setVariantValue(variant.getVariantValueCategory().getName());
			variantOptions.add(variantOption);

			if (!variant.getIsLeaf())
			{
				processVariantOptions(code, variant.getElements(), variantOptions);
			}
		}
	}
}
