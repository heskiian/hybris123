/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.bpoguidedselling.impl;

import de.hybris.platform.b2ctelcofacades.bpoguidedselling.TmaBpoGuidedSellingFacade;
import de.hybris.platform.b2ctelcofacades.configurableguidedselling.EntryGroupFacade;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardData;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardPopulatorParameters;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingStepData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaCartService;
import de.hybris.platform.b2ctelcoservices.order.impl.DefaultTmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ObjectUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation for {@link TmaBpoGuidedSellingFacade}.
 *
 * @since 6.7
 */
public class DefaultTmaBpoGuidedSellingFacade implements TmaBpoGuidedSellingFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaBpoGuidedSellingFacade.class.getName());
	private static final String DEFAULT = "_default";

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private Populator<GuidedSellingDashBoardPopulatorParameters, GuidedSellingDashBoardData> tmaBpoGuidedSellingDashBoardDataPopulator;
	private Populator<GuidedSellingDashBoardPopulatorParameters, GuidedSellingDashBoardData> tmaGuidedSellingDashBoardDataPopulator;
	private TmaPoService tmaPoService;
	private ProductSearchFacade<ProductData> productSearchFacade;
	private EntryGroupFacade entryGroupFacade;
	private TmaCartService cartService;
	private DefaultTmaAbstractOrderEntryService abstractOrderEntryService;

	public DefaultTmaBpoGuidedSellingFacade(
			Populator<GuidedSellingDashBoardPopulatorParameters, GuidedSellingDashBoardData> tmaGuidedSellingDashBoardDataPopulator,
			TmaPoService tmaPoService,
			ProductSearchFacade<ProductData> productSearchFacade,
			EntryGroupFacade entryGroupFacade, TmaCartService cartService,
			DefaultTmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.tmaGuidedSellingDashBoardDataPopulator = tmaGuidedSellingDashBoardDataPopulator;
		this.tmaPoService = tmaPoService;
		this.productSearchFacade = productSearchFacade;
		this.entryGroupFacade = entryGroupFacade;
		this.cartService = cartService;
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public Map<String, GuidedSellingStepData> getCalculatedStepsForBPO(final String bpoCode)
	{
		validateParameterNotNull(bpoCode, "BPO Code must not be null!");
		final TmaBundledProductOfferingModel tmaBundledProductOfferingModel = getTmaPoService().getBpoForCode(bpoCode);
		if (tmaBundledProductOfferingModel instanceof TmaFixedBundledProductOfferingModel)
		{
			throw new UnsupportedOperationException("TmaProductOfferingModel with code " + bpoCode + " not found!");
		}
		final LinkedMap<String, GuidedSellingStepData> bpoGuidedSellingStepData = new LinkedMap<>();
		return getBPOGuidedSellingStepData(tmaBundledProductOfferingModel, bpoGuidedSellingStepData);
	}

	/**
	 * {@inheritDoc}
	 * It retrieves the guided selling dashboard data for the BPO and group number provided. The group number can be the ID of
	 * the entry group (if exists) or the parent entry (if exists).
	 */
	@Override
	public GuidedSellingDashBoardData getDashBoardForBPO(final String bpoCode, final Integer groupNumber)
	{
		validateParameterNotNull(bpoCode, "BPO Code  must not be null!");
		final GuidedSellingDashBoardPopulatorParameters params = new GuidedSellingDashBoardPopulatorParameters();
		final LinkedMap<String, GuidedSellingStepData> bpoGuidedSellingStepData = (LinkedMap<String, GuidedSellingStepData>) getCalculatedStepsForBPO(
				bpoCode);
		params.setStepData((new ArrayList<>(bpoGuidedSellingStepData.values())));

		if (null != groupNumber)
		{
			populateParamsWithEntryGroup(bpoCode, groupNumber, params);
			populateParamsWithParentEntry(groupNumber, params);
		}

		final GuidedSellingDashBoardData guidedSellingDashBoardData = new GuidedSellingDashBoardData();
		getTmaGuidedSellingDashBoardDataPopulator().populate(params, guidedSellingDashBoardData);
		return guidedSellingDashBoardData;
	}

	/**
	 * {@inheritDoc}
	 * It retrieves the guided selling dashboard data for the BPO, group ID and group number provided. The group number can be the
	 * ID of
	 * the entry group (if exists) or the parent entry (if exists).
	 */
	@Override
	public GuidedSellingDashBoardData getDashBoardForBPO(final String bpoCode, final String groupId, final Integer groupNumber)
	{
		validateParameterNotNull(bpoCode, "BPO Code  must not be null!");
		final GuidedSellingDashBoardPopulatorParameters params = new GuidedSellingDashBoardPopulatorParameters();
		final LinkedMap<String, GuidedSellingStepData> bpoGuidedSellingStepData = (LinkedMap<String, GuidedSellingStepData>) getCalculatedStepsForBPO(
				bpoCode);
		final boolean isValidGroupId = bpoGuidedSellingStepData.containsKey(groupId);

		if (!isValidGroupId)
		{
			throw new IllegalArgumentException("Group Entered '" + groupId + "' does not exist");
		}

		final List<GuidedSellingStepData> guidedSellingStepDataList = new ArrayList<>();
		guidedSellingStepDataList.add(bpoGuidedSellingStepData.get(groupId));
		params.setStepData(guidedSellingStepDataList);

		if (null != groupNumber)
		{
			populateParamsWithEntryGroup(bpoCode, groupNumber, params);
			populateParamsWithParentEntry(groupNumber, params);
		}

		final GuidedSellingDashBoardData guidedSellingDashBoardData = new GuidedSellingDashBoardData();
		getTmaGuidedSellingDashBoardDataPopulator().populate(params, guidedSellingDashBoardData);
		return guidedSellingDashBoardData;
	}

	@Override
	public ProductSearchPageData<SearchStateData, ProductData> getProductsAssociatedWithGroups(final String groupId,
			final PageableData pageableData, final String searchQuery, final String bpoId, final Integer groupNumber)
	{
		validateParameterNotNull(groupId, "Groups must not be null!");
		String urlPrefix = "/bpo/configure/" + bpoId + "/" + groupId + "/" + "?q=";
		if (null != groupNumber)
		{
			urlPrefix = "/bpo/configure/" + bpoId + "/" + groupId + "/" + groupNumber + "/" + "?q=";
		}
		final SearchStateData searchState = new SearchStateData();
		patchQuery(searchState, searchQuery, groupId);
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = getProductSearchFacade().textSearch(searchState,
				pageableData);
		patchURLs(urlPrefix, searchPageData);
		return searchPageData;
	}

	@Override
	public String getFirstStepForBPO(final String bpoCode)
	{
		validateParameterNotNull(bpoCode, "Bundle Product Offering code must not be null!");

		final LinkedMap<String, GuidedSellingStepData> bundleProductOfferingGuidedSellingStepData = (LinkedMap<String, GuidedSellingStepData>) getCalculatedStepsForBPO(
				bpoCode);

		if (!bundleProductOfferingGuidedSellingStepData.isEmpty())
		{
			return bundleProductOfferingGuidedSellingStepData.firstKey();
		}
		else
		{
			throw new ModelNotFoundException(String.format("TmaProductOfferingModel with code '%s' not found!", bpoCode));
		}
	}

	@Override
	public EntryGroup validateAndGetBPOEntryGroup(final String bpoCode, final Integer groupNumber)
	{
		EntryGroup rootEntryGroup = null;
		if (!ObjectUtils.isEmpty(groupNumber) && StringUtils.isNotBlank(bpoCode))
		{
			rootEntryGroup = getEntryGroupFacade().getCurrentEntryGroup(groupNumber);
			if (!bpoCode.equalsIgnoreCase(rootEntryGroup.getExternalReferenceId()))
			{
				throw new IllegalArgumentException("BPO '" + bpoCode + "' not found with the group number " + groupNumber);
			}
		}
		return rootEntryGroup;
	}

	protected void patchQuery(final SearchStateData searchState, final String searchQuery, final String groupId)
	{
		validateParameterNotNull(groupId, "groups cannot be null");

		final StringBuilder newSearchQuery = new StringBuilder(100);
		if (StringUtils.isEmpty(searchQuery))
		{
			newSearchQuery.append("*:");
		}
		else if (searchQuery.charAt(0) == ':' || searchQuery.charAt(0) == '*')
		{
			newSearchQuery.append(searchQuery);
		}
		newSearchQuery.append(":productOfferingGroups:");
		newSearchQuery.append(groupId);
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(newSearchQuery.toString());
		searchState.setQuery(searchQueryData);
	}

	/**
	 * Query Builder for fetching bundle Data from solr.
	 *
	 * @param urlPrefix
	 * 		the url prefix
	 * @param searchPageData
	 * 		the search page data
	 * @return the url prefix
	 */
	protected String patchURLs(final String urlPrefix, final ProductSearchPageData<SearchStateData, ProductData> searchPageData)
	{
		for (final BreadcrumbData<SearchStateData> breadcrumbData : searchPageData.getBreadcrumbs())
		{
			breadcrumbData.getRemoveQuery().setUrl(urlPrefix + breadcrumbData.getRemoveQuery().getQuery().getValue());
		}
		for (final FacetData<SearchStateData> facetData : searchPageData.getFacets())
		{
			if (facetData.getTopValues() != null)
			{
				for (final FacetValueData<SearchStateData> facetValuesData : facetData.getTopValues())
				{
					facetValuesData.getQuery().setUrl(urlPrefix + facetValuesData.getQuery().getQuery().getValue());
				}
			}
			for (final FacetValueData<SearchStateData> facetValuesData : facetData.getValues())
			{
				facetValuesData.getQuery().setUrl(urlPrefix + facetValuesData.getQuery().getQuery().getValue());
			}
		}
		searchPageData.getCurrentQuery().setUrl(urlPrefix + searchPageData.getCurrentQuery().getQuery().getValue());
		return urlPrefix;
	}

	private void populateParamsWithParentEntry(final Integer groupNumber, final GuidedSellingDashBoardPopulatorParameters params)
	{
		try
		{
			AbstractOrderEntryModel parentEntry = getAbstractOrderEntryService().getEntryBy(getCartService().getSessionCart(),
					groupNumber);
			params.setParentEntry(parentEntry);
		}
		catch (final RuntimeException e)
		{
			LOG.warn("There was an error in obtaining parent entry with id " + groupNumber, e);
		}
	}

	private void populateParamsWithEntryGroup(final String bpoCode, final Integer groupNumber,
			final GuidedSellingDashBoardPopulatorParameters params)
	{
		try
		{
			final EntryGroup rootEntryGroup = validateAndGetBPOEntryGroup(bpoCode, groupNumber);
			params.setRootGroup(rootEntryGroup);
		}
		catch (final IllegalArgumentException e)
		{
			LOG.debug("There was an error in obtaining entry group with id " + groupNumber, e);
		}
	}

	private Map<String, GuidedSellingStepData> getBPOGuidedSellingStepData(final Object obj,
			final Map<String, GuidedSellingStepData> bpoGuidedSellingStepData)
	{
		if (obj instanceof TmaBundledProductOfferingModel)
		{
			final TmaBundledProductOfferingModel bundledProductOfferingModel = (TmaBundledProductOfferingModel) obj;
			getGuidedSellingStepDataFromBpo(bundledProductOfferingModel, bpoGuidedSellingStepData);
		}
		else if (obj instanceof TmaProductOfferingGroupModel)
		{
			final TmaProductOfferingGroupModel groupModel = (TmaProductOfferingGroupModel) obj;
			getGuidedSellingStepDataFromPog(groupModel, bpoGuidedSellingStepData);
		}
		return bpoGuidedSellingStepData;
	}


	private void getGuidedSellingStepDataFromBpo(final TmaBundledProductOfferingModel bundledProductOfferingModel,
			final Map<String, GuidedSellingStepData> bpoGuidedSellingStepData)
	{
		if (bundledProductOfferingModel.getProductOfferingGroups().isEmpty() && bundledProductOfferingModel.getChildren().isEmpty())
		{
			return;
		}
		final LinkedHashSet<TmaProductOfferingModel> poAssociatedWithGroups = new LinkedHashSet<>();
		final LinkedHashSet<TmaProductOfferingModel> poAssociatedWithBPO = new LinkedHashSet<>(
				bundledProductOfferingModel.getChildren());

		final String groupId = bundledProductOfferingModel.getCode().concat(DEFAULT);

		for (final TmaProductOfferingGroupModel groupModel : bundledProductOfferingModel.getProductOfferingGroups())
		{
			getBPOGuidedSellingStepData(groupModel, bpoGuidedSellingStepData);
			poAssociatedWithGroups.addAll(groupModel.getChildProductOfferings());
		}
		if (!poAssociatedWithBPO.isEmpty() && !poAssociatedWithGroups.isEmpty())
		{
			poAssociatedWithBPO.removeAll(poAssociatedWithGroups);
		}

		for (final TmaProductOfferingModel offeringModel : poAssociatedWithBPO)
		{
			if (offeringModel instanceof TmaSimpleProductOfferingModel && (!bpoGuidedSellingStepData.containsKey(groupId)))
			{
				final GuidedSellingStepData guidedSellingStepdata = new GuidedSellingStepData();
				guidedSellingStepdata.setId(groupId);
				guidedSellingStepdata.setName(bundledProductOfferingModel.getCode());

				bpoGuidedSellingStepData.put(groupId, guidedSellingStepdata);
			}
			else
			{
				getBPOGuidedSellingStepData(offeringModel, bpoGuidedSellingStepData);
			}
		}
	}

	private void getGuidedSellingStepDataFromPog(final TmaProductOfferingGroupModel groupModel,
			final Map<String, GuidedSellingStepData> bpoGuidedSellingStepData)
	{
		for (final TmaProductOfferingModel offeringModel : groupModel.getChildProductOfferings())
		{
			final String groupId = groupModel.getCode();
			if (offeringModel instanceof TmaSimpleProductOfferingModel && (!bpoGuidedSellingStepData.containsKey(groupId)))
			{
				final GuidedSellingStepData guidedSellingStepdata = new GuidedSellingStepData();
				guidedSellingStepdata.setId(groupId);
				guidedSellingStepdata.setName(groupModel.getName());

				bpoGuidedSellingStepData.put(groupId, guidedSellingStepdata);
			}

			else
			{
				getBPOGuidedSellingStepData(offeringModel, bpoGuidedSellingStepData);
			}
		}

	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected Populator<GuidedSellingDashBoardPopulatorParameters, GuidedSellingDashBoardData> getTmaBpoGuidedSellingDashBoardDataPopulator()
	{
		return tmaBpoGuidedSellingDashBoardDataPopulator;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	public void setTmaBpoGuidedSellingDashBoardDataPopulator(
			final Populator<GuidedSellingDashBoardPopulatorParameters, GuidedSellingDashBoardData> tmaBpoGuidedSellingDashBoardDataPopulator)
	{
		this.tmaBpoGuidedSellingDashBoardDataPopulator = tmaBpoGuidedSellingDashBoardDataPopulator;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	public void setTmaPoService(final TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	protected ProductSearchFacade<ProductData> getProductSearchFacade()
	{
		return productSearchFacade;
	}

	public void setProductSearchFacade(
			ProductSearchFacade<ProductData> productSearchFacade)
	{
		this.productSearchFacade = productSearchFacade;
	}

	protected EntryGroupFacade getEntryGroupFacade()
	{
		return entryGroupFacade;
	}

	public void setEntryGroupFacade(EntryGroupFacade entryGroupFacade)
	{
		this.entryGroupFacade = entryGroupFacade;
	}

	protected DefaultTmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}

	protected TmaCartService getCartService()
	{
		return cartService;
	}

	protected Populator<GuidedSellingDashBoardPopulatorParameters, GuidedSellingDashBoardData> getTmaGuidedSellingDashBoardDataPopulator()
	{
		return tmaGuidedSellingDashBoardDataPopulator;
	}
}
