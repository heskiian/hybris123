/*
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers;

import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;


/**
 * TmaEntityMapper class maps data in between TMF resource{@link SOURCE} and {@link TARGET}
 *
 * @deprecated since 1911. Use TmaEntityMapper from b2ctelcofacade instead
 *
 */
@Deprecated(since = "1911", forRemoval= true)
public class TmaEntityMapper<SOURCE, TARGET> extends AbstractCustomMapper<SOURCE, TARGET>
{

	private Class<SOURCE> sourceClass;

	private Class<TARGET> targetClass;

	private List<TmaAttributeMapper<SOURCE, TARGET>> attributeMappers;


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

	@Required
	public void setSourceClass(final Class<SOURCE> sourceClass)
	{
		this.sourceClass = sourceClass;
	}

	@SuppressWarnings("WeakerAccess")
	protected Class<TARGET> getTargetClass()
	{
		return targetClass;
	}

	@Required
	public void setTargetClass(final Class<TARGET> targetClass)
	{
		this.targetClass = targetClass;
	}

	@SuppressWarnings("WeakerAccess")
	protected List<TmaAttributeMapper<SOURCE, TARGET>> getAttributeMappers()
	{
		return attributeMappers;
	}

	@Required
	public void setAttributeMappers(final List<TmaAttributeMapper<SOURCE, TARGET>> attributeMappers)
	{
		this.attributeMappers = attributeMappers;
	}

}
