/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaValidationMessagesStrategy;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.order.TmaEntryGroupService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.i18n.L10NService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service handling failed {@link TmaPolicyActionModel} having {@link TmaPoPolicyStatementModel} statement and
 * {@link TmaCompatibilityPolicyActionType#AUTOPICK} action type.
 *
 * @since 6.7
 */
public class TmaAutoPickPolicyActionResolver implements TmaPolicyActionResolver
{
	private static final Logger LOG = LoggerFactory.getLogger(TmaAutoPickPolicyActionResolver.class);
	private static final String ERROR_MESSAGE = "tmapopolicystatement.validationmessage.autopick.error";

	private CommerceCartService commerceCartService;
	private L10NService l10NService;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private EntryGroupService entryGroupService;

	private TmaValidationMessagesStrategy tmaValidationMessagesStrategy;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private TmaEntryGroupService tmaEntryGroupService;

	private TmaAbstractOrderEntryService abstractOrderEntryService;
	private TmaPoService poService;

	public TmaAutoPickPolicyActionResolver(final TmaAbstractOrderEntryService abstractOrderEntryService,
			final TmaPoService poService)
	{
		this.abstractOrderEntryService = abstractOrderEntryService;
		this.poService = poService;
	}

	@Override
	public void processPolicyActions(final AbstractOrderModel orderModel, final EntryGroup entryGroup,
			final List<TmaPolicyActionModel> policyActions)
	{
		if (entryGroup == null)
		{
			//autopick actions are supported only for entry groups
			return;
		}
		final List<TmaPolicyActionModel> invalidPolicies = new ArrayList<>();

		for (final TmaPolicyActionModel policyResult : policyActions)
		{
			if (!(policyResult.getStatement() instanceof TmaPoPolicyStatementModel))
			{
				continue;
			}
			final TmaPoPolicyStatementModel statementModel = (TmaPoPolicyStatementModel) policyResult.getStatement();
			final CommerceCartParameter parameter = getNewCartEntryGroupParameter(orderModel, entryGroup, statementModel);

			if (parameter == null)
			{
				LOG.error("AutoPick Policy " + policyResult.getCode() + " cannot be processed.");
				invalidPolicies.add(policyResult);
				continue;
			}
			try
			{
				getCommerceCartService().addToCart(parameter);
			}
			catch (final CommerceCartModificationException e)
			{
				LOG.error("AutoPick Policy cannot be processed:", e);
				invalidPolicies.add(policyResult);
			}
		}
		if (!invalidPolicies.isEmpty())
		{
			updateErrorMessages(orderModel, entryGroup, invalidPolicies);
		}
	}

	@Override
	public void processPolicyActions(final AbstractOrderModel orderModel, final CartEntryModel rootEntryModel,
			final List<TmaPolicyActionModel> policyActions)
	{
		if (rootEntryModel == null)
		{
			//autopick actions are supported only for bundles
			return;
		}

		final List<TmaPolicyActionModel> invalidPolicies = new ArrayList<>();

		for (final TmaPolicyActionModel policyResult : policyActions)
		{
			if (!(policyResult.getStatement() instanceof TmaPoPolicyStatementModel))
			{
				return;
			}

			final TmaPoPolicyStatementModel statementModel = (TmaPoPolicyStatementModel) policyResult.getStatement();

			if (!shouldUpdateEntry(rootEntryModel, statementModel))
			{
				LOG.error("AutoPick Policy {} cannot be processed.", policyResult.getCode());
				invalidPolicies.add(policyResult);
				return;
			}

			try
			{
				addToCart(statementModel, rootEntryModel, policyResult.getCode());
			}
			catch (CommerceCartModificationException e)
			{
				LOG.error("AutoPick Policy cannot be processed:", e);
				invalidPolicies.add(policyResult);
			}
		}

		if (CollectionUtils.isNotEmpty(invalidPolicies))
		{
			updateErrorMessages(rootEntryModel, invalidPolicies);
		}
	}

	/**
	 * Adds the provided product offering to the cart with.
	 *
	 * @param statementModel
	 * 		The statement used for getting the product to be added
	 * @param rootEntryModel
	 * 		The root entry
	 * @param policyCode
	 * 		The identifier of the policy
	 */
	protected void addToCart(final TmaPoPolicyStatementModel statementModel, final CartEntryModel rootEntryModel,
			final String policyCode) throws CommerceCartModificationException
	{
		final List<AbstractOrderEntryModel> entriesInBundle = getAbstractOrderEntryService().getAllChildEntries(rootEntryModel);
		final List<TmaBundledProductOfferingModel> parentBpos = getIntermediateParentsToBeAdded(
				statementModel.getProductOffering(), rootEntryModel, entriesInBundle);

		if (CollectionUtils.isEmpty(parentBpos))
		{
			throw new IllegalArgumentException(String.format(
					"AutoPick Policy %s cannot be processed. Cannot add product offering '%s' to bundled product offering '%s'.",
					policyCode, statementModel.getProductOffering(), rootEntryModel.getProduct().getCode()));
		}

		final TmaBundledProductOfferingModel firstParentAlreadyInCart = parentBpos.iterator().next();

		final AbstractOrderEntryModel parentEntry = entriesInBundle.stream()
				.filter((AbstractOrderEntryModel entry) -> entry.getProduct().getCode().equals(firstParentAlreadyInCart.getCode()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException(String.format(
						"AutoPick Policy %s cannot be processed. Cannot add product offering '%s' to bundled product offering '%s'.",
						policyCode, statementModel.getProductOffering(), rootEntryModel.getProduct().getCode())));

		addToCart(statementModel, parentBpos, parentEntry);
	}

	/**
	 * Adds the provided product offering with the given intermediate BPOs to the cart.
	 *
	 * @param statementModel
	 * 		The statement used for getting the product to be added
	 * @param parentBpos
	 * 		The intermediate products in the hierarchy
	 * @param entry
	 * 		The entry to which the new hierarchy will be added
	 * @throws CommerceCartModificationException
	 * 		If errors occur during add to cart
	 */
	protected void addToCart(final TmaPoPolicyStatementModel statementModel, final List<TmaBundledProductOfferingModel> parentBpos,
			final AbstractOrderEntryModel entry) throws CommerceCartModificationException
	{
		AbstractOrderEntryModel parentEntry = entry;

		for (int i = 1; i < parentBpos.size(); i++)
		{
			final CommerceCartParameter parameter = createCommerceCartParameter(parentEntry.getOrder(), parentBpos.get(i), 1,
					parentEntry, false);
			final CommerceCartModification modification = getCommerceCartService().addToCart(parameter);

			parentEntry = modification.getEntry();
		}

		final CommerceCartParameter parameter = createCommerceCartParameter(parentEntry.getOrder(),
				statementModel.getProductOffering(), statementModel.getMin(), parentEntry, true);
		getCommerceCartService().addToCart(parameter);
	}

	/**
	 * Returns the intermediate BPOs between the provided product offering and the product offering in the root entry which need to be added to cart.
	 *
	 * @param productOffering
	 * 		The product offering
	 * @param rootEntry
	 * 		The root entry
	 * @param entriesInBundle
	 * 		The entries part of the bundle
	 * @return The intermediate BPOs which need to be added to the cart
	 */
	protected List<TmaBundledProductOfferingModel> getIntermediateParentsToBeAdded(final TmaProductOfferingModel productOffering,
			final AbstractOrderEntryModel rootEntry, final List<AbstractOrderEntryModel> entriesInBundle)
	{
		final List<TmaBundledProductOfferingModel> parentBpos = getPoService().getIntermediateBpos(productOffering,
				(TmaBundledProductOfferingModel) rootEntry.getProduct());

		if (CollectionUtils.isEmpty(parentBpos))
		{
			return Collections.emptyList();
		}

		final List<String> posInBundleEntry = entriesInBundle.stream()
				.map(AbstractOrderEntryModel::getProduct)
				.map(ProductModel::getCode)
				.collect(Collectors.toList());

		final Optional<TmaBundledProductOfferingModel> firstParentAlreadyInCart = parentBpos.stream()
				.filter((TmaBundledProductOfferingModel bpo) -> posInBundleEntry.contains(bpo.getCode())).findFirst();

		if (firstParentAlreadyInCart.isEmpty())
		{
			return Collections.emptyList();
		}

		parentBpos.subList(parentBpos.indexOf(firstParentAlreadyInCart.get()) + 1, parentBpos.size()).clear();

		Collections.reverse(parentBpos);

		return parentBpos;
	}

	/**
	 * Creates the new cart parameter used for adding the new entry to cart.
	 *
	 * @param orderModel
	 * 		current order
	 * @param entryGroup
	 * 		the entry group where the new entry is added
	 * @param statementModel
	 * 		the statement used for getting the product to be added
	 * @return {@link CommerceCartParameter} for the new entry
	 * @deprecated since 2102
	 * Use {@link #createCommerceCartParameter(AbstractOrderModel, TmaProductOfferingModel, int, AbstractOrderEntryModel, boolean)}  instead}
	 */
	@Deprecated(since = "2102")
	protected CommerceCartParameter getNewCartEntryGroupParameter(final AbstractOrderModel orderModel, final EntryGroup entryGroup,
			final TmaPoPolicyStatementModel statementModel)
	{
		if (!shouldUpdateGroupEntries(orderModel, entryGroup, statementModel))
		{
			return null;
		}
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart((CartModel) orderModel);
		parameter.setProduct(statementModel.getProductOffering());
		parameter.setQuantity(statementModel.getMin());
		parameter.setAutomaticallyAdded(true);
		parameter.setBpoCode(entryGroup.getExternalReferenceId());
		parameter.setEntryGroupNumbers(Collections.singleton((entryGroup.getGroupNumber())));
		parameter.setProcessType(entryGroup.getProcessType());
		parameter.setAction(TmaSubscribedProductAction.ADD);
		parameter.setUser(orderModel.getUser());

		return parameter;
	}

	/**
	 * Creates the new cart parameter used for adding the new entry to cart.
	 *
	 * @param orderModel
	 * 		Current order
	 * @param productOffering
	 * 		The product offering to be added
	 * @param quantity
	 * 		The quantity to be added
	 * @param parentEntry
	 * 		The parent entry
	 * @param enableHooks
	 * 		Flag indicating if hooks should be executed or not
	 * @return {@link CommerceCartParameter} for the new entry
	 */
	protected CommerceCartParameter createCommerceCartParameter(final AbstractOrderModel orderModel,
			final TmaProductOfferingModel productOffering, final int quantity, final AbstractOrderEntryModel parentEntry,
			final boolean enableHooks)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();

		parameter.setEnableHooks(enableHooks);
		parameter.setCart((CartModel) orderModel);
		parameter.setProduct(productOffering);
		parameter.setQuantity(quantity);
		parameter.setAutomaticallyAdded(true);
		parameter.setAction(TmaSubscribedProductAction.ADD);
		parameter.setUser(orderModel.getUser());
		parameter.setParentEntry(parentEntry);
		parameter.setBpoCode(parentEntry.getBpo().getCode());
		parameter.setProcessType(parentEntry.getProcessType().getCode());

		return parameter;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private boolean shouldUpdateGroupEntries(final AbstractOrderModel orderModel, final EntryGroup entryGroup,
			final TmaPoPolicyStatementModel statementModel)
	{
		return orderModel.getEntries().stream().filter(entry -> entry
				.getEntryGroupNumbers().contains(entryGroup.getGroupNumber()) && entry.getProduct().equals(statementModel
				.getProductOffering())
				&& entry.getQuantity() >= statementModel.getMin()).count() == 0;
	}

	private boolean shouldUpdateEntry(final AbstractOrderEntryModel parentEntry, final TmaPoPolicyStatementModel statementModel)
	{
		final AbstractOrderEntryModel rootEntry = getAbstractOrderEntryService().getRootEntry(parentEntry);
		final List<AbstractOrderEntryModel> allEntries = getAbstractOrderEntryService().getAllChildEntries(rootEntry);
		allEntries.add(rootEntry);

		return allEntries.stream()
				.noneMatch((AbstractOrderEntryModel entry) -> entry.getProduct().equals(statementModel.getProductOffering())
						&& entry.getQuantity() >= statementModel.getMin());
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private void updateErrorMessages(final AbstractOrderModel orderModel, final EntryGroup entryGroup,
			final List<TmaPolicyActionModel> invalidPolicies)
	{
		final List<String> errorMessages = invalidPolicies.stream()
				.map(policy -> getL10NService().getLocalizedString(ERROR_MESSAGE, new Object[]
						{ ((TmaPoPolicyStatementModel) policy.getStatement()).getProductOffering().getName() }))
				.collect(Collectors.toList());

		if (!entryGroup.getErroneous() || getTmaValidationMessagesStrategy()
				.shouldUpdateValidationMessages(entryGroup, errorMessages))
		{
			getTmaValidationMessagesStrategy().cleanupValidationMessagesOn(orderModel, entryGroup);
			getTmaValidationMessagesStrategy().setValidationMessagesOn(entryGroup, errorMessages);
			getTmaEntryGroupService().updateEntryGroup(orderModel, entryGroup);
		}
	}

	private void updateErrorMessages(final CartEntryModel parentEntry, final List<TmaPolicyActionModel> invalidPolicies)
	{
		final List<String> errorMessages = invalidPolicies.stream()
				.map(policy -> getL10NService().getLocalizedString(ERROR_MESSAGE, new Object[]
						{ ((TmaPoPolicyStatementModel) policy.getStatement()).getProductOffering().getName() }))
				.collect(Collectors.toList());

		if (getTmaValidationMessagesStrategy().shouldUpdateValidationMessages(parentEntry, errorMessages))
		{
			getTmaValidationMessagesStrategy().cleanupValidationMessagesOn(parentEntry);
			getTmaValidationMessagesStrategy().setValidationMessagesOn(parentEntry, errorMessages);
		}
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	protected L10NService getL10NService()
	{
		return l10NService;
	}

	public void setL10NService(final L10NService l10NService)
	{
		this.l10NService = l10NService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected EntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	public void setEntryGroupService(final EntryGroupService entryGroupService)
	{
		this.entryGroupService = entryGroupService;
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

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	public TmaEntryGroupService getTmaEntryGroupService()
	{
		return tmaEntryGroupService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	public void setTmaEntryGroupService(TmaEntryGroupService tmaEntryGroupService)
	{
		this.tmaEntryGroupService = tmaEntryGroupService;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}

	protected TmaPoService getPoService()
	{
		return poService;
	}
}
