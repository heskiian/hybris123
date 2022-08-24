/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Attachment;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.VariantOption;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;


/**
 * This attribute Mapper class maps data for PO variant attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 1903
 */
public class PoVariantPoAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{

	private static final String THUMBNAIL_IMAGE = "thumbnail";

	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (source.getVariantMatrix() == null || StringUtils.isNotEmpty(source.getBaseProduct()))
		{
			return;
		}

		final Map<String, ProductOfferingRef> variantReferences = new HashMap<>();
		mapVariantDetails(source.getVariantMatrix(), variantReferences, new ArrayList<>(), context);

		target.setVariantProductOffering(new ArrayList<>(variantReferences.values()));
	}

	private void mapVariantDetails(final List<VariantMatrixElementData> variantElements, final Map<String, ProductOfferingRef> offeringRefs,
			List<VariantOption> options, final MappingContext context)
	{
		for (VariantMatrixElementData variant : variantElements)
		{
			String variantCode = variant.getVariantOption() != null ? variant.getVariantOption().getCode() : Strings.EMPTY;
			if (StringUtils.isEmpty(variantCode))
			{
				break;
			}

			if (!offeringRefs.containsKey(variantCode))
			{
				final ProductOfferingRef productOfferingRef = getProductOfferingRef(options, variant, variantCode, variant.getVariantOption().getName(), context);
				offeringRefs.put(variantCode, productOfferingRef);
			}
			else
			{
				ProductOfferingRef variantReference = offeringRefs.get(variantCode);

				final VariantOption variantOption = new VariantOption();
				variantOption.setVariantCategory(variant.getParentVariantCategory().getName());
				variantOption.setVariantValue(variant.getVariantValueCategory().getName());
				Set<VariantOption> existingOptions = new HashSet<>(variantReference.getVariantOption());
				options.add(variantOption);
				existingOptions.addAll(options);
				variantReference.setVariantOption(new ArrayList<>(existingOptions));
				offeringRefs.put(variantCode, variantReference);
			}

			if (!variant.getIsLeaf())
			{
				mapVariantDetails(variant.getElements(), offeringRefs, options, context);
				options = new ArrayList<>();
			}
		}
	}

	private ProductOfferingRef getProductOfferingRef(List<VariantOption> options,
			final VariantMatrixElementData variant, final String variantCode, final String variantName, final MappingContext context)
	{
		final ProductOfferingRef productOfferingRef = new ProductOfferingRef();
		productOfferingRef.setId(variantCode);
		productOfferingRef.setName(variantName);
		productOfferingRef.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_API_URL + variantCode);

		Attachment image = variant.getVariantOption().getVariantOptionQualifiers().stream()
				.map(VariantOptionQualifierData::getImage).filter(imageData -> imageData.getFormat().equals(THUMBNAIL_IMAGE))
				.findFirst().map(imageData -> getMapperFacade().map(imageData, Attachment.class, context)).orElse(new Attachment());
		productOfferingRef.setImage(image);

		final VariantOption variantOption = new VariantOption();
		variantOption.setVariantCategory(variant.getParentVariantCategory().getName());
		variantOption.setVariantValue(variant.getVariantValueCategory().getName());
		List<VariantOption> variantOptions = new ArrayList<>();

		if (variant.getIsLeaf())
		{
			variantOptions.addAll(options);
		}
		else
		{
			options.add(variantOption);
		}

		variantOptions.add(variantOption);
		productOfferingRef.setVariantOption(variantOptions);

		return productOfferingRef;
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
