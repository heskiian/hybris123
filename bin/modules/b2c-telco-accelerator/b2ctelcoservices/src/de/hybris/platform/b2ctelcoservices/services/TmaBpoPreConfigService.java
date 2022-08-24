/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;


/**
 * Service handles operations related to {@link TmaBpoPreConfigModel}
 *
 * @since 6.7
 */
public interface TmaBpoPreConfigService
{
	/**
	 * Fetches the {@link TmaBpoPreConfigModel} for the given code
	 *
	 * @param code
	 *           Unique identifier for the {@link TmaBpoPreConfigModel}
	 * @return {@link TmaBpoPreConfigModel}
	 */
	TmaBpoPreConfigModel getBpoPreConfigForCode(final String code);
}
