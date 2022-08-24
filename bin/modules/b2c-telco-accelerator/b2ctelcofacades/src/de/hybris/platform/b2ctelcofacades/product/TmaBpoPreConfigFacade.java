/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product;

import de.hybris.platform.b2ctelcofacades.data.TmaBpoPreConfigData;
import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;


/**
 * Facade for operations realted to {@link TmaBpoPreConfigModel}
 * @since6.7
 */
public interface TmaBpoPreConfigFacade
{

	/**
	 * Used to extract {@link TmaBpoPreConfigModel} by code
	 *
	 * @param preConfigCode
	 *           Uniquely identifies {@link TmaBpoPreConfigModel}
	 * @return {@link TmaBpoPreConfigData}
	 */
	TmaBpoPreConfigData getPreConfigBpo(String preConfigCode);

}
