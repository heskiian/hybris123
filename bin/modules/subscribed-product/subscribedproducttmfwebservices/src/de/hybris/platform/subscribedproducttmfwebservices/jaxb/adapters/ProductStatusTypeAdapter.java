/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscribedproducttmfwebservices.jaxb.adapters;

import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductStatusType;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;


/**
 * Adapter used for marshalling/unmarshalling from {@link String} to {@link ProductStatusType} or vice versa.
 *
 * @since 2111
 */
public class ProductStatusTypeAdapter extends XmlAdapter<String, ProductStatusType>
{
	@Override
	public ProductStatusType unmarshal(final String s)
	{
		return ProductStatusType.fromValue(s);
	}

	@Override
	public String marshal(final ProductStatusType productStatusType)
	{
		return productStatusType != null ? productStatusType.toString() : StringUtils.EMPTY;
	}
}
