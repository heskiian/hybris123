/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaProductDao;
import de.hybris.platform.b2ctelcoservices.daos.TmaProductOfferingDao;
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
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfAnyResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static java.lang.String.format;


/**
 * Default implementation of the {@link TmaPoService}.
 *
 * @since 6.7
 */
public class DefaultTmaPoService implements TmaPoService
{
	private static final String CONFIGURABLE_CHARACTERISTIC_VALUE_PATTERN = "configurable.characteristic.value.pattern";
	private static final String ACTIONS_WITH_OPERATIONAL_OFFERINGS = "default.subscribedProductActions.withOperationalOfferings";
	private static final String PRODUCT_NOT_IN_PC = "no product offering found in the Product Catalog for the given subscribed "
			+ "product";
	private static final String INVALID_CART_ACTION = "Invalid cart action";
	private ModelService modelService;
	private ProductService productService;
	private TmaCommercePriceService commercePriceService;
	private TmaProductOfferingDao tmaProductOfferingDao;
	private ConfigurationService configurationService;
	private final TmaProductDao tmaProductDao;

	public DefaultTmaPoService(final TmaProductDao tmaProductDao)
	{
		this.tmaProductDao = tmaProductDao;
	}

	@Override
	public TmaProductOfferingModel getPoForCode(final String code)
	{
		final ProductModel productModel = getProductService().getProductForCode(code);
		if (productModel instanceof TmaProductOfferingModel)
		{
			return (TmaProductOfferingModel) productModel;
		}
		throw new ModelNotFoundException(TmaProductOfferingModel._TYPECODE + format("  with code '%s' not found!", code));
	}


	@Override
	public TmaProductOfferingModel getPoForCodeAndPriceContext(final String code, final TmaPriceContext priceContext)
	{
		validateParameterNotNullStandardMessage("priceContext", priceContext);
		final TmaProductOfferingModel product = getPoForCode(code);
		priceContext.setProduct(product);
		product.setEurope1Prices(getCommercePriceService().filterPricesbyPriceContext(priceContext));
		return product;
	}

	@Override
	public void addPoChildToParent(final TmaBundledProductOfferingModel parent, final TmaProductOfferingModel child)
	{
		final Set<TmaProductOfferingModel> productOfferings = new HashSet<>();
		productOfferings.addAll(parent.getChildren());
		productOfferings.add(child);
		parent.setChildren(productOfferings);
		getModelService().save(parent);
		getModelService().refresh(parent);
		getModelService().refresh(child);
	}

	@Override
	public void removePoChildFromParent(final TmaBundledProductOfferingModel parent, final TmaProductOfferingModel child)
	{
		final Set<TmaProductOfferingModel> newOfferingModels = new HashSet<>();
		for (final TmaProductOfferingModel newOfferingModel : parent.getChildren())
		{
			if (!StringUtils.equals(newOfferingModel.getCode(), child.getCode()))
			{
				newOfferingModels.add(newOfferingModel);
			}
		}
		parent.setChildren(newOfferingModels);
		getModelService().save(parent);
		getModelService().refresh(parent);
		getModelService().refresh(child);
	}

	@Override
	public Collection<TmaBundledProductOfferingModel> getAllParents(final TmaProductOfferingModel poModel)
	{
		final Map<String, TmaBundledProductOfferingModel> parents = new HashMap<>();
		findParents(poModel, parents);
		return new ArrayList<>(parents.values());
	}

	@Override
	public Optional<TmaProductOfferingGroupModel> getGroupNextToProductGroup(final TmaBundledProductOfferingModel bpo,
			final TmaProductOfferingModel po)
	{
		final List<TmaProductOfferingGroupModel> allGroups = bpo.getProductOfferingGroups();
		final Optional<Integer> productGroupIndex = findIndexForGroupWithProduct(po, allGroups);
		if (!productGroupIndex.isPresent() || allGroups.size() < 2)
		{
			return Optional.empty();
		}
		final int nextGroupIndex = (productGroupIndex.get() > 0) ? 0 : ((productGroupIndex.get()) + 1);
		return Optional.of(allGroups.get(nextGroupIndex));
	}

