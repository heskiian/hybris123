/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.springframework.util.CollectionUtils;


/**
 * Interceptor to validate the attributes configured for a {@link TmaBundledProdOfferOptionModel}.
 *
 * @since 2011
 */
public class TmaBundledProdOfferOptionValidateInterceptor implements ValidateInterceptor<TmaBundledProdOfferOptionModel>
{
	@Override
	public void onValidate(final TmaBundledProdOfferOptionModel tmaBundledProdOfferOptionModel,
			final InterceptorContext interceptorContext) throws InterceptorException
	{
		validateProductOffering(tmaBundledProdOfferOptionModel);
		validateLimits(tmaBundledProdOfferOptionModel);
		validateDefault(tmaBundledProdOfferOptionModel);
	}

	/**
	 * Validates the Product Offering in the {@link TmaBundledProdOfferOptionModel}.
	 *
	 * @param tmaBundledProdOfferOptionModel
	 * 		The bundled product offering option
	 * @throws InterceptorException
	 * 		In case the product offering is not valid
	 */
	protected void validateProductOffering(TmaBundledProdOfferOptionModel tmaBundledProdOfferOptionModel)
			throws InterceptorException
	{
		if (CollectionUtils.isEmpty(tmaBundledProdOfferOptionModel.getBundledProductOffering().getChildren()) ||
				!tmaBundledProdOfferOptionModel.getBundledProductOffering().getChildren()
						.contains(tmaBundledProdOfferOptionModel.getProductOffering()))
		{
			throw new InterceptorException(
					"The PO for which a BundledProdOfferOption has been defined is not part of the configured BPO", this);
		}
	}

	/**
	 * Validates the lower, upper and default limits in the {@link TmaBundledProdOfferOptionModel}.
	 *
	 * @param tmaBundledProdOfferOptionModel
	 * 		The bundled product offering option
	 * @throws InterceptorException
	 * 		In case the lower, upper and default limit are not valid
	 */
	protected void validateLimits(TmaBundledProdOfferOptionModel tmaBundledProdOfferOptionModel) throws InterceptorException
	{
		if (isLessThanZero(tmaBundledProdOfferOptionModel.getLowerLimit()) || isLessThanZero(
				tmaBundledProdOfferOptionModel.getUpperLimit()))
		{
			throw new InterceptorException("Lower and upper cardinality must be higher or equal to 0", this);
		}

		if (TmaFixedBundledProductOfferingModel._TYPECODE
				.equals(tmaBundledProdOfferOptionModel.getBundledProductOffering().getItemtype()) && !checkForSameLimits(
				tmaBundledProdOfferOptionModel))
		{
			throw new InterceptorException("Lower, upper and default cardinality for a Fixed Bundled Product Offering must be equal",
					this);
		}

		if (TmaFixedBundledProductOfferingModel._TYPECODE
				.equals(tmaBundledProdOfferOptionModel.getBundledProductOffering().getItemtype()) && !checkLimitsAreGreaterThanZero(
				tmaBundledProdOfferOptionModel))
		{
			throw new InterceptorException("Lower, upper and default cardinality for a Fixed Bundled Product Offering must be "
					+ "greater then 0", this);
		}

		if (tmaBundledProdOfferOptionModel.getUpperLimit() != null
				&& tmaBundledProdOfferOptionModel.getLowerLimit() > tmaBundledProdOfferOptionModel.getUpperLimit())
		{
			throw new InterceptorException("Lower cardinality must be less or equal to upper cardinality", this);
		}
	}

	/**
	 * Validates the default value in the {@link TmaBundledProdOfferOptionModel}.
	 *
	 * @param tmaBundledProdOfferOptionModel
	 * 		The bundled product offering option
	 * @throws InterceptorException
	 * 		In case the default value is not valid
	 */
	protected void validateDefault(TmaBundledProdOfferOptionModel tmaBundledProdOfferOptionModel) throws InterceptorException
	{
		if (isLessThanZero(tmaBundledProdOfferOptionModel.getDefault()))
		{
			throw new InterceptorException("Default cardinality must be higher or equal to 0", this);
		}

		if (tmaBundledProdOfferOptionModel.getDefault() < tmaBundledProdOfferOptionModel.getLowerLimit())
		{
			throw new InterceptorException("Default cardinality must be higher or equal to lower cardinality", this);
		}

		if (tmaBundledProdOfferOptionModel.getUpperLimit() != null
				&& tmaBundledProdOfferOptionModel.getDefault() > tmaBundledProdOfferOptionModel.getUpperLimit())
		{
			throw new InterceptorException("Default cardinality must be lower or equal to upper cardinality", this);
		}
	}

	private boolean isLessThanZero(Integer value)
	{
		return value != null && value < 0;
	}

	private boolean checkForSameLimits(final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel)
	{
		return bundledProdOfferOptionModel.getDefault().equals(bundledProdOfferOptionModel.getLowerLimit())
				&& bundledProdOfferOptionModel.getDefault().equals(bundledProdOfferOptionModel.getUpperLimit());
	}

	private boolean checkLimitsAreGreaterThanZero(final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel)
	{
		return bundledProdOfferOptionModel.getLowerLimit() > 0 && bundledProdOfferOptionModel.getDefault() > 0 &&
				bundledProdOfferOptionModel.getUpperLimit() > 0;
	}
}
