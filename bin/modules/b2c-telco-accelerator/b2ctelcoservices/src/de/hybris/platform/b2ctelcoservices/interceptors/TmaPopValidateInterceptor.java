/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPopService;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;

import java.text.MessageFormat;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;


/**
 * Interceptor to validate that a {@link TmaProductOfferingPriceModel} is valid for all the assigned priceRows with products.
 *
 * @since 2011
 */
public class TmaPopValidateInterceptor implements ValidateInterceptor<TmaProductOfferingPriceModel>
{
	protected static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";

	private SessionService sessionService;
	private TmaPopService tmaPopService;

	public TmaPopValidateInterceptor(final TmaPopService tmaPopService, final SessionService sessionService)
	{
		this.tmaPopService = tmaPopService;
		this.sessionService = sessionService;
	}

	/**
	 * Checks that the {@link TmaProductOfferingPriceModel} is valid for all the product offerings where it is used.
	 *
	 * @param productOfferingPriceModel
	 * 		the product offering price
	 * @param interceptorContext
	 * 		interceptor context
	 * @throws InterceptorException
	 * 		throws exception if a product offering is found that is not compatible with the price.
	 */
	@Override
	public void onValidate(final TmaProductOfferingPriceModel productOfferingPriceModel,
			final InterceptorContext interceptorContext) throws InterceptorException
	{
		final Boolean isSyncActive = this.getSessionService().getCurrentSession().getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE);

		if ((CollectionUtils.isEmpty(productOfferingPriceModel.getPriceRows()) && CollectionUtils
				.isEmpty(productOfferingPriceModel.getCompositeProdOfferPrices())) || BooleanUtils.isTrue(isSyncActive))
		{
			return;
		}

		final Set<PriceRowModel> priceRows = getTmaPopService().getPriceRowsFor(productOfferingPriceModel);

		for (PriceRowModel priceRow : priceRows)
		{
			if (!getTmaPopService()
					.popQualifiesFor(productOfferingPriceModel, priceRow.getAffectedProductOffering() != null ?
							priceRow.getAffectedProductOffering() : (TmaProductOfferingModel) priceRow.getProduct()))
			{
				throw new InterceptorException(
						MessageFormat.format("Product Offering Price: {0} is now incompatible with Product: {1}",
								productOfferingPriceModel.getId(), priceRow.getProduct().getCode()), this);
			}
		}
	}

	protected TmaPopService getTmaPopService()
	{
		return tmaPopService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}
}
