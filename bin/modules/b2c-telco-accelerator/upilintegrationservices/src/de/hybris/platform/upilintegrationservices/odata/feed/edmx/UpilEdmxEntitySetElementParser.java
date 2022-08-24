/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;


/**
 * Interface for the API to parse entity set
 * 
 * @since 1911
 */
public interface UpilEdmxEntitySetElementParser
{

	/**
	 * To parse entity set element of edmx
	 * 
	 * @param startElement
	 *           Entity container start element
	 * @param xmlEventReader
	 *           XML event reader
	 */
	void parse(StartElement startElement, XMLEventReader xmlEventReader);
}
