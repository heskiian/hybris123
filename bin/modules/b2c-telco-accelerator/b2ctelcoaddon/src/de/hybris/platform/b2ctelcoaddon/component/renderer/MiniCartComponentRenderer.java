/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoaddon.component.renderer;

import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;
import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.b2ctelcoaddon.constants.B2ctelcoaddonConstants;
import de.hybris.platform.b2ctelcofacades.order.TmaCartFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;

import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.springframework.beans.factory.annotation.Required;


/**
 * Renderer for mini cart component - it shows only the total items count
 */
public class MiniCartComponentRenderer<C extends MiniCartComponentModel> extends DefaultAddOnCMSComponentRenderer<C>
{
	private static final String COMPONENT = "component";
	private static final String TOTAL_ITEMS = "totalItems";
	private CartFacade cartFacade;

	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		final Map<String, Object> model = super.getVariablesToExpose(pageContext, component);
		final CartData cartData = getCartFacade().getMiniCart();
		model.put(TOTAL_ITEMS, cartData.getTotalUnitCount());
		model.put(COMPONENT, component);
		return model;
	}

	@Override
	protected String getAddonUiExtensionName(final C component)
	{
		return B2ctelcoaddonConstants.EXTENSIONNAME;
	}

	protected TmaCartFacade getCartFacade()
	{
		return (TmaCartFacade) cartFacade;
	}

	@Required
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}
}
