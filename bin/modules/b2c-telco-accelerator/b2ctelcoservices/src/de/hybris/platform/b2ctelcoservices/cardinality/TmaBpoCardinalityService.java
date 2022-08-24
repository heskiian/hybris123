/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.cardinality;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;

import java.util.List;
import java.util.Map;


/**
 * Service for operations related to the {@link TmaBundledProdOfferOptionModel}.
 *
 * @since 2011
 */
public interface TmaBpoCardinalityService
{

	/**
	 * Validated the cardinality rules for the provided entry group.
	 *
	 * @param abstractOrder
	 * 		The cart or order
	 * @param entryGroup
	 * 		The entry group
	 * @return List of validation messages
	 * @deprecated since 2102 - use {@link #verifyCardinality(AbstractOrderEntryModel)} instead
	 */
	@Deprecated(since = "2102")
	List<String> verifyCardinality(final AbstractOrderModel abstractOrder, final EntryGroup entryGroup);

	/**
	 * Validated the cardinality rules for the provided entry.
	 *
	 * @param entry
	 * 		The entry
	 * @return List of validation messages for the entries
	 */
	Map<AbstractOrderEntryModel, List<String>> verifyCardinality(final AbstractOrderEntryModel entry);

	/**
	 * Validates the cardinality rules for the provided abstract order.
	 *
	 * @param abstractOrder
	 * 		The cart or order
	 * @deprecated since 2102 - use {@link #verifyCardinality(AbstractOrderEntryModel)} instead
	 */
	@Deprecated(since = "2102")
	Map<EntryGroup, List<String>> verifyCardinality(final AbstractOrderModel abstractOrder);

	/**
	 * Validates the cardinality rules for the provided abstract order.
	 *
	 * @param abstractOrder
	 * 		The cart or order
	 * @return List of validation messages for the entry
	 */
	Map<AbstractOrderEntryModel, List<String>> verifyCardinalityForOrder(final AbstractOrderModel abstractOrder);

	/**
	 * Checks if provided quantity meets the requirements of the cardinality rule.
	 *
	 * @param cardinalityRule
	 * 		The bundled product offering rule
	 * @param quantity
	 * 		The product offering quantity
	 * @return True if product offering fulfills the cardinality rules, otherwise false
	 */
	boolean validate(final TmaBundledProdOfferOptionModel cardinalityRule, final long quantity);

	/**
	 * Returns cardinality validation message for given Product Offering based on the details of the Bundled Product Offering Option.
	 *
	 * @param bundledProdOfferOption
	 * 		The bundled product offering option
	 * @param productOffering
	 * 		The product Offering
	 * @return The validation message
	 */
	String getValidationMessage(final TmaBundledProdOfferOptionModel bundledProdOfferOption,
			final TmaProductOfferingModel productOffering);

	/**
	 * Updates the cardinality validation messages on the entry group with the provided messages.
	 *
	 * @param abstractOrder
	 * 		The cart or order
	 * @param entryGroup
	 * 		The entry group
	 * @param validationMessages
	 * 		The validation messages
	 * @deprecated since 2102 - use {@link #updateCardinalityValidationMessages(AbstractOrderEntryModel, List String)} instead
	 */
	@Deprecated(since = "2102")
	void updateCardinalityValidationMessages(final AbstractOrderModel abstractOrder, final EntryGroup entryGroup,
			final List<String> validationMessages);

	/**
	 * Updates the cardinality validation messages on the entry group with the provided messages.
	 *
	 * @param entry
	 * 		The entry
	 * @param validationMessages
	 * 		The validation messages
	 */
	void updateCardinalityValidationMessages(final AbstractOrderEntryModel entry, final List<String> validationMessages);

	/**
	 * Verifies if bundled product offering's children have valid cardinality
	 *
	 * @param bpo
	 * 		the bundled product offering
	 * @param children
	 * 		the children product offerings for which the cardinality should be fulfilled
	 * @return true if all the children product offerings fulfills the cardinality rules, otherwise false
	 */
	boolean verifyCardinality(final TmaBundledProductOfferingModel bpo, final Map<TmaProductOfferingModel, Integer> children);
}
