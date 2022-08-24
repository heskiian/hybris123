/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */


package de.hybris.platform.upilintegrationservices.odata.feed.edmx.exception;

/**
 * Class for Run Time custom exception EdmxAssociationEndCreationException
 * 
 * @since 1911
 */
public class UpilEdmxAssociationEndCreationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public UpilEdmxAssociationEndCreationException()
	{
		super();
	}

	public UpilEdmxAssociationEndCreationException(final String msg)
	{
		super(msg);
	}

	public UpilEdmxAssociationEndCreationException(final String msg, final Throwable e)
	{
		super(msg, e);
	}

	public UpilEdmxAssociationEndCreationException(final Throwable e)
	{
		super(e);
	}

}
