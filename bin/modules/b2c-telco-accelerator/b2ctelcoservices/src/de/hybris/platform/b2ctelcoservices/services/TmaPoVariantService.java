/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.Optional;


/**
 * Service for operations related to the {@link TmaPoVariantModel}.
 *
 * @since 1810
 */
public interface TmaPoVariantService
{
	/**
	 * Returns the variant value category of the given variant for the given variant category code.
	 *
	 * @param variantModel
	 * 		product offering variant
	 * @param variantCategoryCode
	 * 		code of the variant category
	 * @return {@link Optional} of {@link VariantValueCategoryModel}
	 */
	Optional<VariantValueCategoryModel> getVariantValueCategory(final TmaPoVariantModel variantModel,
			final String variantCategoryCode);
}
