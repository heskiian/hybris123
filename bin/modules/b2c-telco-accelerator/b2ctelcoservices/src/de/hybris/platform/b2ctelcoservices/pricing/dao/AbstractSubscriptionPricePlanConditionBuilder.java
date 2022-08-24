/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import de.hybris.platform.b2ctelcoservices.daos.TmaGenericConditionBuilder;
import de.hybris.platform.b2ctelcoservices.daos.TmaSearchQueryException;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;


/**
 * Abstract builder for enhancing the {@link SubscriptionPricePlanModel} related search queries.
 *
 * @param <T>
 * 		parameter based on which the search query will be enhanced.
 * @since 1810
 */
public abstract class AbstractSubscriptionPricePlanConditionBuilder<T> implements TmaGenericConditionBuilder<T>
{
	private static final String PK_FIELD = "pk";

	protected static final String SOURCE_RELATION_NAME = "source";
	protected static final String TARGET_RELATION_NAME = "target";

	@Override
	public GenericConditionList buildQueryConditions(final GenericQuery query, final T param)
			throws TmaSearchQueryException
	{
		if (!shouldApplyCondition(param))
		{
			return GenericConditionList.createConditionList();
		}

		return createQueryConditions(query, param);
	}

	abstract protected boolean shouldApplyCondition(final T parameter);

	abstract protected GenericConditionList createQueryConditions(final GenericQuery query, final T param)
			throws TmaSearchQueryException;

	/**
	 * Adds a many to many relation defined by the given params to the provided {@param query} and returns
	 * the target {@link GenericSearchField}.
	 *
	 * @param query
	 * 		query to be enhanced with relation join
	 * @param relationName
	 * 		name of the relation to be added
	 * @param subscriptionPricePlanRelationName
	 * 		source name of the relation
	 * @param targetTypeCode
	 * 		type code of the item type, target of the relation
	 * @param targetTypeCodeRelationName
	 * 		target name of the relation
	 * @return the pk field of the target
	 */
	protected GenericSearchField addRelationJoinToQuery(final GenericQuery query, final String relationName,
			final String subscriptionPricePlanRelationName, final String targetTypeCode, final String targetTypeCodeRelationName)
	{
		final String sourceTypeCode = query.getInitialTypeCode().equalsIgnoreCase(PriceRowModel._TYPECODE)
				? PriceRowModel._TYPECODE
				: SubscriptionPricePlanModel._TYPECODE;
		final GenericSearchField sourcePkField = new GenericSearchField(sourceTypeCode, PK_FIELD);
		final GenericSearchField relationSourceField = new GenericSearchField(relationName, subscriptionPricePlanRelationName);
		final GenericCondition sourcePkToSourceMatchCondition = GenericCondition
				.createJoinCondition(sourcePkField, relationSourceField);

		final GenericSearchField targetPkField = new GenericSearchField(targetTypeCode, PK_FIELD);
		final GenericSearchField relationTargetField = new GenericSearchField(relationName, targetTypeCodeRelationName);
		final GenericCondition targetPkToTargetMatchCondition = GenericCondition
				.createJoinCondition(targetPkField, relationTargetField);

		query.addOuterJoin(relationName, sourcePkToSourceMatchCondition);
		query.addOuterJoin(targetTypeCode, targetPkToTargetMatchCondition);

		return targetPkField;
	}

	/**
	 * Determines whether the product is a Bundled Product Offering other then Fixed Bundled Product Offering.
	 *
	 * @param poModel
	 * 		product offering whose instance will be checked
	 * @return <code>true</code> in case this offering is a normal BPO (not a fixed price one), <code>false</code> otherwise
	 */
	protected boolean productIsBpo(final ProductModel poModel)
	{
		return poModel instanceof TmaBundledProductOfferingModel && !(poModel instanceof TmaFixedBundledProductOfferingModel);
	}
}
