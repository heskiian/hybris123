/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;


/**
 * To parse EDMX association set till element ends
 * 
 * @since 1911
 */
public interface UpilEdmxAssociationSetEndElementParser
{

	/**
	 * To parse EDMX association set with its end element
	 * 
	 * @param startElement
	 *           The StartElement
	 * @param xmlEventReader
	 *           The XMLEventReader
	 * @throws XMLStreamException
	 *            Throws XMLStreamException exception
	 */
	void parse(StartElement startElement, XMLEventReader xmlEventReader);

}
