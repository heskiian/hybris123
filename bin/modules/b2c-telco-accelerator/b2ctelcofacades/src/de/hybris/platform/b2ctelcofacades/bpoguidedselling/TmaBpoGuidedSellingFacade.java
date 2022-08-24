/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.bpoguidedselling;


import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardData;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingStepData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Map;


/**
 * Facade is responsible for getting all information for Guided Selling Feature for BPO
 *
 * @since 6.7
 */

public interface TmaBpoGuidedSellingFacade
{

	/**
	 * Initiate a new or refine an existing search for bundle products based on BPO id and Group id.
	 *
	 * @param groupId
	 *           The calculated Group of {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel}
	 *           of the root Bundled Product Offering, from where
	 *           {@link de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel} is added
	 * @param pageableData
	 *           {@link de.hybris.platform.commerceservices.search.pagedata.PageableData} the pageable data
	 * @param searchQuery
	 *           the search query for the Search page data
	 * @param bpoCode
	 *           the BPO code {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel#CODE} for
	 *           which guided selling journey is started
	 * @param groupNumber
	 *           The group identifying the entry or entry group to which
	 *           {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel} is added to the cart,
	 *           can be empty
	 * @return {@link ProductSearchPageData}
	 */
	ProductSearchPageData<SearchStateData, ProductData> getProductsAssociatedWithGroups(String groupId, PageableData pageableData,
			String searchQuery, String bpoCode, Integer groupNumber);

	/**
	 * fetch the calculated steps to configure a BPO
	 *
	 * @param bpoCode
	 *           the bpoCode for which Journey needs to be calculated
	 *
	 * @return the map with {@link de.hybris.platform.b2ctelcofacades.data.GuidedSellingStepData} as value and calculated
	 *         group Id as keys
	 */
	Map<String, GuidedSellingStepData> getCalculatedStepsForBPO(String bpoCode);

	/**
	 * Fetch the dash board data For the BPO guided Selling Journey
	 *
	 * @param groupNumber
	 *           The group identifying the entry or entry group to which
	 *           {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel} is added to the cart ,
	 *           can be null if no entry is added for a particular journey
	 *
	 * @param bpoCode
	 *           the bpoCode for which Journey needs to be calculated
	 *
	 * @return {@link de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardData} The guided selling dash board
	 *         data for a the given BPO
	 */
	GuidedSellingDashBoardData getDashBoardForBPO(String bpoCode, Integer groupNumber);

	/**
	 * Fetch the first step for given BPO Code
	 *
	 * @param bpoCode
	 *           {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel} code
	 * @return step name Return first key of hash map if Given bpoCode
	 *         {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel} has list of child's or
	 *         list of groups otherwise return the {@link ModelNotFoundException} if null empty is return by given BPO
	 *         code
	 */
	String getFirstStepForBPO(String bpoCode);

	/**
	 * validate and Fetch the EntryGroup data for the corresponding BPO
	 *
	 * @param bpoCode
	 *           the bpoCode for which Journey needs to be calculated
	 * @param groupNumber
	 *           The group identifying the entry or entry group to which
	 *           {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel} is added to the cart ,
	 *           can be null if no entry is added for a particular journey
	 * @return {@link de.hybris.platform.core.order.EntryGroup} The EntryGroup
	 *         data for a the given BPO
	 */
	EntryGroup validateAndGetBPOEntryGroup(String bpoCode, Integer groupNumber);

	/**
	 * Fetch the dash board data For the BPO guided Selling Journey for the corresponding groupId
	 *
	 * @param groupNumber
	 *           The {@link de.hybris.platform.core.order.EntryGroup} to which
	 *           {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel} is added to the cart ,
	 *           can be null if no entry is added for a particular journey
	 *
	 * @param bpoCode
	 *           the bpoCode for which Journey needs to be calculated
	 * @param groupId
	 *           the bpo groupId for which Journey needs to be calculated
	 *
	 * @return {@link de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardData} The guided selling dash board
	 *         data for a the given BPO
	 */
	GuidedSellingDashBoardData getDashBoardForBPO(String bpoCode, String groupId, Integer groupNumber);
}
