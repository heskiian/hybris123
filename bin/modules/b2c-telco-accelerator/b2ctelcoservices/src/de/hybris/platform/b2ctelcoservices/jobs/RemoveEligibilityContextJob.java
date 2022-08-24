/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.jobs;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao.TmaEligibilityContextDao;
import de.hybris.platform.b2ctelcoservices.model.TmaEligibilityContextModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;


/**
 * Cron job to delete created eligibility contexts on demand
 *
 * @since 1907
 */
public class RemoveEligibilityContextJob extends AbstractJobPerformable<CronJobModel>
{
	private TmaEligibilityContextDao eligibilityContextDao;

	@Override
	public PerformResult perform(CronJobModel cronJobModel)
	{
		Set<TmaEligibilityContextModel> contexts = getEligibilityContextDao().getAllEligibilityContexts();
		modelService.removeAll(contexts);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	public TmaEligibilityContextDao getEligibilityContextDao()
	{
		return eligibilityContextDao;
	}

	@Required
	public void setEligibilityContextDao(TmaEligibilityContextDao eligibilityContextDao)
	{
		this.eligibilityContextDao = eligibilityContextDao;
	}
}
