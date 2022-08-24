/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.checklist.converters.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionData;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaChecklistPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates the attributes of {@link TmaChecklistActionData} from {@link RuleEvaluationResult}
 *
 * @since 1907
 */
public class TmaChecklistActionDataPopulator implements Populator<RuleEvaluationResult, TmaChecklistActionData>
{
	private Converter<ProductModel, ProductData> productDataConverter;
	private Map<String, Populator> statementTypeMap;

	@Override
	public void populate(final RuleEvaluationResult source, final TmaChecklistActionData target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("action", source.getAction());
		validateParameterNotNullStandardMessage("statement", source.getAction().getStatement());
		validateParameterNotNullStandardMessage("target", target);

		target.setName(source.getAction().getStatement().getName());
		populateActionType(source, target);
		populateStatement(source, target);

		if (CollectionUtils.isNotEmpty(source.getContexts()))
		{
			populateProcessTypes(source, target);
			populateProducts(source, target);
		}
	}

	private void populateProcessTypes(final RuleEvaluationResult source, final TmaChecklistActionData target)
	{
		final Set<TmaProcessType> processTypes = new HashSet<>();
		source.getContexts().forEach(tmaPolicyContext ->
		{
			if (tmaPolicyContext.getProcessType() != null)
			{
				processTypes.add(tmaPolicyContext.getProcessType());
			}
		});
		target.setProcessTypes(processTypes);
	}

	private void populateProducts(final RuleEvaluationResult source, final TmaChecklistActionData target)
	{
		if (CollectionUtils.isEmpty(source.getContexts()))
		{
			return;
		}

		final List<ProductData> productDataList = new ArrayList<>();
		source.getContexts().forEach(tmaPolicyContext ->
		{
			if (CollectionUtils.isNotEmpty(tmaPolicyContext.getProductOfferings()))
			{
				tmaPolicyContext.getProductOfferings().forEach(productoffering ->
				{
					productDataList.add(getProductDataConverter().convert(productoffering));

				});
			}
		});
		target.setProductOfferings(productDataList);

	}

	private void populateActionType(final RuleEvaluationResult source, final TmaChecklistActionData target)
	{
		final TmaPolicyStatementModel tmaPolicyStatementModel = source.getAction().getStatement();
		if (tmaPolicyStatementModel instanceof TmaChecklistPolicyStatementModel)
		{
			final TmaChecklistPolicyStatementModel tmaChecklistPolicyStatement = (TmaChecklistPolicyStatementModel) tmaPolicyStatementModel;
			target.setActionType(tmaChecklistPolicyStatement.getType());
		}
	}

	private void populateStatement(final RuleEvaluationResult source, final TmaChecklistActionData target)
	{
		final Populator populator = getStatementTypeMap().get(source.getAction().getStatement().getClass().getSimpleName());
		if (populator != null)
		{
			populator.populate(source.getAction().getStatement(), target);
		}
	}

	protected Converter<ProductModel, ProductData> getProductDataConverter()
	{
		return productDataConverter;
	}

	@Required
	public void setProductDataConverter(final Converter<ProductModel, ProductData> productDataConverter)
	{
		this.productDataConverter = productDataConverter;
	}

	protected Map<String, Populator> getStatementTypeMap()
	{
		return statementTypeMap;
	}

	@Required
	public void setStatementTypeMap(final Map<String, Populator> statementTypeMap)
	{
		this.statementTypeMap = statementTypeMap;
	}
}
