/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.search;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityContextService;
import de.hybris.platform.b2ctelcoservices.model.TmaEligibilityContextModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * @since 1810
 */
public class TmaSolrEligibilityQueryBuilder
{
	private TmaEligibilityContextService tmaEligibilityContextService;

	public String buildQuery()
	{
		// todo - use api not raw text processing
		List<String> queryItems = new ArrayList<>();
		for (TmaEligibilityContextModel contextItem : getTmaEligibilityContextService().extractEligibilityContext())
		{
			String processesQuery = buildQueryForParam(contextItem.getProcessesCodes(), "process_string_mv");
			String termsQuery = buildQueryForParam(contextItem.getTermCodes(), "terms_string_mv");

			List<String> partialQItems = new ArrayList<>();
			updateQuery(processesQuery, partialQItems);
			updateQuery(termsQuery, partialQItems);
			String queryItem = joinQuery(partialQItems, "AND");

			updateQuery(queryItem, queryItems);
		}
		return joinQuery(queryItems, "OR");
	}

	private static String joinQuery(List<String> queryItems, String operator)
	{
		if (!queryItems.isEmpty())
		{
			return "(" + String.join(") " + operator + " (", queryItems) + ")";
		}
		return null;
	}

	private static void updateQuery(String queryItem, List<String> queryItems)
	{
		if (queryItem != null)
		{
			queryItems.add(queryItem);
		}
	}

	private String buildQueryForParam(List<String> values, String paramName)
	{
		String query = null;
		if (values != null && !values.isEmpty())
		{
			String valueString = String.join(" ", values);
			query = paramName + ":(" + valueString + ") OR " + paramName + ": \" \"";
		}
		return query;
	}

	public TmaEligibilityContextService getTmaEligibilityContextService()
	{
		return tmaEligibilityContextService;
	}

	@Required
	public void setTmaEligibilityContextService(
			TmaEligibilityContextService tmaEligibilityContextService)
	{
		this.tmaEligibilityContextService = tmaEligibilityContextService;
	}

}
