/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.servicelayer.session;

import de.hybris.platform.servicelayer.session.impl.DefaultSession;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MockSession extends DefaultSession
{
	private long sessionIdCounter = 1;

	private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	private final String sessionId;

	@Override
	public String getSessionId()
	{
		return this.sessionId;
	}

	public MockSession()
	{
		super();
		this.sessionId = String.valueOf(sessionIdCounter++);
	}

	@Override
	public <T> Map<String, T> getAllAttributes()
	{

		return (Map<String, T>) Collections.unmodifiableMap(attributes);
	}

	@Override
	public <T> T getAttribute(final String name)
	{

		return (T) attributes.get(name);
	}


	@Override
	public void setAttribute(final String name, final Object value)
	{
		attributes.put(name, value);
	}

}