	@Override
	public TmaBundledProductOfferingModel getBpoForCode(final String code)
	{
		final ProductModel productModel = getProductService().getProductForCode(code);
		if (productModel instanceof TmaBundledProductOfferingModel)
		{
			return (TmaBundledProductOfferingModel) productModel;
		}
		throw new ModelNotFoundException(format("TmaProductOfferingModel with code '%s' not found!", code));
	}

	@Override
	public boolean isPoEligibleForProcessType(final TmaProcessType processType, final TmaProductOfferingModel product,
			final TmaBundledProductOfferingModel bpo)
	{
		boolean isPoEligibleForProcessType;

		if (ObjectUtils.isEmpty(product))
		{
			return false;
		}

		if (bpo != null)
		{
			final Set<PriceRowModel> bpoPriceRows = new HashSet<>(bpo.getEurope1Prices());
			isPoEligibleForProcessType = existPriceOverrideForProcessType(bpoPriceRows, product, processType);
			if (isPoEligibleForProcessType)
			{
				return true;
			}
		}

		return existPriceRowForProcessType((new HashSet<>(product.getEurope1Prices())), processType);
	}

	@Override
	public boolean isValidParent(final TmaProductOfferingModel product, final TmaBundledProductOfferingModel parent)
	{
		return getAllParents(product).contains(parent);
	}

	@Override
	public Optional<TmaProductOfferingGroupModel> getOfferingGroupForPoAndBpo(final TmaProductOfferingModel productOffering,
			final TmaBundledProductOfferingModel parent)
	{
		final List<TmaProductOfferingGroupModel> allGroupsForProduct = productOffering.getAssociatedProductOfferingGroups();
		if (CollectionUtils.isEmpty(allGroupsForProduct))
		{
			return Optional.empty();
		}
		final Set<TmaProductOfferingGroupModel> allBpoGroups = retrieveAllPoGroupsForBpo(parent, new HashSet<>());
		return allGroupsForProduct.stream().filter(poGroup -> allBpoGroups.contains(poGroup)).findFirst();
	}

	@Override
	public SearchPageData<TmaProductOfferingModel> getProductOfferings(final Collection<CatalogVersionModel> catalogVersions,
			final TmaProductOfferingSearchContext productOfferingSearchContext)
	{
		return getTmaProductOfferingDao().getProductOfferings(catalogVersions, productOfferingSearchContext);
	}

	@Override
	public Set<TmaProductSpecCharacteristicModel> getConfigurableProductSpecCharacteristics(
			final TmaProductOfferingModel productOffering)
	{
		if (CollectionUtils.isEmpty(productOffering.getProductSpecCharacteristicValues()))
		{
			return new HashSet<>();
		}
		return productOffering.getProductSpecCharacteristicValues().stream()
				.filter(productSpecCharacteristicValue -> productSpecCharacteristicValue.getValue()
						.equals(getConfigurationService().getConfiguration().getString(CONFIGURABLE_CHARACTERISTIC_VALUE_PATTERN)))
				.map(TmaProductSpecCharacteristicValueModel::getProductSpecCharacteristic).collect(Collectors.toSet());
	}
	
	@Override
	public Set<TmaProductSpecCharValueUseModel> getConfigurableProductSpecCharValueUses(
			final TmaProductOfferingModel productOffering)
	{
		if (CollectionUtils.isEmpty(productOffering.getProductSpecCharValueUses()))
		{
			return new HashSet<>();
		}
		return productOffering.getProductSpecCharValueUses().stream()
				.filter(pscvu -> CollectionUtils.isEmpty(pscvu.getProductSpecCharacteristicValues()) 
						|| pscvu.getProductSpecCharacteristicValues().stream().anyMatch(pscv-> pscv.getValue()
								.equals(getConfigurationService().getConfiguration().getString(CONFIGURABLE_CHARACTERISTIC_VALUE_PATTERN))))
				.collect(Collectors.toSet());
	}

