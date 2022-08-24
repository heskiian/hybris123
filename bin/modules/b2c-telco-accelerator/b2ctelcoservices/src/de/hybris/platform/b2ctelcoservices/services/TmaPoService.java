/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcofacades.data.TmaPriceContextData;
import de.hybris.platform.b2ctelcoservices.data.TmaProductOfferingSearchContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOperationalProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * Service handling business logic around {@link TmaProductOfferingModel}.
 *
 * @since 6.7
 */
public interface TmaPoService
{
	/**
	 * Returns a {@link TmaProductOfferingModel} for the given code.
	 *
	 * @param code
	 * 		identifier of {@link TmaProductOfferingModel}
	 * @return {@link TmaProductOfferingModel}
	 */
	TmaProductOfferingModel getPoForCode(final String code);

	/**
	 * Adds a new {@link TmaProductOfferingModel} child to a {@link TmaBundledProductOfferingModel} parent.
	 *
	 * @param parent
	 * 		bundled product offering under which the child is added
	 * @param child
	 * 		child to be added
	 */
	void addPoChildToParent(final TmaBundledProductOfferingModel parent, final TmaProductOfferingModel child);

	/**
	 * Removes the link between {@link TmaProductOfferingModel} child and {@link TmaBundledProductOfferingModel} parent.
	 *
	 * @param parent
	 * 		bundled product offering from which the child is removed
	 * @param child
	 * 		product offering child to be removed
	 */
	void removePoChildFromParent(final TmaBundledProductOfferingModel parent, final TmaProductOfferingModel child);

	/**
	 * Returns all parents for the given product offering.
	 *
	 * @param poModel
	 * 		product offering
	 * @return {@link Collection} of {@link TmaBundledProductOfferingModel}
	 */
	Collection<TmaBundledProductOfferingModel> getAllParents(final TmaProductOfferingModel poModel);

	/**
	 * Searches for the the group next to the group which contains the PO given as parameter; It returns: the group with
	 * index 0, if PO given as parameter is in a group with index higher than 0 or, the group with index 1, if PO given
	 * as parameter is in a group with index 0 or, empty {@link Optional} if no group is found.
	 *
	 * @param bpo
	 * 		the {@link TmaBundledProductOfferingModel}
	 * @param po
	 * 		the {@link TmaProductOfferingModel}
	 * @return {@link Optional} of {@link TmaProductOfferingGroupModel}
	 */
	Optional<TmaProductOfferingGroupModel> getGroupNextToProductGroup(final TmaBundledProductOfferingModel bpo,
			final TmaProductOfferingModel po);

	/**
	 * Verifies if the given {@link TmaBundledProductOfferingModel} parent is a valid parent for the given product
	 * offering. A valid parent it not necessary a first direct parent, it can also be a parent of one of product
	 * parents.
	 *
	 * @param product
	 * 		the given product offering against the checks are made
	 * @param parent
	 * 		the given parent to be check if is valid
	 * @return true if all Set elements are valid product parents codes, false otherwise
	 */
	boolean isValidParent(final TmaProductOfferingModel product, final TmaBundledProductOfferingModel parent);

	/**
	 * Returns a {@link TmaBundledProductOfferingModel} for the given code.
	 *
	 * @param code
	 * 		identifier of {@link TmaBundledProductOfferingModel}
	 * @return {@link TmaBundledProductOfferingModel}
	 */
	TmaBundledProductOfferingModel getBpoForCode(final String code);

	/**
	 * Retrieves the {@link TmaProductOfferingGroupModel} for a given product inside the given
	 * {@link TmaBundledProductOfferingModel} parent.
	 *
	 * @param productOffering
	 * 		the {@link TmaProductOfferingModel} whose product offering group will be identified
	 * @param parent
	 * 		the {@link TmaBundledProductOfferingModel} used for filtering the product offering groups
	 * @return the {@link TmaProductOfferingGroupModel} configured for the given product if any
	 */
	Optional<TmaProductOfferingGroupModel> getOfferingGroupForPoAndBpo(final TmaProductOfferingModel productOffering,
			final TmaBundledProductOfferingModel parent);

