/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed;

import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.callback.OnWriteFeedContent;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackResult;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;


/**
 * Populates navigation properties of OData service
 *
 * @since 1911
 */

public class UpilPopulateOdataNavigationProperty implements OnWriteFeedContent
{

	List<Map<String, Object>> feed;

	public UpilPopulateOdataNavigationProperty(final List<Map<String, Object>> feed)
	{
		this.feed = feed;
	}

	@Override
	public WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext writeFeedCallbackContext)
			throws ODataApplicationException
	{
		final EntityProviderWriteProperties currentWriteProperties = writeFeedCallbackContext.getCurrentWriteProperties();

		final WriteFeedCallbackResult result = new WriteFeedCallbackResult();
		result.setInlineProperties(currentWriteProperties);
		result.setFeedData(feed);

		return result;
	}

}
