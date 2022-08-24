/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.cardinality.impl;

import de.hybris.platform.b2ctelcoservices.cardinality.TmaBpoCardinalityService;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaValidationMessagesStrategy;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.order.TmaEntryGroupService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.i18n.L10NService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.assertj.core.util.Lists;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of the {@link TmaBpoCardinalityService}.
 *
 * @since 2011
 */
public class DefaultTmaBpoCardinalityService implements TmaBpoCardinalityService
{
	private static final String VALIDATION_MESSAGE = "validationmessage";
	private static final String MIN_AND_MAX = "minandmax";
	private static final String MIN = "min";
	private static final String SEPARATOR = ".";
	private static final String ABSTRACT_ORDER_CAN_NOT_BE_NULL = "The abstract order can not be null.";
	private static final String BPO_CAN_NOT_BE_NULL = "Bundled Product Offering can not be null.";
	private static final String BPO_CHILDREN_CAN_NOT_BE_NULL = "Bundled Product Offering's children can not be null.";

	private TmaPoService poService;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private TmaEntryGroupService entryGroupService;

	private TmaAbstractOrderEntryService abstractOrderEntryService;
	private TmaValidationMessagesStrategy validationMessagesStrategy;
	private L10NService l10NService;

	public DefaultTmaBpoCardinalityService(final TmaPoService poService, final TmaEntryGroupService entryGroupService,
			final TmaAbstractOrderEntryService abstractOrderEntryService,
			final TmaValidationMessagesStrategy validationMessagesStrategy, final L10NService l10NService)
	{
		this.poService = poService;
		this.entryGroupService = entryGroupService;
		this.abstractOrderEntryService = abstractOrderEntryService;
		this.validationMessagesStrategy = validationMessagesStrategy;
		this.l10NService = l10NService;
	}

	@Override
	public List<String> verifyCardinality(final AbstractOrderModel abstractOrder, final EntryGroup entryGroup)
	{
		validateParameterNotNull(abstractOrder, ABSTRACT_ORDER_CAN_NOT_BE_NULL);
		validateParameterNotNull(entryGroup, "The entry group can not be null.");

		final List<AbstractOrderEntryModel> entriesFromGroup = getEntryGroupService()
				.getEntriesFrom(abstractOrder, entryGroup);

		if (CollectionUtils.isEmpty(entriesFromGroup))
		{
			return Collections.emptyList();
		}

		final TmaBundledProductOfferingModel bundledProductOffering = getPoService()
				.getBpoForCode(entryGroup.getExternalReferenceId());

		return verifyCardinality(entriesFromGroup, bundledProductOffering, null);
	}

	@Override
	public Map<AbstractOrderEntryModel, List<String>> verifyCardinality(final AbstractOrderEntryModel entry)
	{
		validateParameterNotNull(entry, "The entry can not be null.");

		if (CollectionUtils.isEmpty(entry.getChildEntries()))
		{
			return Collections.emptyMap();
		}

		final TmaBundledProductOfferingModel bundledProductOffering = (TmaBundledProductOfferingModel) entry.getProduct();

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = createEmptyValidations(entry);
		validationMessages.putAll(verifyCardinality(entry.getChildEntries(), entry, bundledProductOffering, null));

		return validationMessages;
	}

	@Override
	public Map<EntryGroup, List<String>> verifyCardinality(final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(abstractOrder, ABSTRACT_ORDER_CAN_NOT_BE_NULL);

		if (CollectionUtils.isEmpty(abstractOrder.getEntryGroups()))
		{
			return Collections.emptyMap();
		}

		final Map<EntryGroup, List<String>> validationMessages = new HashMap<>();

		abstractOrder.getEntryGroups().forEach((EntryGroup entryGroup) -> validationMessages
				.put(entryGroup, verifyCardinality(abstractOrder, entryGroup)));

		return validationMessages;
	}

