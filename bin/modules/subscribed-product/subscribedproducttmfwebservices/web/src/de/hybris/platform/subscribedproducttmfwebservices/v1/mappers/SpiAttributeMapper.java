/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers;


import ma.glasnost.orika.MappingContext;


/**
 * This attribute mapper class populates the value of a {@link TARGET} attribute by processing a {@link SOURCE}
 * object
 *
 * @since 2105
 */
public abstract class SpiAttributeMapper<SOURCE, TARGET>
{
	private String sourceAttributeName;
	private String targetAttributeName;

	public SpiAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		this.sourceAttributeName = sourceAttributeName;
		this.targetAttributeName = targetAttributeName;
	}

	/**
	 * Populates the target attribute value obtained by processing the source object.
	 *
	 * @param source
	 * 		source object
	 * @param target
	 * 		target object
	 */
	public abstract void populateTargetAttributeFromSource(SOURCE source, TARGET target, final MappingContext context);

	/**
	 * Populates the source attribute value obtained by processing the target object.
	 *
	 * @param source
	 * 		source object
	 * @param target
	 * 		target object
	 */
	public void populateSourceAttributeFromTarget(final TARGET target, final SOURCE source, final MappingContext context)
	{
	}

	public String getSourceAttributeName()
	{
		return sourceAttributeName;
	}

	public String getTargetAttributeName()
	{
		return targetAttributeName;
	}

}
