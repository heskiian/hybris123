/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.service.impl;

import de.hybris.platform.upilintegrationservices.constants.UpilintegrationservicesConstants;
import de.hybris.platform.upilintegrationservices.data.UpilResponse;
import de.hybris.platform.upilintegrationservices.exceptions.UpilintegrationservicesException;
import de.hybris.platform.upilintegrationservices.odata.feed.UpilPopulateOdataNavigationProperty;
import de.hybris.platform.upilintegrationservices.service.UpilIntegrationClientService;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


/**
 * Default implementation of the {@link UpilIntegrationClientService}.
 *
 * @since 1911
 */
public class DefaultUpilIntegrationClientService implements UpilIntegrationClientService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultUpilIntegrationClientService.class);
	private static final int ERROR_CODE_START = 400;
	private static final int ERROR_CODE_END = 599;

	private static final String APPLICATION_JSON = "application/json";
	private static final String APPLICATION_XML = "application/xml";

	private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HTTP_HEADER_ACCEPT = "Accept";
	private static final String HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Language";
	private static final String HTTP_METHOD_POST = "POST";
	private static final String HTTP_METHOD_GET = "GET";
	private static final String HTTP_LANGUAGE = "en";

	private static final String METADATA = "$metadata";
	private static final String SEPARATOR = "/";
	private static final String X_CSRF_TOKEN = "x-csrf-token";
	private static final String SET_COOKIE = "set-cookie";
	private static final String COOKIE = "Cookie";
	private static final String FETCH = "fetch";
	private static final String AUTHORIZATION_TYPE = "Basic ";
	private static final String AUTHORIZATION = "Authorization";
	private static final String SEMI_COLON = ";";

	private static final String UPIL_SERVICEURL_FOR_READ_UPIL_EDM = Config
			.getParameter(UpilintegrationservicesConstants.UPIL_SERVICE_URL_GET_METADATA);
	private static final String UPIL_SERVICEURL_FOR_CREATE_UPIL_PRODUCT_ENTRY = Config
			.getParameter(UpilintegrationservicesConstants.UPIL_SERVICE_URL_CREATE_PRODUCT);
	private static final String UPIL_SERVICEURL_FOR_SEMANTICS = Config
			.getParameter(UpilintegrationservicesConstants.UPIL_SERVICE_URL_GET_SEMANTICS);
	private static final String UPIL_SERVICEURL_FOR_REFPRODUCT = Config
			.getParameter(UpilintegrationservicesConstants.UPIL_SERVICE_URL_GET_REF_PRODUCT_TYPE);
	private static final String UPIL_USERNAME = Config.getParameter(UpilintegrationservicesConstants.UPIL_SERVICE_USERNAME);
	private static final String UPIL_PASSWORD = Config.getParameter(UpilintegrationservicesConstants.UPIL_SERVICE_PW);

	String[] cookieAndToken = new String[2];

	@Override
	public UpilResponse createUpilProduct(final Map<String, Object> product)
	{
		final UpilResponse response = new UpilResponse();
		try
		{
			final Edm edm = readUpilEdm(UPIL_SERVICEURL_FOR_READ_UPIL_EDM);
			final ODataEntry createdProductEntry = createUpilProductEntry(edm, UPIL_SERVICEURL_FOR_CREATE_UPIL_PRODUCT_ENTRY,
					APPLICATION_JSON,
					UpilintegrationservicesConstants.PRODUCT_ENTITYSET, product);
			if (null != createdProductEntry && !ObjectUtils.isEmpty(createdProductEntry.getProperties()))
			{
				response.setSuccessCode(
						createdProductEntry.getProperties().get(UpilintegrationservicesConstants.UTILITIES_PRODUCT).toString());
			}
		}
		catch (IOException | URISyntaxException | ODataException e)
		{
			LOG.info(e.getMessage(), e);
			final Map<String, String> error = new HashMap<>();
			error.put(product.get(UpilintegrationservicesConstants.UTILITIES_PRODUCT).toString(), e.getMessage());
			response.setError(error);
		}
		return response;
	}

	private Edm readUpilEdm(final String upilServiceUrl) throws IOException, ODataException
	{
		final String metaDataUri = upilServiceUrl + SEPARATOR + METADATA;
		final InputStream metaDatacontent = getContent(metaDataUri, APPLICATION_XML, HTTP_METHOD_GET, HTTP_LANGUAGE);
		return EntityProvider.readMetadata(metaDatacontent, false);
	}

	private ODataEntry createUpilProductEntry(final Edm edm, final String upilServiceUri, final String contentType,
			final String productEntitySetName, final Map<String, Object> productData)
			throws EdmException, EntityProviderException, IOException, URISyntaxException
	{
		final String upilAbsolutUri = upilServiceUri + SEPARATOR + productEntitySetName;
		return writeUpilProductEntity(edm, upilAbsolutUri, productEntitySetName, productData, contentType, HTTP_METHOD_POST);
	}

	private ODataEntry writeUpilProductEntity(final Edm edm, final String upilAbsolutUri, final String productEntitySetName,
			final Map<String, Object> productData, final String contentType, final String httpMethod)
			throws EdmException, IOException, EntityProviderException, URISyntaxException
	{
		final HttpURLConnection upilConnection = establishUpilConnection(upilAbsolutUri, contentType, httpMethod, HTTP_LANGUAGE);

		final EdmEntityContainer productEntityContainer = edm.getDefaultEntityContainer();
		final EdmEntitySet productEntitySet = productEntityContainer.getEntitySet(productEntitySetName);
		final URI upilProductRootUri = new URI(productEntitySetName);

		final ExpandSelectTreeNode node = ExpandSelectTreeNode.entitySet(productEntitySet).expandedLinks(Lists.newArrayList(
				UpilintegrationservicesConstants.TO_UTILS_BILLING_ATTRIBUTE,
				UpilintegrationservicesConstants.TO_UTILS_SALES_ATTRIBUTE,
				UpilintegrationservicesConstants.TO_UTILS_ONE_TIME_CHAGRE,
				UpilintegrationservicesConstants.TO_UTILS_RECURRING_CHARGE,
				UpilintegrationservicesConstants.TO_UTILS_USAGE_CHARGE,
				UpilintegrationservicesConstants.TO_UTILS_DISCOUNT)).build();

		final List<Map<String, Object>> utilsBillingAttribute = (List<Map<String, Object>>) productData
				.get(UpilintegrationservicesConstants.TO_UTILS_BILLING_ATTRIBUTE);

		final List<Map<String, Object>> utilsSalesAttribute = (List<Map<String, Object>>) productData
				.get(UpilintegrationservicesConstants.TO_UTILS_SALES_ATTRIBUTE);

		final List<Map<String, Object>> utilsProductOneTimeCharges = (List<Map<String, Object>>) productData
				.get(UpilintegrationservicesConstants.TO_UTILS_ONE_TIME_CHAGRE);


		final List<Map<String, Object>> utilsProductRecurringCharges = (List<Map<String, Object>>) productData
				.get(UpilintegrationservicesConstants.TO_UTILS_RECURRING_CHARGE);


		final List<Map<String, Object>> utilsProductUsageCharge = (List<Map<String, Object>>) productData
				.get(UpilintegrationservicesConstants.TO_UTILS_USAGE_CHARGE);

		final List<Map<String, Object>> utilsProductDiscounts = (List<Map<String, Object>>) productData
				.get(UpilintegrationservicesConstants.TO_UTILS_DISCOUNT);

		final EntityProviderWriteProperties properties = EntityProviderWriteProperties
				.serviceRoot(upilProductRootUri)
				.expandSelectTree(node)
				.callbacks(new ImmutableMap.Builder<String, ODataCallback>()
						.put(UpilintegrationservicesConstants.TO_UTILS_BILLING_ATTRIBUTE,
								new UpilPopulateOdataNavigationProperty(utilsBillingAttribute))
						.put(UpilintegrationservicesConstants.TO_UTILS_SALES_ATTRIBUTE,
								new UpilPopulateOdataNavigationProperty(utilsSalesAttribute))
						.put(UpilintegrationservicesConstants.TO_UTILS_ONE_TIME_CHAGRE,
								new UpilPopulateOdataNavigationProperty(utilsProductOneTimeCharges))
						.put(UpilintegrationservicesConstants.TO_UTILS_RECURRING_CHARGE,
								new UpilPopulateOdataNavigationProperty(utilsProductRecurringCharges))
						.put(UpilintegrationservicesConstants.TO_UTILS_USAGE_CHARGE,
								new UpilPopulateOdataNavigationProperty(utilsProductUsageCharge))
						.put(UpilintegrationservicesConstants.TO_UTILS_DISCOUNT,
								new UpilPopulateOdataNavigationProperty(utilsProductDiscounts))
						.build())
				.build();
		final ODataResponse upilResponse = EntityProvider.writeEntry(contentType, productEntitySet, productData, properties);
		final Object productEntity = upilResponse.getEntity();
		if (productEntity instanceof InputStream)
		{
			final byte[] productBuffer = IOUtils.toByteArray((InputStream) productEntity);
			try (OutputStream outputStream = upilConnection.getOutputStream())
			{
				outputStream.write(productBuffer);
			}
			catch (final IOException e)
			{
				LOG.info(e.getMessage(), e);
			}
		}
		ODataEntry productOdataentry = null;
		final HttpStatusCodes responseStatusCode = HttpStatusCodes.fromStatusCode(upilConnection.getResponseCode());

		if (responseStatusCode == HttpStatusCodes.CREATED)
		{
			try (InputStream upilInputStream = upilConnection.getInputStream())
			{
				final byte[] productResponseBytes = IOUtils.toByteArray(upilInputStream);
				upilInputStream.close();
				try (InputStream productResponseContent = new ByteArrayInputStream(productResponseBytes))
				{
					productOdataentry = EntityProvider.readEntry(contentType, productEntitySet, productResponseContent,
							EntityProviderReadProperties.init().build());
				}
			}
		}

		else
		{
			try (InputStream errorStream = upilConnection.getErrorStream())
			{
				if (errorStream != null)
				{
					final String error = IOUtils.toString(errorStream, Charset.defaultCharset());
					throw new IOException(error);
				}

			}
		}

		upilConnection.disconnect();
		return productOdataentry;
	}

	private InputStream getContent(final String metaDataUri, final String contentType, final String httpMethod,
			final String httpLanguage) throws IOException
	{
		final HttpURLConnection upilConnection = establishUpilConnection(metaDataUri, contentType, httpMethod, httpLanguage);
		upilConnection.connect();
		checkUpilConnectionStatus(upilConnection);

		final Map<String, List<String>> headerFields = upilConnection.getHeaderFields();

		cookieAndToken[0] = String.join(SEMI_COLON, headerFields.get(getHeaderKey(headerFields.keySet(), SET_COOKIE)));
		cookieAndToken[1] = String.join(SEMI_COLON, headerFields.get(getHeaderKey(headerFields.keySet(), X_CSRF_TOKEN)));

		InputStream content = null;
		try (InputStream upilInputStream = upilConnection.getInputStream())
		{
			final byte[] bytes = IOUtils.toByteArray(upilInputStream);
			content = new ByteArrayInputStream(bytes);
		}

		catch (final IOException e)
		{
			LOG.info(e.getMessage(), e);
		}

		finally
		{
			upilConnection.disconnect();
			if (content != null)
			{
				content.close();
			}
		}
		return content;
	}

	private String getHeaderKey(final Set<String> headerKeys, final String key)
	{
		return headerKeys.stream().filter(Objects::nonNull).filter(headerKey -> headerKey.equalsIgnoreCase(key)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Invalid header key: " + key));
	}

	private HttpURLConnection establishUpilConnection(final String upilAbsoluteUri, final String contentType,
			final String httpMethod, final String httpLanguage) throws IOException
	{
		final URL upilUrl = new URL(upilAbsoluteUri);
		final HttpURLConnection upilConnection = (HttpURLConnection) upilUrl.openConnection();
		upilConnection.setRequestProperty(HTTP_HEADER_ACCEPT, contentType);
		upilConnection.setRequestProperty(HTTP_HEADER_ACCEPT_LANGUAGE, httpLanguage);
		upilConnection.setRequestMethod(httpMethod);
		if (HTTP_METHOD_GET.equals(httpMethod))
		{
			upilConnection.setRequestProperty(X_CSRF_TOKEN, FETCH);
		}
		if (HTTP_METHOD_POST.equals(httpMethod))
		{
			upilConnection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
			upilConnection.setRequestProperty(COOKIE, sanitize(cookieAndToken[0]));
			upilConnection.setRequestProperty(X_CSRF_TOKEN, sanitize(cookieAndToken[1]));
			upilConnection.setDoOutput(true);
		}
		upilConnection.setRequestProperty(AUTHORIZATION,
				AUTHORIZATION_TYPE + new String(Base64.encodeBase64((UPIL_USERNAME + ":" + UPIL_PASSWORD).getBytes()), //NOSONAR
						StandardCharsets.UTF_8));
		return upilConnection;
	}

	protected String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}


	private void checkUpilConnectionStatus(final HttpURLConnection upilConnection) throws IOException
	{
		final HttpStatusCodes httpStatusCode = HttpStatusCodes.fromStatusCode(upilConnection.getResponseCode());
		if (ERROR_CODE_START <= httpStatusCode.getStatusCode() && httpStatusCode.getStatusCode() <= ERROR_CODE_END)
		{
			final String message = "Http Connection failed with status " + httpStatusCode.getStatusCode() + " "
					+ httpStatusCode.toString() +
					".\n\tRequest URL was: '" + upilConnection.getURL().toString() + "'.";
			throw new IOException(message);
		}
	}

	@Override
	public List<ODataEntry> getIsuSemantics() throws UpilintegrationservicesException
	{
		try
		{
			final ODataFeed feed = readFeed(readUpilEdm(UPIL_SERVICEURL_FOR_READ_UPIL_EDM), UPIL_SERVICEURL_FOR_SEMANTICS,
					APPLICATION_XML,
					UpilintegrationservicesConstants.UTILSATTRIBUTEVALUEVH);
			return feed.getEntries();
		}
		catch (IOException | ODataException e)
		{
			throw new UpilintegrationservicesException(e);
		}
	}

	@Override
	public List<ODataEntry> getIsuReferenceTypes() throws UpilintegrationservicesException
	{
		try
		{
			final ODataFeed feed = readFeed(readUpilEdm(UPIL_SERVICEURL_FOR_READ_UPIL_EDM), UPIL_SERVICEURL_FOR_REFPRODUCT,
					APPLICATION_XML,
					UpilintegrationservicesConstants.UTILSREFERENCEPRODUCT);
			return feed.getEntries();
		}
		catch (IOException | ODataException e)
		{
			throw new UpilintegrationservicesException(e);
		}
	}

	private ODataFeed readFeed(final Edm edm, final String serviceUri, final String contentType, final String entitySetName)
			throws IOException, ODataException
	{
		final EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		final String absolutUri = StringUtils.join(serviceUri, SEPARATOR, entitySetName);
		final InputStream content = getContent(absolutUri, contentType, HTTP_METHOD_GET, HTTP_LANGUAGE);
		return EntityProvider.readFeed(contentType, entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}

}