	@Override
	public Map<AbstractOrderEntryModel, List<String>> verifyCardinalityForOrder(final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(abstractOrder, ABSTRACT_ORDER_CAN_NOT_BE_NULL);

		if (CollectionUtils.isEmpty(abstractOrder.getEntries()))
		{
			return Collections.emptyMap();
		}

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = new HashMap<>();

		abstractOrder.getEntries().stream().filter((AbstractOrderEntryModel entry) -> entry.getMasterEntry() == null)
				.forEach((AbstractOrderEntryModel entry) -> validationMessages.putAll(verifyCardinality(entry)));
		return validationMessages;
	}

	@Override
	public boolean validate(final TmaBundledProdOfferOptionModel cardinalityRule, final long quantity)
	{
		validateParameterNotNull(cardinalityRule, "The cardinality rule can not be null.");

		return ((cardinalityRule.getUpperLimit() == null || quantity <= cardinalityRule.getUpperLimit())
				&& quantity >= cardinalityRule.getLowerLimit());
	}

	@Override
	public String getValidationMessage(final TmaBundledProdOfferOptionModel cardinalityRule,
			final TmaProductOfferingModel productOffering)
	{
		if (cardinalityRule == null || productOffering == null)
		{
			return Strings.EMPTY;
		}

		if (cardinalityRule.getUpperLimit() != null)
		{
			final String messageProperty = String
					.join(SEPARATOR, StringUtils.lowerCase(cardinalityRule.getItemtype()), VALIDATION_MESSAGE, MIN_AND_MAX);
			return getL10NService().getLocalizedString(messageProperty,
					new Object[] { productOffering.getName(), cardinalityRule.getLowerLimit(), cardinalityRule.getUpperLimit() });
		}

		final String messageProperty = String
				.join(SEPARATOR, StringUtils.lowerCase(cardinalityRule.getItemtype()), VALIDATION_MESSAGE, MIN);
		return getL10NService()
				.getLocalizedString(messageProperty, new Object[] { productOffering.getName(), cardinalityRule.getLowerLimit() });
	}

	@Override
	public void updateCardinalityValidationMessages(final AbstractOrderModel abstractOrder, final EntryGroup entryGroup,
			final List<String> validationMessages)
	{
		getValidationMessagesStrategy().cleanupValidationMessagesOn(abstractOrder, entryGroup);

		if (getValidationMessagesStrategy().shouldUpdateValidationMessages(entryGroup, validationMessages))
		{
			getValidationMessagesStrategy().setValidationMessagesOn(entryGroup, validationMessages);
			getEntryGroupService().updateEntryGroup(abstractOrder, entryGroup);
		}
	}

	@Override
	public void updateCardinalityValidationMessages(final AbstractOrderEntryModel entry, final List<String> validationMessages)
	{
		getValidationMessagesStrategy().cleanupValidationMessagesOn((CartEntryModel) entry);

		if (getValidationMessagesStrategy().shouldUpdateValidationMessages((CartEntryModel) entry, validationMessages))
		{
			getValidationMessagesStrategy().setValidationMessagesOn((CartEntryModel) entry, validationMessages);
		}
	}

	@Override
	public boolean verifyCardinality(final TmaBundledProductOfferingModel bpo,
			final Map<TmaProductOfferingModel, Integer> children)
	{
		validateParameterNotNull(bpo, BPO_CAN_NOT_BE_NULL);
		validateParameterNotNull(children, BPO_CHILDREN_CAN_NOT_BE_NULL);

		return children.keySet().stream().filter(productOfferingModel ->
				!validate(getPoService().getBundledProdOfferOptionFor(bpo, productOfferingModel).get(),
						children.get(productOfferingModel))).findAny().isEmpty();
	}

