/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.data.TmaBpoPreConfigData;
import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.converters.Populator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Populates {@link TmaBpoPreConfigData} from {@link TmaBpoPreConfigModel}
 *
 * @since 6.7
 */
public class TmaBpoPreConfigDataPopulator implements Populator<TmaBpoPreConfigModel, TmaBpoPreConfigData>
{

	@Override
	public void populate(final TmaBpoPreConfigModel source, final TmaBpoPreConfigData target)
	{
		validateParameterNotNullStandardMessage("source", source);
		target.setCode(source.getCode());
		if (source.getRootBpo() != null)
		{
			target.setRootBpoCode(source.getRootBpo().getCode());
		}

		if (CollectionUtils.isNotEmpty(source.getPreConfigSpos()))
		{
			final List<String> spoList = new ArrayList<>();
			source.getPreConfigSpos().forEach(spo -> spoList.add(spo.getCode()));
			target.setSpoList(spoList);
		}

	}

}
