/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.dto.subscription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Enumeration holding value for TmaStatusType
 * 
 * @since 1810
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum TmaStatusTypeWsDto
{

	CREATED("created"),

	PENDINGACTIVE("pendingActive"),

	CANCELLED("cancelled"),

	ACTIVE("active"),

	PENDINGTERMINATE("pendingTerminate"),

	TERMINATED("terminated"),

	SUSPENDED("suspended"),

	ABORTED("aborted");

	private String value;

	TmaStatusTypeWsDto(final String value)
	{
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString()
	{
		return String.valueOf(value);
	}

	@JsonCreator
	public static TmaStatusTypeWsDto fromValue(final String text)
	{
		for (final TmaStatusTypeWsDto b : TmaStatusTypeWsDto.values())
		{
			if (String.valueOf(b.value).equals(text))
			{
				return b;
			}
		}
		return null;
	}
}

