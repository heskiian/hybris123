/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.apache.commons.collections.CollectionUtils;


/**
 * Interceptor to validate the attributes configured for a {@link TmaFixedBundledProductOfferingModel}.
 *
 * @since 2105
 */
public class TmaFixedBpoValidateInterceptor implements ValidateInterceptor<TmaFixedBundledProductOfferingModel>
{
	private TmaPoService poService;

	public TmaFixedBpoValidateInterceptor(final TmaPoService poService)
	{
		this.poService = poService;
	}

	@Override
	public void onValidate(final TmaFixedBundledProductOfferingModel fixedBundledProductOfferingModel,
			final InterceptorContext interceptorContext) throws InterceptorException
	{
		validateChildren(fixedBundledProductOfferingModel);
	}

	/**
	 * Validates that the children are properly set for {@link TmaFixedBundledProductOfferingModel}.
	 *
	 * @param fixedBundledProductOfferingModel
	 * 		The fixed bundled product offering
	 * @throws InterceptorException
	 * 		In case the children are not valid
	 */
	protected void validateChildren(final TmaFixedBundledProductOfferingModel fixedBundledProductOfferingModel)
			throws InterceptorException
	{
		if (CollectionUtils.isEmpty(fixedBundledProductOfferingModel.getChildren()))
		{
			return;
		}

		if (fixedBundledProductOfferingModel.getChildren().stream().anyMatch(
				childProductOffering -> !getPoService().isValidChild(childProductOffering, fixedBundledProductOfferingModel)))
		{
			throw new InterceptorException("Invalid child attached to Fixed Bundle Product Offering. It must be a Simple Product "
					+ "Offering or a Fixed Bundle Product Offering", this);
		}
	}

	protected TmaPoService getPoService()
	{
		return poService;
	}
}
