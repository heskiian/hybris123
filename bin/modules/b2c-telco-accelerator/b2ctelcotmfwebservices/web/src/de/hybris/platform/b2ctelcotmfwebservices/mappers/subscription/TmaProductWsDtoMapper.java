/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaBillingAccountRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductOfferingRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductOrderRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductPriceWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductRelationshipWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaStatusTypeWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.mappers.TmaTmfAbstractRelatedPartyDataMapper;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {@link TmaSubscribedProductData} and {@link TmaProductWsDto}
 *
 * @since 1810
 */
public class TmaProductWsDtoMapper extends TmaTmfAbstractRelatedPartyDataMapper<TmaSubscribedProductData, TmaProductWsDto>
{
	public static final String PRDUCTWSDTO_HREF = Config.getString(B2ctelcotmfwebservicesConstants.API_BASE_URL, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCTAPI_VERSION, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCTAPI_HREF, StringUtils.EMPTY);


	@Resource(name = "tmaSubscriptionBaseFacade")
	private TmaSubscriptionBaseFacade subscriptionBaseFacade;

	@Resource(name = "tmaSubscribedProductFacade")
	private TmaSubscribedProductFacade subscribedProductFacade;

	@Resource(name = "tmaProductOfferFacade")
	private TmaProductOfferFacade tmaProductOfferFacade;

	private static final String PRODUCT_CODE = "productCode";
	private static final String SUBSCRIPTION_BASE_ID = "subscriptionBaseId";

