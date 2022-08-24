/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
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

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.ObjectUtils;


/**
 * Interceptor that validates {@link PriceRowModel} for a {@link TmaProductOfferingModel}.
 *
 * @since 2011
 */
public class TmaPriceRowPopValidateInterceptor implements ValidateInterceptor<PriceRowModel>
{
	protected static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";

	private SessionService sessionService;
	private TmaPopService tmaPopService;

	public TmaPriceRowPopValidateInterceptor(final TmaPopService tmaPopService, final SessionService sessionService)
	{
		this.tmaPopService = tmaPopService;
		this.sessionService = sessionService;
	}

	/**
	 * Validates that the {@link PriceRowModel} is still valid for the {@link TmaProductOfferingModel} used, based on the
	 * {@link TmaProductOfferingPriceModel} of the price row.
	 *
	 * @param priceRow
	 * 		the price row.
	 * @param interceptorContext
	 * 		the interceptor context
	 * @throws InterceptorException
	 * 		throws exception if the price row is no longer valid for the offering based on the product
	 * 		offering price.
	 */
	@Override
	public void onValidate(final PriceRowModel priceRow, final InterceptorContext interceptorContext) throws InterceptorException
	{
		final Boolean isSyncActive = this.getSessionService().getCurrentSession().getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE);

		if (!(priceRow.getProduct() instanceof TmaProductOfferingModel) || BooleanUtils.isTrue(isSyncActive))
		{
			return;
		}

		final TmaProductOfferingModel productOffering = priceRow.getAffectedProductOffering() != null ?
				priceRow.getAffectedProductOffering() : (TmaProductOfferingModel) priceRow.getProduct();
		if (ObjectUtils.isEmpty(productOffering) || ObjectUtils.isEmpty(priceRow.getProductOfferingPrice()))
		{
			return;
		}
		if (!getTmaPopService().popQualifiesFor(priceRow.getProductOfferingPrice(), productOffering))
		{
			throw new InterceptorException(MessageFormat.format("Product Offering Price is not valid for Product Offering : {0} ",
					productOffering.getCode()), this);
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

