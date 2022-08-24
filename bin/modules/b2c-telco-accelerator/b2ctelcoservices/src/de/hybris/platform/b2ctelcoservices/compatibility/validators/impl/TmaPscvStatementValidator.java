/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.TmaPolicyPscvValueComparator;
import de.hybris.platform.b2ctelcoservices.enums.TmaPscvValueType;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPscvPolicyStatementModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Checks if a PscvStatement (typically part of a Policy) is valid or not. e.g. [if] you have and 100mbps(PSCV) internet
 * connection [then] you can buy maximum 2 media boxes (PO).
 *
 * @since 1805
 */
public class TmaPscvStatementValidator extends AbstractTmaPolicyStatementValidator
{
	private static final String CONFIGURABLE_CHARACTERISTIC_VALUE_PATTERN = "configurable.characteristic.value.pattern";
	/**
	 * map of comparators &lt;key: the value type/&gt; &lt;value: the comparator (e.g. to compare 1024 mbps with 0.8
	 * gbps)./&gt;
	 */
	private Map<TmaPscvValueType, TmaPolicyPscvValueComparator> comparators;
	private ConfigurationService configurationService;

	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaPscvPolicyStatementModel))
		{
			return contexts;
		}
		final TmaPscvPolicyStatementModel pscvStatement = (TmaPscvPolicyStatementModel) statement;

		final Set<TmaPolicyContext> validContexts = new HashSet<>();

		contexts.forEach(context -> {
			if (CollectionUtils.isNotEmpty(context.getProductOfferings()))
			{
				context.getProductOfferings().forEach(productOffering -> {
					validContexts.addAll(getApplicableContext(productOffering, context, pscvStatement));
				});
			}
		});

		return new ArrayList<>(validContexts);
	}

	private Set<TmaPolicyContext> getApplicableContext(final TmaProductOfferingModel offer, final TmaPolicyContext policyContext,
			final TmaPscvPolicyStatementModel pscvStatement)
	{
		final Set<TmaPolicyContext> validContexts = new HashSet<>();
		final TmaProductSpecCharacteristicModel psc = pscvStatement.getProductSpecCharacteristic();
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = offer.getProductSpecCharValueUses().stream()
				.flatMap(pscvu -> pscvu.getProductSpecCharacteristicValues().stream()).collect(Collectors.toSet());
		for (final TmaProductSpecCharacteristicValueModel prodPscv : pscvs)
		{
			if (!prodPscv.getProductSpecCharacteristic().equals(psc))
			{
				continue;
			}
			if (!(prodPscv.getValue()
					.equals(getConfigurationService().getConfiguration().getString(CONFIGURABLE_CHARACTERISTIC_VALUE_PATTERN)))
					&& compare(prodPscv.getValueType(), prodPscv.getValue(), prodPscv.getUnitOfMeasure(), pscvStatement.getValueMin(),
							pscvStatement.getValueMax(), pscvStatement.getUnitOfMeasure()))
			{
				validContexts.add(policyContext);
			}
		}
		return validContexts;
	}

	private boolean compare(final TmaPscvValueType valType, final String prodPscvValue, final UsageUnitModel prodPscvUnit,
			final String stMin, final String stMax, final UsageUnitModel stUnit)
	{
		final TmaPolicyPscvValueComparator cmp = valType == null ? getComparators().get(TmaPscvValueType.NUMERIC)
				: getComparators().get(valType);
		return cmp.compare(prodPscvValue, prodPscvUnit, stMin, stMax, stUnit);
	}

	public Map<TmaPscvValueType, TmaPolicyPscvValueComparator> getComparators()
	{
		return comparators;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setComparators(final Map<TmaPscvValueType, TmaPolicyPscvValueComparator> comparators)
	{
		this.comparators = comparators;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
