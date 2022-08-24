/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.b2ctelcoservices.data.TmaBpoChildContext;
import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentBundledProdOfferOption;
import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentBundledProductOffering;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Optional;

import org.springframework.util.ObjectUtils;


/**
 * Default populator for populating a {@link TmaSolrDocumentBundledProductOffering} to be persisted by the solr server having a
 * {@link TmaBpoChildContext} as source.
 *
 * @since 2105
 */
public class TmaSolrDocumentBpoPopulator
		implements Populator<TmaBpoChildContext, TmaSolrDocumentBundledProductOffering>
{
	private Converter<TmaBundledProdOfferOptionModel, TmaSolrDocumentBundledProdOfferOption> bpoOptionSolrConverter;
	private TmaPoService poService;

	public TmaSolrDocumentBpoPopulator(
			final Converter<TmaBundledProdOfferOptionModel, TmaSolrDocumentBundledProdOfferOption> bpoOptionSolrConverter,
			final TmaPoService poService)
	{
		this.bpoOptionSolrConverter = bpoOptionSolrConverter;
		this.poService = poService;
	}

	@Override
	public void populate(final TmaBpoChildContext source, final TmaSolrDocumentBundledProductOffering target)
	{
		ServicesUtil.validateParameterNotNull(source, "Parameter source can not be null");
		ServicesUtil.validateParameterNotNull(target, "Parameter target can not be null");
		ServicesUtil.validateParameterNotNull(source.getChild(), "Parameter source.child can not be null");
		ServicesUtil.validateParameterNotNull(source.getParent(), "Parameter source.parent can not be null");

		final ProductModel child = source.getChild();
		target.setCode(child.getCode());
		target.setName(child.getName());
		target.setItemtype(child.getItemtype());
		if (!ObjectUtils.isEmpty(child.getApprovalStatus()))
		{
			target.setApprovalStatus(child.getApprovalStatus().getCode());
		}

		final Optional<TmaBundledProdOfferOptionModel> bpoOptions = getPoService()
				.getBundledProdOfferOptionFor(source.getParent(), (TmaProductOfferingModel) child);

		bpoOptions.ifPresent(tmaBundledProdOfferOption -> target.setBundledProdOfferOption(
				getBpoOptionSolrConverter().convert(tmaBundledProdOfferOption)));
	}

	protected Converter<TmaBundledProdOfferOptionModel, TmaSolrDocumentBundledProdOfferOption> getBpoOptionSolrConverter()
	{
		return bpoOptionSolrConverter;
	}

	protected TmaPoService getPoService()
	{
		return poService;
	}

}
