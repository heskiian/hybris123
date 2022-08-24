/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.upilintegrationservices.model.IsuConfigSyncCronJobModel;
import de.hybris.platform.upilintegrationservices.service.UpilintegrationservicesService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * Job class to sync the Utilities configurations with Hybris.
 *
 * @since 1911
 */
public class UtilitiesConfigSyncJob extends AbstractJobPerformable<IsuConfigSyncCronJobModel>
{
	private UpilintegrationservicesService upilintegrationservicesService;

	@Override
	public PerformResult perform(final IsuConfigSyncCronJobModel model)
	{
		return getUpilintegrationservicesService().syncIsuConfigWithTua(model.getAppliedCatalogVersion())
				? new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED)
				: new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
	}

	protected UpilintegrationservicesService getUpilintegrationservicesService()
	{
		return upilintegrationservicesService;
	}

	@Autowired
	public void setUpilintegrationservicesService(final UpilintegrationservicesService upilintegrationservicesService)
	{
		this.upilintegrationservicesService = upilintegrationservicesService;
	}
}