	/**
	 * Verifies if the given product is eligible for the given process type. Eligibility is performed considering the
	 * process types defined on the subscription price plans of the given product. If the BPO is given then the checks
	 * are performed against price plan overrides of the BPO where the product is defined as affected product.
	 *
	 * @param processType
	 * 		the process type that is going to be verified
	 * @param product
	 * 		the product checked if eligible for given process type
	 * @param bpo
	 * 		the BPO where product is part of
	 * @return true/ false depending on the eligibility of the given product for the process type
	 */
	boolean isPoEligibleForProcessType(final TmaProcessType processType, final TmaProductOfferingModel product,
			final TmaBundledProductOfferingModel bpo);

	/**
	 * Returns a {@link TmaProductOfferingModel} for the given code and prices of the {@link TmaProductOfferingModel}
	 * based on the {@link TmaPriceContextData}
	 *
	 * @param code
	 * 		identifier of {@link TmaProductOfferingModel}
	 * @param priceContext
	 * 		priceContext {@link TmaPriceContext} to retrieve prices of the {@link TmaProductOfferingModel}
	 * @return {@link TmaProductOfferingModel}
	 */
	TmaProductOfferingModel getPoForCodeAndPriceContext(String code, TmaPriceContext priceContext);

	/**
	 * Finds the {@link TmaProductOfferingModel} for a given collection {@link CatalogVersionModel} and
	 * {@link TmaProductOfferingSearchContext}
	 *
	 * @param catalogVersions
	 * 		catalog versions used to query for products
	 * @param productOfferingSearchContext
	 * 		product search context object stores all attributes for search
	 * @return page of {@link TmaProductOfferingModel} wrapped on {@link SearchResult}
	 * @deprecated since 1911 - use getAllProducts method of ExportProductService instead
	 */
	@Deprecated(since = "1911", forRemoval = true)
	SearchPageData<TmaProductOfferingModel> getProductOfferings(final Collection<CatalogVersionModel> catalogVersions,
			final TmaProductOfferingSearchContext productOfferingSearchContext);

	/**
	 * Computes and returns the configurable {@link TmaProductSpecCharacteristicModel} of a given product.
	 *
	 * @param productOffering
	 * 		the product offering
	 * @return the configurable characteristics
	 */
	Set<TmaProductSpecCharacteristicModel> getConfigurableProductSpecCharacteristics(
			final TmaProductOfferingModel productOffering);

	/**
	 * Returns the operational product corresponding to the specified action.
	 *
	 * @param action
	 * 		the action that should be supported by the operational product.
	 * @return The first operational product found that supports the given action, if any.
	 */
	Optional<TmaOperationalProductOfferingModel> getOperationalProductOffering(final TmaSubscribedProductAction action);

	/**
	 * This method checks if the given cart action requires an operational product.
	 *
	 * @param action
	 * 		the input action to be checked.
	 * @return true if an operational Po is used; false otherwise
	 */
	boolean isServiceRequestAction(final TmaSubscribedProductAction action);

	/**
	 * Returns a product offering for the given product code and cart action.
	 *
	 * @param productCode
	 * 		the code used for getting a PO if the action is keep
	 * @param cartAction
	 * 		the cart action
	 * @return the product offering for the product code and cart action.
	 * @throws ModelNotFoundException
	 * 		if no product offering is found.
	 * @throws IllegalArgumentException
	 * 		if the cartAction is invalid.
	 */
	ProductModel getProductOffering(final String productCode, final TmaSubscribedProductAction cartAction);

	/**
	 * Return the list of {@link TmaProductOfferingModel} based on product code
	 *
	 * @param code
	 * 		identifier of {@link TmaProductOfferingModel}
	 * @return {@link TmaProductOfferingModel}
	 */
	List<TmaProductOfferingModel> getProductsForCode(String code);

