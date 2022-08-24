/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Interceptor to validate the attributes configured for {@link TmaProductSpecCharValueUseModel}.
 *
 * @since 2105
 */
public class TmaProductSpecCharValueUseValidateInterceptor implements ValidateInterceptor<TmaProductSpecCharValueUseModel>
{
	@Override
	public void onValidate(final TmaProductSpecCharValueUseModel productSpecCharValueUse,
			final InterceptorContext interceptorContext) throws InterceptorException
	{
		validateMinCardinality(productSpecCharValueUse);
		validateMaxCardinality(productSpecCharValueUse);
		validatePscvs(productSpecCharValueUse);
	}

	/**
	 * Validates the min cardinality in the {@link TmaProductSpecCharValueUseModel}.
	 *
	 * @param productSpecCharValueUse
	 * 		The product specification characteristic value use
	 * @throws InterceptorException
	 * 		In case the min cardinality is not valid
	 */
	protected void validateMinCardinality(final TmaProductSpecCharValueUseModel productSpecCharValueUse)
			throws InterceptorException
	{
		if (productSpecCharValueUse.getMinCardinality() ==  null)
		{
			return;
		}

		if (productSpecCharValueUse.getMinCardinality() < 0)
		{
			throw new InterceptorException("Minimum cardinality must be equal or higher to 0.");
		}
	}

	/**
	 * Validates the max cardinality in the {@link TmaProductSpecCharValueUseModel}.
	 *
	 * @param productSpecCharValueUse
	 * 		The product specification characteristic value use
	 * @throws InterceptorException
	 * 		In case the max cardinality is not valid
	 */
	protected void validateMaxCardinality(final TmaProductSpecCharValueUseModel productSpecCharValueUse)
			throws InterceptorException
	{
		if (productSpecCharValueUse.getMaxCardinality() == null)
		{
			return;
		}

		if (productSpecCharValueUse.getMaxCardinality() < 0)
		{
			throw new InterceptorException("Minimum cardinality must be equal or higher to 0.");
		}

		if (productSpecCharValueUse.getMaxCardinality() < productSpecCharValueUse.getMinCardinality())
		{
			throw new InterceptorException("Maximum cardinality must be equal or higher than minimum cardinality.");
		}
	}

	/**
	 * Validates the PSCVs in the {@link TmaProductSpecCharValueUseModel}.
	 *
	 * @param productSpecCharValueUse
	 * 		The product specification characteristic value use
	 * @throws InterceptorException
	 * 		In case the PSCVs are not valid
	 */
	protected void validatePscvs(final TmaProductSpecCharValueUseModel productSpecCharValueUse)
			throws InterceptorException
	{
		if (CollectionUtils.isEmpty(productSpecCharValueUse.getProductSpecCharacteristicValues()))
		{
			return;
		}

		final Set<TmaProductSpecCharacteristicModel> productSpecCharacteristics = productSpecCharValueUse
				.getProductSpecCharacteristicValues().stream()
				.map(TmaProductSpecCharacteristicValueModel::getProductSpecCharacteristic).collect(Collectors.toSet());

		if (CollectionUtils.isNotEmpty(productSpecCharacteristics) && productSpecCharacteristics.size() > 1)
		{
			throw new InterceptorException("PSCVs assigned to a PSCVU must be part of the same PSC.");
		}
	}
}
