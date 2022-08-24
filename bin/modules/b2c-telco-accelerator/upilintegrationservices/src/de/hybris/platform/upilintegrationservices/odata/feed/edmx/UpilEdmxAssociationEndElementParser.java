/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;


/**
 * To Parse EDMX association if its an end element in reader provided
 * 
 * @since 1911
 */
public interface UpilEdmxAssociationEndElementParser
{

	/**
	 * To Parse EDMX association if its an end element
	 * 
	 * @param startElement
	 *           The startElement
	 * @param xmlEventReader
	 *           The xmlEventReader
	 */
	void parse(StartElement startElement, XMLEventReader xmlEventReader);
}