	/**
	 * Returns the set of {@link TmaProductOfferingModel} based on key and value in map where key is the property name
	 * and the value is the pattern value of the property
	 *
	 * @param propertyPattern
	 * 		map which has property as key and pattern as value
	 * @return Set of {@link TmaProductOfferingModel}
	 */
	Set<TmaProductOfferingModel> getProductOfferingsByProperties(Map<String, String> propertyPattern);

	/**
	 * Returns the set of @{link TmaProductOfferingGroupModel} based on key and value in map where key is the property
	 * name and the value is the pattern value of the property
	 *
	 * @param propertyPattern
	 * 		map which has propertyPattern as key and pattern as value
	 * @return Set of {@link TmaProductOfferingGroupModel}
	 */
	Set<TmaProductOfferingGroupModel> getProductOfferingGroupsByProperties(Map<String, String> propertyPattern);

	/**
	 * Returns the list of all child product offering {@link TmaSimpleProductOfferingModel} for a given bundled product
	 * offering.
	 *
	 * @param bundleProductOffering
	 * 		the bundle product offering
	 * @return the child spos of the product offering
	 */
	List<TmaSimpleProductOfferingModel> getSpoListForBpo(final TmaBundledProductOfferingModel bundleProductOffering);

	/**
	 * Returns product offering list for corresponding catalog version
	 *
	 * @param catalogVersion
	 * 		for which product offerings will be retrieved
	 * @return List of {@link TmaProductOfferingModel}
	 */
	List<TmaProductOfferingModel> getProductOfferings(CatalogVersionModel catalogVersion);

	/**
	 * Returns the bundled product offering option part of the provided bpo which is referred to the provided product offering.
	 *
	 * @param bundledProductOffering
	 * 		The Bundled Product Offering
	 * @param productOffering
	 * 		The product offering
	 * @return The {@link TmaBundledProdOfferOptionModel} from the bpo which refers to the product offering
	 */
	Optional<TmaBundledProdOfferOptionModel> getBundledProdOfferOptionFor(
			final TmaBundledProductOfferingModel bundledProductOffering,
			final TmaProductOfferingModel productOffering);

	/**
	 * Returns the list of BPOs between the provided PO and the target BPO.
	 *
	 * @param currentPo
	 * 		The product offering
	 * @param targetBpo
	 * 		The target bundled product offering
	 * @return List of {@link TmaBundledProductOfferingModel}
	 */
	List<TmaBundledProductOfferingModel> getIntermediateBpos(final TmaProductOfferingModel currentPo,
			final TmaBundledProductOfferingModel targetBpo);

	/**
	 * Verifies if the given child is eligible to be attached on the given parent. Eligible means that child must be a SPO either
	 * a Fixed BPO if the parent is a Fixed BPO
	 *
	 * @param child
	 * 		The PO which is going to be checked if it is eligible for attaching on a Fixed BPO
	 * @param parent
	 * 		The BPO depending on we know if the child PO is eligible or not
	 * @return true/ false depending on the eligibility of the given product offering
	 */
	boolean isValidChild(final TmaProductOfferingModel child, final TmaBundledProductOfferingModel parent);

	/**
	 * Returns the list of all children product offerings {@link TmaSimpleProductOfferingModel} and
	 * {@link TmaFixedBundledProductOfferingModel} for a given bundled product offering.
	 *
	 * @param bundledProductOffering
	 * 		the bundle product offering
	 * @return the child spos or fixed bpos of the product offering
	 */
	List<TmaProductOfferingModel> getSposAndFixedBposFor(final TmaBundledProductOfferingModel bundledProductOffering);

	/**
	 * Gets the configurable product spec characteristics value uses.
	 *
	 * @param productOffering the product offering
	 * @return the configurable product spec characteristics value uses
	 */
	Set<TmaProductSpecCharValueUseModel> getConfigurableProductSpecCharValueUses(final TmaProductOfferingModel productOffering);
}
