/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.exception;

/**
 * Thrown when an invalid cart actions is sent to request
 *
 * @since 1902
 */
public class InvalidCartActionException extends RuntimeException
{
	public InvalidCartActionException(String action)
	{
		super("No " + action + " is defined. Must be one of add|delete|modify ");
	}
}