	@Override
	public void mapAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b, final MappingContext context)
	{
		// other fields are mapped automatically
		mapDefaultAtoB(a, b, context);
		mapFieldsAtoB(a, b, context);
		mapRelatedPartyAtoB(a, b, context);
		mapStatusTypeAtoB(a, b, context);
		mapProductOfferingRefAtoB(a, b, context);
		mapOrderAtoB(a, b, context);

		if (StringUtils.isNotEmpty(a.getSubscriptionBaseId()) && StringUtils.isNotEmpty(a.getBillingsystemId()))
		{
			mapBillingAccountAtoB(a, b, context);
			mapProductPriceAtoB(a, b, context);
			mapProductRelationAtoB(a, b, context);
		}
	}

	protected void mapOrderAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b, final MappingContext context)
	{
		context.beginMappingField("orderNumber", getAType(), a, "productOrder", getBType(), b);
		try
		{
			final List<TmaProductOrderRefWsDto> productOrderRefWsDtos = new ArrayList<>();
			if (shouldMap(a, b, context) && a.getOrderNumber() != null)
			{
				productOrderRefWsDtos.add(mapperFacade.map(a, TmaProductOrderRefWsDto.class, context));
				b.setProductOrder(productOrderRefWsDtos);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapProductPriceAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b, final MappingContext context)
	{
		final java.util.List<TmaProductPriceWsDto> tmaProductPriceWsDtos = new ArrayList<>();

		context.beginMappingField("id", getAType(), a, "productPrice", getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				final TmaProductPriceWsDto tmaProductPriceWsDto = mapperFacade.map(a, TmaProductPriceWsDto.class, context);
				if (b.getBillingAccount() != null)
				{
					tmaProductPriceWsDto.setBillingAccount(b.getBillingAccount().get(0));
				}
				tmaProductPriceWsDtos.add(tmaProductPriceWsDto);
				b.setProductPrice(tmaProductPriceWsDtos);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapBillingAccountAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b, final MappingContext context)
	{
		final TmaSubscriptionBaseData tmaSubscriptionBaseData = getSubscriptionBaseFacade()
				.getSubscriptionBaseForSubscriberIdentity(a.getSubscriptionBaseId(), a.getBillingsystemId());
		context.beginMappingField("tmaBillingAccountData", getAType(), tmaSubscriptionBaseData, "billingAccount", getBType(), b);
		try
		{
			final List<TmaBillingAccountRefWsDto> tmaBillingAccountRefWsDtos = new ArrayList<>();

			if (shouldMap(a, b, context) && tmaSubscriptionBaseData.getTmaBillingAccountData() != null)
			{
				tmaBillingAccountRefWsDtos.add(mapperFacade.map(tmaSubscriptionBaseData, TmaBillingAccountRefWsDto.class, context));
				b.setBillingAccount(tmaBillingAccountRefWsDtos);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapFieldsAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b, final MappingContext context)
	{
		context.beginMappingField("endDate", getAType(), a, "terminationDate", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getEndDate() != null)
			{
				b.setTerminationDate(a.getEndDate());
			}
		}
		finally
		{
			context.endMappingField();
		}
		context.beginMappingField(PRODUCT_CODE, getAType(), a, "productSerialNumber", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getProductCode() != null)
			{
				b.setProductSerialNumber(a.getProductCode());
			}
		}
		finally
		{
			context.endMappingField();
		}
		context.beginMappingField("parentPOCode", getAType(), a, "isBundle", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getParentPOCode() != null)
			{
				b.setIsBundle(StringUtils.isNotEmpty(a.getParentPOCode()));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapDefaultAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b, final MappingContext context)
	{
		context.beginMappingField("id", getAType(), a, "href", getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				b.setHref(PRDUCTWSDTO_HREF + a.getId());
				b.setIsCustomerVisible(Config.getBoolean(B2ctelcotmfwebservicesConstants.TMA_PRODUCTAPI_CUSTOMERVISISBLE, true));
				b.setBaseType(Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCTAPI_BASETYPE, "product"));
				b.setType(b.getClass().getSimpleName());
				b.setSchemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapRelatedPartyAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b, final MappingContext context)
	{
		String publicIdentity = StringUtils.EMPTY;
		context.beginMappingField(SUBSCRIPTION_BASE_ID, getAType(), a, "relatedParty", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getSubscriptionBaseId() != null && a.getBillingsystemId() != null)
			{
				final List<TmaSubscriptionAccessData> tmaSubscriptionAccessDatas = getSubscriptionAccessFacade()
						.getSubscriptionAccessesBySubscriberIdentity(a.getSubscriptionBaseId(), a.getBillingsystemId());
				if (CollectionUtils.isNotEmpty(tmaSubscriptionAccessDatas))
				{
					b.setRelatedParty(getTmaRelatedPartyWsDtoList(tmaSubscriptionAccessDatas, context));
					publicIdentity = tmaSubscriptionAccessDatas.get(0).getSubscriberIdentity();
				}
			}
		}
		finally
		{
			context.endMappingField();
		}
		//Set Public Identity
		context.beginMappingField(SUBSCRIPTION_BASE_ID, getAType(), a, "publicIdentifier", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(publicIdentity))
			{
				b.setPublicIdentifier(publicIdentity);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapProductRelationAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b, final MappingContext context)
	{
		context.beginMappingField(SUBSCRIPTION_BASE_ID, getAType(), a, "productRelationship", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getSubscriptionBaseId())
					&& StringUtils.isNotEmpty(a.getBillingsystemId()))
			{
				final List<TmaSubscribedProductData> subscribedProductDatas = getSubscribedProductFacade()
						.getSubscribedProducts(a.getSubscriptionBaseId(), a.getBillingsystemId());
				b.setProductRelationship(getProductRelationshipsFromSubscribedProductDatas(subscribedProductDatas, a, context));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected List<TmaProductRelationshipWsDto> getProductRelationshipsFromSubscribedProductDatas(
			final List<TmaSubscribedProductData> subscribedProductDatas, final TmaSubscribedProductData tmaSubscribedProductData,
			final MappingContext context)
	{
		final List<TmaProductRelationshipWsDto> productRelationships = new ArrayList<>();
		subscribedProductDatas.forEach(subscribedProductsData -> {
			if (!tmaSubscribedProductData.getId().equals(subscribedProductsData.getId()))
			{
				productRelationships.add(mapperFacade.map(subscribedProductsData, TmaProductRelationshipWsDto.class, context));
			}
		});
		return productRelationships;
	}

	protected void mapProductOfferingRefAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b,
			final MappingContext context)
	{
		context.beginMappingField(PRODUCT_CODE, getAType(), a, "productOffering", getBType(), b);

		try
		{
			if (shouldMap(a, b, context) && a.getProductCode() != null)
			{
				final ProductData productData = getPoFacade().getPoForCode(a.getProductCode(),
						Arrays.asList(ProductOption.BASIC, ProductOption.PRICE));
				b.setProductOffering(mapperFacade.map(productData, TmaProductOfferingRefWsDto.class, context));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapStatusTypeAtoB(final TmaSubscribedProductData a, final TmaProductWsDto b, final MappingContext context)
	{
		context.beginMappingField("subscriptionStatus", getAType(), a, "status", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && null != a.getSubscriptionStatus())
			{
				if ("EXPIRED".equalsIgnoreCase(a.getSubscriptionStatus()))
				{
					b.setStatus(TmaStatusTypeWsDto.fromValue("terminated"));
				}
				else if ("PENDING_ACTIVATION".equalsIgnoreCase(a.getSubscriptionStatus())
						|| "INACTIVE".equalsIgnoreCase(a.getSubscriptionStatus()))
				{
					b.setStatus(TmaStatusTypeWsDto.fromValue("pendingActive"));
				}
				else
				{
					b.setStatus(TmaStatusTypeWsDto.fromValue(a.getSubscriptionStatus().toLowerCase(Locale.ENGLISH)));
				}
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaProductWsDto b, final TmaSubscribedProductData a, final MappingContext context)
	{
		// other fields mapped automatically
		context.beginMappingField("productOffering", getBType(), b, PRODUCT_CODE, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && b.getProductOffering() != null)
			{
				a.setProductCode(b.getProductOffering().getId());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected TmaSubscriptionBaseFacade getSubscriptionBaseFacade()
	{
		return subscriptionBaseFacade;
	}

	public void setSubscriptionBaseFacade(final TmaSubscriptionBaseFacade subscriptionBaseFacade)
	{
		this.subscriptionBaseFacade = subscriptionBaseFacade;
	}

	protected TmaSubscribedProductFacade getSubscribedProductFacade()
	{
		return subscribedProductFacade;
	}

	public void setSubscribedProductFacade(final TmaSubscribedProductFacade subscribedProductFacade)
	{
		this.subscribedProductFacade = subscribedProductFacade;
	}

	protected TmaProductOfferFacade getPoFacade()
	{
		return tmaProductOfferFacade;
	}

	public void setPoFacade(final TmaProductOfferFacade poFacade)
	{
		this.tmaProductOfferFacade = poFacade;
	}
}
