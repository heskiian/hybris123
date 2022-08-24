/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoaddon.component.renderer;

import de.hybris.platform.acceleratorcms.model.actions.AddToCartActionModel;
import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.b2ctelcoaddon.constants.B2ctelcoaddonConstants;

import java.util.Collections;
import java.util.Map;

import javax.servlet.jsp.PageContext;



/**
 * Renderer for the {@link AddToCartActionModel} .
 *
 * @since 6.7
 */

public class AddToCartActionRenderer<C extends AddToCartActionModel>
		extends DefaultAddOnCMSComponentRenderer<C>
{
	private static final String COMPONENT = "component";

	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		return Collections.singletonMap(COMPONENT, component);
	}

	@Override
	protected String getAddonUiExtensionName(final C component)
	{
		return B2ctelcoaddonConstants.EXTENSIONNAME;
	}
}
