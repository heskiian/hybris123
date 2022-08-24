/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.services;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;


/**
 * Service responsible for the creation of the {@link TmaPriceContext}.
 *
 * @since 2007
 */
public interface TmaPriceContextService
{
	/**
	 * Creates a {@link TmaPriceContext} based on the attributes values set on the CommerceCartParameter. If a bundle
	 * product offering exists on the CommerceCartParameter then a bundle product offering price context is created,
	 * otherwise a simple price context is created
	 *
	 * @param parameter
	 * 		CommerceCartParameter for which the context is created
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 */
	TmaPriceContext createPriceContext(CommerceCartParameter parameter);

	/**
	 * Creates a {@link TmaPriceContext} based on the attributes values set on the order entry. If a bundle product
	 * offering exists on the order entry then a bundle product offering price context is created, otherwise a simple
	 * price context is created. For components part of a Fixed Bundled Product Offering, a simple price context is created.
	 *
	 * @param entry
	 * 		order entry for which the context is created
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 */
	TmaPriceContext createPriceContext(final AbstractOrderEntryModel entry);

	/**
	 * Creates a {@link TmaPriceContext} based on the attributes values set on the order entry. If a bundle product
	 * offering exists on the order entry then a bundle product offering price context is created, otherwise a simple
	 * price context is created
	 *
	 * @param entry
	 * 		order entry for which the context is created
	 * @param bundledProductOffering
	 * 		bundled product offering used for context creation
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 * @deprecated since 2105. Use instead {@link TmaPriceContextService#createPriceContext(AbstractOrderEntryModel)}
	 */
	@Deprecated(since = "2105")
	TmaPriceContext createPriceContext(final AbstractOrderEntryModel entry,
			final TmaBundledProductOfferingModel bundledProductOffering);
}
