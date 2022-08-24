/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.constants.B2ctelcoservicesConstants;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Prepare interceptor to create {@link TmaBundledProdOfferOptionModel}s with default values for a {@link TmaFixedBundledProductOfferingModel}
 *
 * @since 2105
 */
public class TmaFixedBpoPrepareInterceptor implements PrepareInterceptor<TmaFixedBundledProductOfferingModel>
{
	private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";
	private SessionService sessionService;

	public TmaFixedBpoPrepareInterceptor(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * Create {@link TmaBundledProdOfferOptionModel}s with default values for a {@link TmaFixedBundledProductOfferingModel}
	 *
	 * @param fixedBundledProductOffering
	 * 		the fixed BPO
	 * @param interceptorContext
	 * 		the interceptor context
	 */
	@Override
	public void onPrepare(final TmaFixedBundledProductOfferingModel fixedBundledProductOffering,
			final InterceptorContext interceptorContext)
	{
		final Boolean isSyncActive = this.getSessionService().getCurrentSession().getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE);

		if (CollectionUtils.isEmpty(fixedBundledProductOffering.getChildren()) || Boolean.TRUE.equals(isSyncActive))
		{
			return;
		}

		final Set<TmaProductOfferingModel> childrenPoWithOptions = new HashSet<>();

		if (CollectionUtils.isNotEmpty(fixedBundledProductOffering.getBpoOptions()))
		{
			childrenPoWithOptions.addAll(fixedBundledProductOffering.getBpoOptions().stream()
					.map(TmaBundledProdOfferOptionModel::getProductOffering).collect(Collectors.toSet()));
		}

		final Set<TmaBundledProdOfferOptionModel> bpoOptions = new HashSet<>();

		if (CollectionUtils.isNotEmpty(fixedBundledProductOffering.getBpoOptions()))
		{
			bpoOptions.addAll(fixedBundledProductOffering.getBpoOptions());
		}

		fixedBundledProductOffering.getChildren().stream().filter(childPo -> !childrenPoWithOptions.contains(childPo))
				.forEach(childPo -> bpoOptions.add(createBpoOptionFor(fixedBundledProductOffering, childPo)));

		fixedBundledProductOffering.setBpoOptions(bpoOptions);
	}

	private TmaBundledProdOfferOptionModel createBpoOptionFor(final TmaFixedBundledProductOfferingModel fixedBpo,
			final TmaProductOfferingModel childProductOffering)
	{
		final TmaBundledProdOfferOptionModel bundledProdOfferOption = new TmaBundledProdOfferOptionModel();
		bundledProdOfferOption.setBundledProductOffering(fixedBpo);
		bundledProdOfferOption.setProductOffering(childProductOffering);
		bundledProdOfferOption
				.setLowerLimit(Integer.valueOf(Config.getParameter(B2ctelcoservicesConstants.DEFAULT_BPO_OPTIONS_VALUE)));
		bundledProdOfferOption
				.setUpperLimit(Integer.valueOf(Config.getParameter(B2ctelcoservicesConstants.DEFAULT_BPO_OPTIONS_VALUE)));
		bundledProdOfferOption
				.setDefault(Integer.valueOf(Config.getParameter(B2ctelcoservicesConstants.DEFAULT_BPO_OPTIONS_VALUE)));
		bundledProdOfferOption.setCatalogVersion(fixedBpo.getCatalogVersion());

		return bundledProdOfferOption;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}
}
