/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaValidationMessagesStrategy;
import de.hybris.platform.b2ctelcoservices.model.TmaPoGroupPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPscvPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.order.TmaEntryGroupService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;


/**
 * Service handling failed {@link TmaPolicyActionModel} having
 * {@link de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType#SELECT} action type.
 *
 * @since 6.7
 */
public class TmaSelectPolicyActionResolver implements TmaPolicyActionResolver
{
	private static final String VALIDATION_MESSAGE = "validationmessage";
	private static final String INCOMPATIBLE = "incompatible";
	private static final String GENERAL = "general";
	private static final String MINIMUM_VALUE = "minvalue";
	private static final String MAXIMUM_VALUE = "maxvalue";
	private static final String SEPARATOR = ".";

	private L10NService l10NService;
	private EntryGroupService entryGroupService;
	private TmaValidationMessagesStrategy tmaValidationMessagesStrategy;
	private TmaEntryGroupService tmaEntryGroupService;

	/**
	 * @deprecated since 2011 - saving the validation messages has been moved to TmaValidationMessageStrategy
	 */
	@Deprecated(since = "2011", forRemoval = true)
	private ModelService modelService;

	/**
	 * Updates the entry group with the error messages computed based on the failed poly actions.
	 */
	@Override
	public void processPolicyActions(final AbstractOrderModel orderModel, final EntryGroup entryGroup,
			final List<TmaPolicyActionModel> policyActions)
	{
		final Set<String> errorMessages = policyActions.stream().map(invalidPolicy -> getErrorMessage(invalidPolicy.getStatement()))
				.collect(Collectors.toSet());
		final boolean newErrorsFound = !errorMessages.isEmpty();

		final List<String> errorMessagesList = new ArrayList<>(errorMessages);
		if (entryGroup == null)
		{
			getTmaValidationMessagesStrategy().setValidationMessagesOn(((CartModel) orderModel), errorMessagesList);
			return;
		}

		if (newErrorsFound != entryGroup.getErroneous() || (newErrorsFound && getTmaValidationMessagesStrategy()
				.shouldUpdateValidationMessages(entryGroup, errorMessagesList)))
		{
			getTmaValidationMessagesStrategy().cleanupValidationMessagesOn(orderModel, entryGroup);
			getTmaValidationMessagesStrategy().setValidationMessagesOn(entryGroup, errorMessagesList);
			getTmaEntryGroupService().updateEntryGroup(orderModel, entryGroup);
		}
	}

	@Override
	public void processPolicyActions(final AbstractOrderModel orderModel, final CartEntryModel parentEntryModel,
			final List<TmaPolicyActionModel> policyActions)
	{
		final List<String> errorMessages = policyActions.stream().map(policyAction -> getErrorMessage(policyAction.getStatement()))
				.distinct().collect(Collectors.toList());

		if (parentEntryModel == null)
		{
			getTmaValidationMessagesStrategy().setValidationMessagesOn(((CartModel) orderModel), errorMessages);
			return;
		}

		if (getTmaValidationMessagesStrategy().shouldUpdateValidationMessages(parentEntryModel, errorMessages))
		{
			getTmaValidationMessagesStrategy().cleanupValidationMessagesOn(parentEntryModel);
			getTmaValidationMessagesStrategy().setValidationMessagesOn(parentEntryModel, errorMessages);
		}
	}


	protected String getErrorMessage(final TmaPolicyStatementModel statement)
	{
		if (statement instanceof TmaSubscribedPoPolicyStatementModel)
		{
			return getPolicyStatementMessageProperty(statement, GENERAL);
		}
		if (statement.getMin() == 0 && statement.getMax() != null)
		{
			if (statement.getMax() == 0)
			{
				return getPolicyStatementMessageProperty(statement, INCOMPATIBLE);
			}
			else
			{
				return getPolicyStatementMessageProperty(statement, MAXIMUM_VALUE);
			}
		}
		if (statement.getMin() > 0 && statement.getMax() == null)
		{
			return getPolicyStatementMessageProperty(statement, MINIMUM_VALUE);
		}

		return getPolicyStatementMessageProperty(statement, GENERAL);
	}

	private String getPolicyStatementMessageProperty(final TmaPolicyStatementModel statementModel, final String suffix)
	{
		final String messageProperty = String
				.join(SEPARATOR, StringUtils.lowerCase(statementModel.getItemtype()), VALIDATION_MESSAGE, suffix);

		if (statementModel instanceof TmaPoPolicyStatementModel)
		{
			return getL10NService().getLocalizedString(messageProperty, new Object[]
					{ ((TmaPoPolicyStatementModel) statementModel).getProductOffering().getName(), statementModel.getMin(),
							statementModel.getMax() });
		}
		if (statementModel instanceof TmaPoGroupPolicyStatementModel)
		{
			return getL10NService().getLocalizedString(messageProperty, new Object[]
					{ ((TmaPoGroupPolicyStatementModel) statementModel).getProductOfferingGroup().getName(), statementModel.getMin(),
							statementModel.getMax() });
		}
		if (statementModel instanceof TmaPscvPolicyStatementModel)
		{
			final TmaPscvPolicyStatementModel pscvStatement = (TmaPscvPolicyStatementModel) statementModel;
			return getL10NService().getLocalizedString(messageProperty, new Object[]
					{ pscvStatement.getProductSpecCharacteristic().getName(),
							pscvStatement.getValueMin(),
							pscvStatement.getMin(),
							pscvStatement.getMax() });
		}
		return Strings.EMPTY;
	}

	protected L10NService getL10NService()
	{
		return l10NService;
	}

	public void setL10NService(final L10NService l10NService)
	{
		this.l10NService = l10NService;
	}

	protected EntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	public void setEntryGroupService(final EntryGroupService entryGroupService)
	{
		this.entryGroupService = entryGroupService;
	}

	/**
	 * @deprecated since 2011 - saving the validation messages has been moved to TmaValidationMessageStrategy
	 */
	@Deprecated(since = "2011", forRemoval = true)
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @deprecated since 2011 - saving the validation messages has been moved to TmaValidationMessageStrategy
	 */
	@Deprecated(since = "2011", forRemoval = true)
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TmaValidationMessagesStrategy getTmaValidationMessagesStrategy()
	{
		return tmaValidationMessagesStrategy;
	}

	public void setTmaValidationMessagesStrategy(
			TmaValidationMessagesStrategy tmaValidationMessagesStrategy)
	{
		this.tmaValidationMessagesStrategy = tmaValidationMessagesStrategy;
	}

	public TmaEntryGroupService getTmaEntryGroupService()
	{
		return tmaEntryGroupService;
	}

	public void setTmaEntryGroupService(TmaEntryGroupService tmaEntryGroupService)
	{
		this.tmaEntryGroupService = tmaEntryGroupService;
	}
}
