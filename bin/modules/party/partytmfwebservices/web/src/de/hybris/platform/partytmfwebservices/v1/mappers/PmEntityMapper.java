/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.mappers;

import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.List;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;


/**
 * This Mapper class maps data between resource {@link SOURCE} and {@link TARGET}
 *
 * @since 2108
 */
public class PmEntityMapper<SOURCE, TARGET> extends AbstractCustomMapper<SOURCE, TARGET>
{
	/**
	 * Source class
	 */
	private Class<SOURCE> sourceClass;
	/**
	 * Target class
	 */
	private Class<TARGET> targetClass;

	/**
	 * List of attribute mappers
	 */
	private List<PmAttributeMapper<SOURCE, TARGET>> attributeMappers;

	public PmEntityMapper(final Class<SOURCE> sourceClass, final Class<TARGET> targetClass,
			final List<PmAttributeMapper<SOURCE, TARGET>> attributeMappers)
	{
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
		this.attributeMappers = attributeMappers;
	}

	/**
	 * {@inheritDoc} Populates the attributes on the target object with values of the attributes from source object, by
	 * calling the attribute mappers.
	 *
	 * @param source
	 * 		the source object
	 * @param target
	 * 		the target object
	 * @param context
	 * 		the context
	 */
	@Override
	public void mapAtoB(final SOURCE source, final TARGET target, final MappingContext context)
	{
		getAttributeMappers().forEach(attributeMapper ->
		{
			context.beginMappingField(attributeMapper.getSourceAttributeName(), getAType(), source,
					attributeMapper.getTargetAttributeName(), getBType(), target);
			try
			{
				if (shouldMap(source, target, context))
				{
					attributeMapper.populateTargetAttributeFromSource(source, target, context);
				}
			}
			finally
			{
				context.endMappingField();
			}
		});
	}

	/**
	 * {@inheritDoc} Populates the attributes on the source object with values of the attributes from target object, by
	 * calling the attribute mappers.
	 *
	 * @param source
	 * 		the source object
	 * @param target
	 * 		the target object
	 * @param context
	 * 		the context
	 */
	@Override
	public void mapBtoA(final TARGET target, final SOURCE source, final MappingContext context)
	{
		getAttributeMappers().forEach(attributeMapper ->
		{
			context.beginMappingField(attributeMapper.getTargetAttributeName(), getBType(), target,
					attributeMapper.getSourceAttributeName(), getAType(), source);
			try
			{
				if (shouldMap(target, source, context))
				{
					attributeMapper.populateSourceAttributeFromTarget(target, source, context);
				}
			}
			finally
			{
				context.endMappingField();
			}
		});
	}

	@Override
	public Type<SOURCE> getAType()
	{
		return TypeFactory.valueOf(getSourceClass());
	}

	@Override
	public Type<TARGET> getBType()
	{
		return TypeFactory.valueOf(getTargetClass());
	}

	@SuppressWarnings("WeakerAccess")
	protected Class<SOURCE> getSourceClass()
	{
		return sourceClass;
	}

	@SuppressWarnings("WeakerAccess")
	protected Class<TARGET> getTargetClass()
	{
		return targetClass;
	}

	@SuppressWarnings("WeakerAccess")
	protected List<PmAttributeMapper<SOURCE, TARGET>> getAttributeMappers()
	{
		return attributeMappers;
	}
}