	@Override
	public Optional<TmaOperationalProductOfferingModel> getOperationalProductOffering(final TmaSubscribedProductAction action)
	{
		return getTmaProductDao().getOperationalProductOffering(action);
	}

	@Override
	public ProductModel getProductOffering(final String productOfferingCode, final TmaSubscribedProductAction cartAction)
	{
		if (TmaSubscribedProductAction.KEEP.equals(cartAction))
		{
			ServicesUtil.validateParameterNotNullStandardMessage("subscribedProduct", productOfferingCode);
			final ProductModel productModel = getPoForCode(productOfferingCode);
			if (ObjectUtils.isEmpty(productModel))
			{
				throw new ModelNotFoundException(PRODUCT_NOT_IN_PC);
			}
			return productModel;
		}
		if (isServiceRequestAction(cartAction))
		{
			final Optional<TmaOperationalProductOfferingModel> operationalProduct = getOperationalProductOffering(cartAction);
			if (operationalProduct.isEmpty())
			{
				throw new IllegalArgumentException(INVALID_CART_ACTION);
			}
			return operationalProduct.get();
		}
		throw new IllegalArgumentException(INVALID_CART_ACTION);
	}

	@Override
	public boolean isServiceRequestAction(final TmaSubscribedProductAction action)
	{
		if (action == null || StringUtils.isEmpty(action.getCode()))
		{
			return false;
		}
		return getConfigurationService().getConfiguration().getString(ACTIONS_WITH_OPERATIONAL_OFFERINGS)
				.contains(action.getCode());
	}

	@Override
	public List<TmaProductOfferingModel> getProductsForCode(final String code)
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		final List<ProductModel> products = getTmaProductDao().findProductsByCode(code);
		validateIfAnyResult(products, format("Product with code '%s' not found!", code));

