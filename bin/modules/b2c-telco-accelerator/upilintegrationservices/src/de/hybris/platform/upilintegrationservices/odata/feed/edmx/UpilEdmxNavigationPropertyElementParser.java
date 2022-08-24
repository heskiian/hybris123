/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx;


import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;


/**
 * Interface for the API to parse edmx navigation property
 * 
 * @since 1911
 */
public interface UpilEdmxNavigationPropertyElementParser
{

	/**
	 * To parse Edmx navigation property
	 * 
	 * @param startElement
	 *           {@link StartElement} start element of navigation property
	 * @param xmlEventReader
	 *           {@link XMLEventReader} reader
	 */
	void parse(StartElement startElement, XMLEventReader xmlEventReader);

}
