/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.collections.CollectionUtils;


/**
 * Prepare interceptor to remove orphan {@link TmaBundledProdOfferOptionModel}s for a {@link TmaFixedBundledProductOfferingModel}
 *
 * @since 2105
 */
public class TmaBpoPrepareInterceptor implements PrepareInterceptor<TmaBundledProductOfferingModel>
{
	private ModelService modelService;

	public TmaBpoPrepareInterceptor(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public void onPrepare(final TmaBundledProductOfferingModel bundledProductOfferingModel,
			final InterceptorContext interceptorContext)
	{
		if (interceptorContext.isModified(bundledProductOfferingModel, TmaBundledProductOfferingModel.CHILDREN) && CollectionUtils
				.isNotEmpty(bundledProductOfferingModel.getBpoOptions()))
		{
			bundledProductOfferingModel.getBpoOptions().stream().forEach(bundledProdOfferOptionModel -> {
				if (!bundledProductOfferingModel.getChildren().contains(bundledProdOfferOptionModel.getProductOffering()))
				{
					getModelService().remove(bundledProdOfferOptionModel);
				}
			});
		}
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
