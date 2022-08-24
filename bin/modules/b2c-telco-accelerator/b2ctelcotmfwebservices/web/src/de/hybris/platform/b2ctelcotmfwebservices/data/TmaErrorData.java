/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.data;

/**
 * Displays error message and status code for TmaRestControllerAdvise
 */
public class TmaErrorData
{

   private String status;
	private String message;

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}
	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(final String message)
	{
		this.message = message;
	}

}