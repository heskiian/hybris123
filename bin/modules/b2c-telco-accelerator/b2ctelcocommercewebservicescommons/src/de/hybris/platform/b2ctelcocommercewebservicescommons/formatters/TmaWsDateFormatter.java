/*
 *	Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.formatters;

import java.util.Date;


/**
 * The Date Formatter
 * 
 * @since 2003
 *
 */
public interface TmaWsDateFormatter
{
	Date toDate(String timestamp);

	String toString(Date date);
}