		final List<TmaProductOfferingModel> tmaProductOfferings = new ArrayList<>();
		products.forEach(product -> {
			if (product instanceof TmaProductOfferingModel)
			{
				tmaProductOfferings.add((TmaProductOfferingModel) product);
			}
		});
		return tmaProductOfferings;
	}

	@Override
	public Set<TmaProductOfferingModel> getProductOfferingsByProperties(final Map<String, String> propertyPattern)
	{
		final Set<Entry<String, String>> entries = propertyPattern.entrySet();

		final List<TmaProductOfferingModel> productOfferingByProperties = new ArrayList<>();
		for (final Entry<String, String> entry : entries)
		{
			final List<TmaProductOfferingModel> productOfferingByProperty = getTmaProductDao()
					.findProductOfferingsByPattern(entry.getKey(), entry.getValue());
			productOfferingByProperties.addAll(productOfferingByProperty);
		}

		return new HashSet<TmaProductOfferingModel>(productOfferingByProperties);
	}

	@Override
	public Set<TmaProductOfferingGroupModel> getProductOfferingGroupsByProperties(final Map<String, String> propertyPattern)
	{
		final Set<Entry<String, String>> entries = propertyPattern.entrySet();

		final List<TmaProductOfferingGroupModel> productOfferingGroupByProperties = new ArrayList<>();
		for (final Entry<String, String> entry : entries)
		{
			final List<TmaProductOfferingGroupModel> productOfferingGroupByProperty = getTmaProductDao()
					.findProductOfferingGroupsByPattern(entry.getKey(), entry.getValue());
			productOfferingGroupByProperties.addAll(productOfferingGroupByProperty);
		}

		return new HashSet<TmaProductOfferingGroupModel>(productOfferingGroupByProperties);
	}

	/**
	 * Returns product offering list for corresponding catalog version
	 *
	 * @param catalogVersion
	 * 		for which product offerings will be retrieved
	 * @return List of {@link TmaProductOfferingModel}
	 */
	@Override
	public List<TmaProductOfferingModel> getProductOfferings(final CatalogVersionModel catalogVersion)
	{
		return getTmaProductDao().getProductOfferings(catalogVersion);
	}

	/**
	 * This method will traverses down the hierarchy of a given bundle product offering to find out all the child spos.
	 * There might be a case when a given bpo has a child bpo and further this child bpo have many child spos. Returns
	 * the list of all child simple product offerings {@Link TmaSimpleProductOfferingModel} of a given bundle product
	 * offering.
	 *
	 * @param bundleProductOffering
	 * 		the bundle product offering
	 * @return the child spos of the bundle product offering
	 */
	@Override
	public List<TmaSimpleProductOfferingModel> getSpoListForBpo(final TmaBundledProductOfferingModel bundleProductOffering)
	{
		final Set<TmaSimpleProductOfferingModel> childSpoSet = new HashSet<>();
		final Set<TmaProductOfferingModel> children = bundleProductOffering.getChildren();
		getSposForBpo(children, childSpoSet);
		final List<TmaSimpleProductOfferingModel> childSpos = childSpoSet.stream().collect(Collectors.toList());
		return childSpos;
	}

	@Override
	public Optional<TmaBundledProdOfferOptionModel> getBundledProdOfferOptionFor(
			final TmaBundledProductOfferingModel bundledProductOffering, final TmaProductOfferingModel productOffering)
	{
		validateParameterNotNull(bundledProductOffering, "The bundled product offering can not be null.");
		validateParameterNotNull(productOffering, "The product offering can not be null.");

		if (CollectionUtils.isEmpty(bundledProductOffering.getBpoOptions()))
		{
			return Optional.empty();
		}

		return bundledProductOffering.getBpoOptions().stream()
				.filter((TmaBundledProdOfferOptionModel bpoOption) -> productOffering.getCode()
						.equals(bpoOption.getProductOffering().getCode())).findFirst();
	}

	@Override
	public List<TmaBundledProductOfferingModel> getIntermediateBpos(final TmaProductOfferingModel currentPo,
			final TmaBundledProductOfferingModel targetBpo)
	{
		validateParameterNotNull(currentPo, "The product offering can not be null.");
		validateParameterNotNull(targetBpo, "The target bundled product offering can not be null.");

		final List<TmaBundledProductOfferingModel> intermediateBpos = new ArrayList<>();

		if (currentPo.getCode().equals(targetBpo.getCode()))
		{
			intermediateBpos.add((TmaBundledProductOfferingModel) currentPo);
			return intermediateBpos;
		}

		if (CollectionUtils.isEmpty(currentPo.getParents()))
		{
			return Collections.emptyList();
		}

		if (currentPo instanceof TmaBundledProductOfferingModel)
		{
			intermediateBpos.add((TmaBundledProductOfferingModel) currentPo);
		}

		for (TmaBundledProductOfferingModel parentBpo : currentPo.getParents())
		{
			final List<TmaBundledProductOfferingModel> bpos = getIntermediateBpos(parentBpo, targetBpo);

			if (!intermediateBpos.contains(parentBpo) && CollectionUtils.isNotEmpty(bpos))
			{
				intermediateBpos.addAll(bpos);
				return intermediateBpos;
			}
		}

		if (currentPo instanceof TmaBundledProductOfferingModel)
		{
			intermediateBpos.remove(currentPo);
		}

		return intermediateBpos;
	}

	@Override
	public boolean isValidChild(final TmaProductOfferingModel child, final TmaBundledProductOfferingModel parent)
	{
		if (TmaFixedBundledProductOfferingModel._TYPECODE.equals(parent.getItemtype()))
		{
			return TmaSimpleProductOfferingModel._TYPECODE.equals(child.getItemtype())
					|| TmaFixedBundledProductOfferingModel._TYPECODE.equals(child.getItemtype());
		}
		return true;
	}

	@Override
	public List<TmaProductOfferingModel> getSposAndFixedBposFor(final TmaBundledProductOfferingModel bundledProductOffering)
	{
		final Set<TmaProductOfferingModel> childrenSposAndFixedBposSet = new HashSet<>();
		final Set<TmaProductOfferingModel> children = bundledProductOffering.getChildren();
		getSposAndFixedBposFor(children, childrenSposAndFixedBposSet);
		return new ArrayList<>(childrenSposAndFixedBposSet);
	}

	private Set<TmaProductOfferingGroupModel> retrieveAllPoGroupsForBpo(final TmaBundledProductOfferingModel bpo,
			final Set<TmaProductOfferingGroupModel> allGroups)
	{
		allGroups.addAll(bpo.getProductOfferingGroups());
		for (final TmaProductOfferingModel child : bpo.getChildren())
		{
			if (child instanceof TmaBundledProductOfferingModel)
			{
				retrieveAllPoGroupsForBpo((TmaBundledProductOfferingModel) child, allGroups);
			}
		}
		return allGroups;
	}

	private boolean existPriceOverrideForProcessType(final Set<PriceRowModel> priceOverrides,
			final TmaProductOfferingModel affectedProduct, final TmaProcessType processType)
	{
		if (CollectionUtils.isEmpty(priceOverrides))
		{
			return false;
		}

		final Set<PriceRowModel> filteredPriceRows = priceOverrides.stream()
				.filter(priceRow -> (priceRow.getAffectedProductOffering() != null && priceRow.getAffectedProductOffering().getCode()
						.equals(affectedProduct.getCode()))).collect(Collectors.toSet());

		return existPriceRowForProcessType(filteredPriceRows, processType);
	}

	private boolean existPriceRowForProcessType(final Set<PriceRowModel> priceRows,
			final TmaProcessType processType)
	{
		if (CollectionUtils.isEmpty(priceRows))
		{
			return false;
		}

		for (final PriceRowModel price : priceRows)
		{
			final Set<TmaProcessType> processTypes = price.getProcessTypes();
			if (processTypes == null || processTypes.isEmpty() || processTypes.contains(processType))
			{
				return true;
			}
		}

		return false;
	}

	private void findParents(final TmaProductOfferingModel poModel, final Map<String, TmaBundledProductOfferingModel> parentBpos)
	{
		poModel.getParents().forEach(bpo -> {
			parentBpos.put(bpo.getCode(), bpo);
			findParents(bpo, parentBpos);
		});
	}

	private Optional<Integer> findIndexForGroupWithProduct(final TmaProductOfferingModel poModel,
			final List<TmaProductOfferingGroupModel> groups)
	{
		for (int i = 0; i < groups.size(); i++)
		{
			if (groups.get(i).getChildProductOfferings().contains(poModel))
			{
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}

	private Set<TmaSimpleProductOfferingModel> getSposForBpo(final Set<TmaProductOfferingModel> children,
			final Set<TmaSimpleProductOfferingModel> singleProductOfferings)
	{
		for (final TmaProductOfferingModel productOffering : children)
		{
			if (productOffering instanceof TmaBundledProductOfferingModel)
			{
				getSposForBpo(((TmaBundledProductOfferingModel) productOffering).getChildren(), singleProductOfferings);
			}
			else
			{
				singleProductOfferings.add((TmaSimpleProductOfferingModel) productOffering);
			}
		}
		return singleProductOfferings;
	}

	private Set<TmaProductOfferingModel> getSposAndFixedBposFor(final Set<TmaProductOfferingModel> children,
			final Set<TmaProductOfferingModel> productOfferings)
	{
		for (final TmaProductOfferingModel productOffering : children)
		{
			if (!TmaFixedBundledProductOfferingModel._TYPECODE.equals(productOffering.getItemtype())
					&& productOffering instanceof TmaBundledProductOfferingModel)
			{
				getSposAndFixedBposFor(((TmaBundledProductOfferingModel) productOffering).getChildren(), productOfferings);
			}
			else
			{
				productOfferings.add(productOffering);
			}
		}
		return productOfferings;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	protected TmaCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	protected TmaProductOfferingDao getTmaProductOfferingDao()
	{
		return tmaProductOfferingDao;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	@Required
	public void setCommercePriceService(final TmaCommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}

	@Required
	public void setTmaProductOfferingDao(final TmaProductOfferingDao tmaProductOfferingDao)
	{
		this.tmaProductOfferingDao = tmaProductOfferingDao;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected TmaProductDao getTmaProductDao()
	{
		return tmaProductDao;
	}

}