	/**
	 * @deprecated since 2102 - use {@link #verifyCardinality(Collection AbstractOrderEntryModel, AbstractOrderEntryModel, TmaBundledProductOfferingModel, TmaBundledProdOfferOptionModel)} instead
	 */
	@Deprecated(since = "2102")
	protected List<String> verifyCardinality(final List<AbstractOrderEntryModel> entries,
			final TmaBundledProductOfferingModel bundledProductOffering, final TmaBundledProdOfferOptionModel parentCardinalityRule)
	{
		final List<String> validationMessages = new ArrayList<>();

		bundledProductOffering.getChildren().forEach((TmaProductOfferingModel productOffering) -> {
			final Optional<TmaBundledProdOfferOptionModel> cardinalityRule = getPoService()
					.getBundledProdOfferOptionFor(bundledProductOffering, productOffering);
			final TmaBundledProdOfferOptionModel rule = cardinalityRule.orElse(parentCardinalityRule);

			if (productOffering instanceof TmaBundledProductOfferingModel)
			{
				validationMessages.addAll(verifyBpoCardinality(entries, (TmaBundledProductOfferingModel) productOffering, rule));
				return;
			}

			final long quantityForPo = getAbstractOrderEntryService().getPoQuantity(entries, productOffering);
			if (rule != null && !validate(rule, quantityForPo))
			{
				validationMessages.add(getValidationMessage(rule, productOffering));
			}

		});

		return validationMessages;
	}

	protected Map<AbstractOrderEntryModel, List<String>> verifyCardinality(final Collection<AbstractOrderEntryModel> entries,
			final AbstractOrderEntryModel parentEntry, final TmaBundledProductOfferingModel bundledProductOffering,
			final TmaBundledProdOfferOptionModel parentCardinalityRule)
	{
		final Map<AbstractOrderEntryModel, List<String>> validationMessages = new HashMap<>();

		bundledProductOffering.getChildren().forEach((TmaProductOfferingModel productOffering) -> {
			final Optional<TmaBundledProdOfferOptionModel> cardinalityRule = getPoService()
					.getBundledProdOfferOptionFor(bundledProductOffering, productOffering);
			final TmaBundledProdOfferOptionModel rule = cardinalityRule.orElse(parentCardinalityRule);

			if (productOffering instanceof TmaBundledProductOfferingModel)
			{
				addValidationMessageToMap(
						verifyBpoCardinality(entries, parentEntry, (TmaBundledProductOfferingModel) productOffering, rule),
						validationMessages);
				return;
			}

			final List<String> validationMessagesForEntry = getValidationMessagesForEntry(entries, productOffering, rule);
			if (CollectionUtils.isNotEmpty(validationMessagesForEntry))
			{
				addValidationMessageToMap(validationMessages, parentEntry, validationMessagesForEntry);
			}
		});

		return validationMessages;
	}

	/**
	 * Validates the cardinality rules for the provided entries and bundled product offering.
	 *
	 * @param entries
	 * 		The child entries
	 * @param parentEntry
	 * 		The parent entry of the child entries
	 * @param bundledProductOffering
	 * 		The bundled product offering
	 * @param rule
	 * 		The cardinality rule applied
	 * @return List of validation messages for the entries
	 */
	protected Map<AbstractOrderEntryModel, List<String>> verifyBpoCardinality(final Collection<AbstractOrderEntryModel> entries,
			final AbstractOrderEntryModel parentEntry, final TmaBundledProductOfferingModel bundledProductOffering,
			final TmaBundledProdOfferOptionModel rule)
	{
		final Map<AbstractOrderEntryModel, List<String>> validationMessages = new HashMap<>();

		final List<String> validationMessagesForEntry = getValidationMessagesForEntry(entries, bundledProductOffering, rule);
		if (CollectionUtils.isNotEmpty(validationMessagesForEntry))
		{
			addValidationMessageToMap(validationMessages, parentEntry, validationMessagesForEntry);
		}

		final List<AbstractOrderEntryModel> entriesForPo = entries.stream()
				.filter((AbstractOrderEntryModel entry) -> bundledProductOffering.getCode().equals(entry.getProduct().getCode()))
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(entriesForPo))
		{
			return validationMessages;
		}

		entriesForPo.forEach((AbstractOrderEntryModel entry) -> validationMessages
				.putAll(verifyCardinality(entry.getChildEntries(), entry, bundledProductOffering, rule)));

