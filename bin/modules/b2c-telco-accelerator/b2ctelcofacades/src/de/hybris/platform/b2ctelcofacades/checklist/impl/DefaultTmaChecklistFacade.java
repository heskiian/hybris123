/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.checklist.impl;

import de.hybris.platform.b2ctelcofacades.checklist.TmaChecklistFacade;
import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionData;
import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionParamData;
import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistService;
import de.hybris.platform.b2ctelcoservices.checklist.context.TmaChecklistContext;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaChecklistFacade}
 *
 * @since 1907
 */
public class DefaultTmaChecklistFacade implements TmaChecklistFacade
{
	private Converter<TmaChecklistActionParamData, TmaChecklistContext> checklistActionParamDataReverseConverter;
	private TmaChecklistService tmaChecklistService;
	private Converter<RuleEvaluationResult, TmaChecklistActionData> checklistActionDataConverter;

	public Set<TmaChecklistActionData> findActions(final TmaChecklistActionParamData tmaChecklistActionParamData)
	{
		final TmaChecklistContext tmaChecklistContext = getChecklistActionParamDataReverseConverter()
				.convert(tmaChecklistActionParamData);

		final Set<TmaChecklistActionData> checklistActionDatas = new HashSet<>();

		final Set<RuleEvaluationResult> ruleEvaluationResults = getTmaChecklistService().findActions(tmaChecklistContext);
		ruleEvaluationResults.forEach(
				ruleEvaluationResult -> checklistActionDatas.add(getChecklistActionDataConverter().convert(ruleEvaluationResult)));

		return checklistActionDatas;
	}

	protected Converter<TmaChecklistActionParamData, TmaChecklistContext> getChecklistActionParamDataReverseConverter()
	{
		return checklistActionParamDataReverseConverter;
	}

	@Required
	public void setChecklistActionParamDataReverseConverter(
			Converter<TmaChecklistActionParamData, TmaChecklistContext> checklistActionParamDataReverseConverter)
	{
		this.checklistActionParamDataReverseConverter = checklistActionParamDataReverseConverter;
	}

	protected TmaChecklistService getTmaChecklistService()
	{
		return tmaChecklistService;
	}

	@Required
	public void setTmaChecklistService(TmaChecklistService tmaChecklistService)
	{
		this.tmaChecklistService = tmaChecklistService;
	}

	protected Converter<RuleEvaluationResult, TmaChecklistActionData> getChecklistActionDataConverter()
	{
		return checklistActionDataConverter;
	}

	@Required
	public void setChecklistActionDataConverter(
			Converter<RuleEvaluationResult, TmaChecklistActionData> checklistActionDataConverter)
	{
		this.checklistActionDataConverter = checklistActionDataConverter;
	}
}
