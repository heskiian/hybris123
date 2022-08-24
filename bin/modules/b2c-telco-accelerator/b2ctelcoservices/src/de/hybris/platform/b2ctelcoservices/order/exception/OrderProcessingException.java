/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.exception;

import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * Business exception marking a problem in creating an order entity
 *
 * @since 1907
 */
public class OrderProcessingException extends BusinessException
{
	/**
	 * Enum storing order processing error codes
	 *
	 * @since 1907
	 */
	public enum OrderProcessingErrorCode
	{
		GENERIC_ORDER_PROCESSING_ERROR (0, "Order cannot be processed"),
		ORDER_CREATION_ERROR (1, "Order cannot be created");

		private int errorCode;

		private String description;

		OrderProcessingErrorCode(final int errorCode, final String description)
		{
			this.errorCode = errorCode;
			this.description = description;
		}

		public OrderProcessingErrorCode getOrderProcessingErrorCodeById(int errorCode) {
			for (OrderProcessingErrorCode orderProcessingErrorCode : values())
			{
				if (orderProcessingErrorCode.getErrorCode() == errorCode) {
					return orderProcessingErrorCode;
				}
			}

			return OrderProcessingErrorCode.GENERIC_ORDER_PROCESSING_ERROR;
		}

		public int getErrorCode()
		{
			return errorCode;
		}

		public String getDescription()
		{
			return description;
		}
	}
	/**
	 * Error code marking the business logic problem
	 */
	private OrderProcessingErrorCode orderProcessingErrorCode;

	/**
	 * Explanation of the business issue
	 */
	private String businessReason;

	public OrderProcessingException(String message, OrderProcessingErrorCode orderProcessingErrorCode,  String businessReason)
	{
		super(message);
		this.orderProcessingErrorCode = orderProcessingErrorCode;
		this.businessReason = businessReason;
	}

	public OrderProcessingException(String message, OrderProcessingErrorCode orderProcessingErrorCode, String businessReason, Throwable cause)
	{
		super(message, cause);
		this.orderProcessingErrorCode = orderProcessingErrorCode;
		this.businessReason = businessReason;
	}

	public OrderProcessingErrorCode getOrderProcessingErrorCode()
	{
		return orderProcessingErrorCode;
	}

	public String getBusinessReason()
	{
		return businessReason;
	}
}
