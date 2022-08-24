/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.pagination.impl;

import de.hybris.platform.b2ctelcofacades.pagination.TmaPaginationFacade;
import de.hybris.platform.util.Config;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;


/**
 * Default implementation of {@link TmaPaginationFacade}
 *
 * @since 1907
 */
public class DefaultTmaPaginationFacade implements TmaPaginationFacade
{
	private static final String REL_SELF = "rel=\"self\", ";
	private static final String REL_FIRST = "rel=\"first\", ";
	private static final String REL_NEXT = "rel=\"next\", ";
	private static final String REL_PREVIOUS = "rel=\"prev\", ";
	private static final String REL_LAST = "rel=\"last\", ";
	private static final String QUESTION_MARK = "?";
	private static final String LOWER = "<";
	private static final String GREATER = ">; ";
	private static final String OFFSET = "offset=";
	private static final String LIMIT = "&limit=";

	@Override
	public Integer getTotalNumberOfPages(final Integer noOfItems, final Integer limit)
	{

		return noOfItems / checkLimit(limit);
	}

	@Override
	public void addPaginationHeadersToResponse(final MultiValueMap<String, String> header,
			final String url, final String queryStringWithoutParams, final Long totalCount, final Integer limit,
			final Integer offset)
	{
		header.add("X-Total-Count", totalCount.toString());
		header.add("Link", createLinks(url, queryStringWithoutParams, totalCount, limit, offset));

	}

	@Override
	public <T> List<T> filterListByOffsetAndLimit(Integer offset, Integer limit, List<T> inputList)
	{
		List<T> filteredList = inputList;

		if (CollectionUtils.isEmpty(filteredList) || (offset != null && offset >= inputList.size()))
		{
			return Collections.emptyList();
		}

		if (offset != null && offset < filteredList.size() && offset >= 0)
		{
			filteredList = filteredList.subList(offset, filteredList.size());
		}
		if (limit != null && limit < filteredList.size() && limit > 0)
		{
			filteredList = filteredList.subList(0, limit);
		}

		return filteredList;
	}

	@Override
	public Integer checkOffset(Integer inputOffset)
	{
		if (inputOffset == null)
		{
			return 0;
		}
		return inputOffset < 0 ? 0 : inputOffset;
	}

	@Override
	public Integer checkLimit(Integer inputLimit)
	{
		if (inputLimit == null)
		{
			return getDefaultLimit();
		}
		if (inputLimit <= 0 || inputLimit > getDefaultLimit())
		{
			return getDefaultLimit();
		}
		return inputLimit;
	}

	private String createLinks(final String url, final String queryStringWithoutParams, final Long totalCount,
			final Integer inputLimit,
			final Integer inputOffset)
	{
		final Integer offset = checkOffset(inputOffset);
		final Integer limit = checkLimit(inputLimit);

		String link = "";
		link += LOWER + url + QUESTION_MARK + queryStringWithoutParams + OFFSET + offset +
				LIMIT + limit + GREATER + REL_SELF;
		link += LOWER + url + QUESTION_MARK + queryStringWithoutParams + OFFSET + 0 + LIMIT
				+ limit + GREATER + REL_FIRST;
		if (offset + limit < totalCount)
		{
			final int nextOffset = offset + limit;
			link += LOWER + url + QUESTION_MARK + queryStringWithoutParams + OFFSET
					+ nextOffset +
					LIMIT + limit + GREATER + REL_NEXT;
		}
		if (offset != 0 && offset - limit < totalCount)
		{
			final int prevOffset = offset - limit < 0 ? 0 : offset - limit;
			link += LOWER + url + QUESTION_MARK + queryStringWithoutParams + OFFSET
					+ prevOffset +
					LIMIT + limit + GREATER + REL_PREVIOUS;
		}
		if (totalCount != 0)
		{
			final Long lastOffset = getLastOffset(offset, limit, totalCount);
			link += LOWER + url + QUESTION_MARK + queryStringWithoutParams + OFFSET
					+ lastOffset +
					LIMIT + limit + GREATER + REL_LAST;
		}
		return link;
	}

	private Long getLastOffset(final Integer offset, final Integer limit, final Long totalCount)
	{
		final Long temporaryTotalCount = totalCount - offset;

		if (offset > totalCount || temporaryTotalCount % limit == 0)
		{
			return limit > totalCount ? 0 : totalCount - limit;
		}
		if (limit < temporaryTotalCount)
		{
			return totalCount - temporaryTotalCount % limit;
		}
		if (offset > totalCount - limit)
		{
			return (long) offset;
		}
		return totalCount - limit;
	}

	private Integer getDefaultLimit()
	{
		return Integer.valueOf(Config.getParameter("b2ctelcofacades.pagination.defaultLimit"));
	}
}
