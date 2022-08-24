/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductUsageSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.services.TmaProductUsageSpecService;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link TmaProductUsageSpecService}
 *
 * @since 2011
 */
public class DefaultTmaProductUsageSpecService implements TmaProductUsageSpecService
{

	@Override
	public Set<TmaProductUsageSpecificationModel> getProdUsageSpecificationsFor(
			final TmaProductOfferingModel productOffering)
	{
		validateParameterNotNull(productOffering, "The product offering can not be null");
		final Set<TmaProductUsageSpecificationModel> productUsageSpecifications = new HashSet<>();
		if (productOffering instanceof TmaBundledProductOfferingModel)
		{
			flattenBpoUsageSpecification((TmaBundledProductOfferingModel) productOffering, productUsageSpecifications);
		}
		else
		{
			flattenOfferingUsageSpecification(productOffering.getProductSpecification(), productUsageSpecifications);
		}
		return productUsageSpecifications;
	}

	@Override
	public Set<TmaProductUsageSpecificationModel> getProdUsageSpecificationsFor(final
	TmaProductOfferingPriceModel productOfferingPrice)
	{
		validateParameterNotNull(productOfferingPrice, "The product offering price can not be null");
		final Set<TmaProductUsageSpecificationModel> productUsageSpecifications = new HashSet<>();
		flattenPopUsageSpecification(productOfferingPrice, productUsageSpecifications);
		return productUsageSpecifications;
	}

	@Override
	public TmaProductOfferingModel getOfferingWithMinimumProductUsageSpecsSize(final Set<TmaProductOfferingModel> productOfferings)
	{
		TmaProductOfferingModel referenceProduct = productOfferings.stream().iterator().next();
		Integer referenceProdUsageSpecificationsSize =
				getProdUsageSpecificationsFor(referenceProduct).size();
		for (TmaProductOfferingModel productOffering : productOfferings)
		{
			Integer prodUsageSpecificationsSize = getProdUsageSpecificationsFor(productOffering).size();
			if (prodUsageSpecificationsSize < referenceProdUsageSpecificationsSize)
			{
				referenceProdUsageSpecificationsSize = prodUsageSpecificationsSize;
				referenceProduct = productOffering;
			}
		}
		return referenceProduct;
	}

	private void flattenBpoUsageSpecification(final TmaBundledProductOfferingModel bpo,
			final Set<TmaProductUsageSpecificationModel> productUsageSpecifications)
	{
		if (CollectionUtils.isEmpty(bpo.getChildren()))
		{
			return;
		}

		bpo.getChildren().forEach(child ->
		{
			if (child instanceof TmaBundledProductOfferingModel)
			{
				flattenBpoUsageSpecification((TmaBundledProductOfferingModel) child, productUsageSpecifications);
			}
			else
			{
				flattenOfferingUsageSpecification(child.getProductSpecification(), productUsageSpecifications);
			}
		});
	}

	/**
	 * Retrieves all the {@link TmaProductUsageSpecificationModel}s found on a {@link TmaProductSpecificationModel}.
	 * In the case of a {@link TmaCompositeProductSpecificationModel} it will search for
	 * {@link TmaProductUsageSpecificationModel}s on the entire children hierarchy.
	 *
	 * @param productSpecification
	 * 		the product specification
	 * @param productUsageSpecifications
	 * 		the {@link TmaProductUsageSpecificationModel}s found on the offering.
	 */
	private void flattenOfferingUsageSpecification(final TmaProductSpecificationModel productSpecification,
			final Set<TmaProductUsageSpecificationModel> productUsageSpecifications)
	{
		if (ObjectUtils.isEmpty(productSpecification))
		{
			return;
		}

		if (productSpecification instanceof TmaAtomicProductSpecificationModel && !ObjectUtils
				.isEmpty(productSpecification.getProductUsageSpecification()))
		{
			flattenProductUsageSpecification(productSpecification.getProductUsageSpecification(), productUsageSpecifications);
		}

		if (productSpecification instanceof TmaCompositeProductSpecificationModel && CollectionUtils
				.isNotEmpty(((TmaCompositeProductSpecificationModel) productSpecification).getChildren()))
		{
			((TmaCompositeProductSpecificationModel) productSpecification).getChildren()
					.forEach(child -> flattenOfferingUsageSpecification(child, productUsageSpecifications));
		}
	}

	/**
	 * Retrieves a set of all the {@link TmaProductUsageSpecificationModel}s found on a {@link TmaProductOfferingPriceModel}
	 *
	 * @param price
	 * 		the product offering price
	 * @param productUsageSpecifications
	 * 		the {@link TmaProductUsageSpecificationModel}s found on the price
	 */
	private void flattenPopUsageSpecification(final TmaProductOfferingPriceModel price,
			final Set<TmaProductUsageSpecificationModel> productUsageSpecifications)
	{
		if (price instanceof TmaUsageProdOfferPriceChargeModel)
		{
			flattenProductUsageSpecification(((TmaUsageProdOfferPriceChargeModel) price).getProductUsageSpec(),
					productUsageSpecifications);
		}

		if (price instanceof TmaCompositeProdOfferPriceModel && CollectionUtils
				.isNotEmpty(((TmaCompositeProdOfferPriceModel) price).getChildren()))
		{
			((TmaCompositeProdOfferPriceModel) price).getChildren()
					.forEach(child -> flattenPopUsageSpecification(child, productUsageSpecifications));
		}
	}

	/**
	 * Retrieves all the {@link TmaProductUsageSpecificationModel} found.
	 * In the case of a {@link TmaCompositeProductUsageSpecModel} it will flatten the children hierarchy and retrieve them all.
	 *
	 * @param productUsageSpecification
	 * 		the product usage specification.
	 * @param productUsageSpecifications
	 * 		the product usage specifications found.
	 */
	private void flattenProductUsageSpecification(final TmaProductUsageSpecificationModel productUsageSpecification,
			final Set<TmaProductUsageSpecificationModel> productUsageSpecifications)
	{
		if (ObjectUtils.isEmpty(productUsageSpecification))
		{
			return;
		}
		productUsageSpecifications.add(productUsageSpecification);

		if (productUsageSpecification instanceof TmaCompositeProductUsageSpecModel && CollectionUtils
				.isNotEmpty(((TmaCompositeProductUsageSpecModel) productUsageSpecification).getChildren()))
		{
			((TmaCompositeProductUsageSpecModel) productUsageSpecification).getChildren()
					.forEach(child -> flattenProductUsageSpecification(child, productUsageSpecifications));
		}
	}
}