		return validationMessages;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private List<String> verifyBpoCardinality(final List<AbstractOrderEntryModel> entries,
			final TmaBundledProductOfferingModel bundledProductOffering, final TmaBundledProdOfferOptionModel cardinalityRule)
	{
		if ((cardinalityRule != null && cardinalityRule.getLowerLimit() != 0) || isAnyChildPoInCart(entries,
				bundledProductOffering))
		{
			return verifyCardinality(entries, bundledProductOffering, cardinalityRule);
		}
		return Collections.emptyList();
	}

	private List<String> getValidationMessagesForEntry(final Collection<AbstractOrderEntryModel> entries,
			final TmaProductOfferingModel productOffering, final TmaBundledProdOfferOptionModel rule)
	{
		final long quantityForPo = getAbstractOrderEntryService()
				.getPoQuantity((List<AbstractOrderEntryModel>) entries, productOffering);
		if (rule != null && !validate(rule, quantityForPo))
		{
			return Lists.newArrayList(getValidationMessage(rule, productOffering));
		}
		return Collections.emptyList();
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private boolean isAnyChildPoInCart(final List<AbstractOrderEntryModel> entries,
			final TmaBundledProductOfferingModel bundledProductOffering)
	{
		final List<String> poCodesInEntryList = entries.stream()
				.map((AbstractOrderEntryModel entry) -> entry.getProduct().getCode()).collect(Collectors.toList());
		final List<TmaSimpleProductOfferingModel> childProductOfferings = getPoService()
				.getSpoListForBpo(bundledProductOffering);
		final List<String> poCodesInChildPoList = childProductOfferings.stream().map(TmaProductOfferingModel::getCode)
				.collect(Collectors.toList());

		return poCodesInEntryList.stream().distinct().anyMatch(poCodesInChildPoList::contains);
	}

	private void addValidationMessageToMap(final Map<AbstractOrderEntryModel, List<String>> validationMessagesMap,
			final AbstractOrderEntryModel parentEntry, final List<String> validationMessages)
	{
		if (validationMessagesMap.get(parentEntry) != null)
		{
			validationMessagesMap.get(parentEntry).addAll(validationMessages);
			return;
		}

		validationMessagesMap.put(parentEntry, validationMessages);
	}

	private void addValidationMessageToMap(final Map<AbstractOrderEntryModel, List<String>> source,
			final Map<AbstractOrderEntryModel, List<String>> target)
	{
		if (MapUtils.isEmpty(source))
		{
			return;
		}
		source.entrySet().forEach((Map.Entry<AbstractOrderEntryModel, List<String>> validationMap) -> {
			if (CollectionUtils.isEmpty(validationMap.getValue()))
			{
				return;
			}

			if (target.get(validationMap.getKey()) != null)
			{
				target.get(validationMap.getKey()).addAll(validationMap.getValue());
				return;
			}

			target.put(validationMap.getKey(), validationMap.getValue());
		});
	}

	private Map<AbstractOrderEntryModel, List<String>> createEmptyValidations(final AbstractOrderEntryModel entry)
	{
		final Map<AbstractOrderEntryModel, List<String>> validationMessages = new HashMap<>();

		if (entry == null)
		{
			return Collections.emptyMap();
		}

		validationMessages.put(entry, Collections.emptyList());

		if (CollectionUtils.isEmpty(entry.getChildEntries()))
		{
			return validationMessages;
		}

		entry.getChildEntries()
				.forEach((AbstractOrderEntryModel childEntry) -> validationMessages.putAll(createEmptyValidations(childEntry)));

		return validationMessages;
	}

	protected TmaPoService getPoService()
	{
		return poService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected TmaEntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}

	protected TmaValidationMessagesStrategy getValidationMessagesStrategy()
	{
		return validationMessagesStrategy;
	}

	protected L10NService getL10NService()
	{
		return l10NService;
	}
}
