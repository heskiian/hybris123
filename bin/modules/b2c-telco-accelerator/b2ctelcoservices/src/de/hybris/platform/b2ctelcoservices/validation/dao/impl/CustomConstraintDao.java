/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.validation.dao.impl;

import de.hybris.platform.b2ctelcoservices.model.ClassificationNotBlankConstraintModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.validation.daos.impl.DefaultConstraintDao;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

import java.util.List;


/**
 * Overrides the DefaultConstraintDao to allow more than 1
 * {@link de.hybris.platform.b2ctelcoservices.jalo.ClassificationNotBlankConstraint} per target (= type to be checked).
 * As the {@link de.hybris.platform.b2ctelcoservices.jalo.ClassificationNotBlankConstraint} inherits from TypeConstraint
 * the DefaultConstraintDao would select the constraints by the attributes target, annotation and active only. But the
 * {@link de.hybris.platform.b2ctelcoservices.jalo.ClassificationNotBlankConstraint}s need to have the additional
 * attributes category and classification to be unique.
 */
public class CustomConstraintDao extends DefaultConstraintDao
{
	@Override
	public List<AbstractConstraintModel> getConstraintDuplicates(final AbstractConstraintModel constraint)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {").append("pk").append("} ");
		if (constraint instanceof AttributeConstraintModel)
		{
			builder.append("FROM {").append(AttributeConstraintModel._TYPECODE).append(" AS constraint} ");
		}
		else if (constraint instanceof ClassificationNotBlankConstraintModel)
		{
			builder.append("FROM {").append(ClassificationNotBlankConstraintModel._TYPECODE).append(" AS constraint} ");
		}
		else
		{
			builder.append("FROM {").append(AttributeConstraintModel._TYPECODE).append(" AS constraint} ");
		}
		builder.append("WHERE ");
		if (constraint.getPk() != null)
		{
			builder.append("{constraint:").append(AbstractConstraintModel.PK).append("} != ?PK ");
			builder.append("AND ");
		}
		builder.append("{constraint:").append("target").append("}").append("=?target ");
		builder.append("AND ").append("{constraint:").append("annotation").append("}").append("= ?annot ");
		builder.append("AND ").append("{constraint:").append("active").append("}").append("= ?active ");
		if (constraint instanceof AttributeConstraintModel)
		{
			builder.append("AND ").append("{constraint:").append("qualifier").append("}").append("= ?qualifier ");
		}
		if (constraint instanceof ClassificationNotBlankConstraintModel)
		{
			builder.append("AND ").append("{constraint:").append(ClassificationNotBlankConstraintModel.CATEGORY).append("}")
					.append("= ?category ");
			builder.append("AND ").append("{constraint:").append(ClassificationNotBlankConstraintModel.CLASSIFICATION).append("}")
					.append("= ?classification ");
		}

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		if (constraint.getPk() != null)
		{
			query.addQueryParameter("PK", constraint.getPk());
		}
		query.addQueryParameter("target", constraint.getTarget());
		query.addQueryParameter("annot", constraint.getAnnotation().getName());
		query.addQueryParameter("active", Boolean.TRUE);
		if (constraint instanceof AttributeConstraintModel)
		{
			query.addQueryParameter("qualifier", ((AttributeConstraintModel) constraint).getQualifier());
		}
		if (constraint instanceof ClassificationNotBlankConstraintModel)
		{
			query.addQueryParameter("category", ((ClassificationNotBlankConstraintModel) constraint).getCategory());
			query.addQueryParameter("classification", ((ClassificationNotBlankConstraintModel) constraint).getClassification());
		}

		return getFlexibleSearchService().<AbstractConstraintModel>search(query).getResult();
	}
}
