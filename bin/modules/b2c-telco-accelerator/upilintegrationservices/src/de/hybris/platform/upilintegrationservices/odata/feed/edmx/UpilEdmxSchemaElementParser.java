/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;


/**
 * Interface for the API to parse edmx Schema element
 * 
 * @since 1911
 */
public interface UpilEdmxSchemaElementParser
{

	/**
	 * To parse Edmx schema element
	 * 
	 * @param startElement
	 *           {@link StartElement} start element of edmx schema
	 * @param xmlEventReader
	 *           {@link XMLEventReader} reader
	 * @throws XMLStreamException
	 *            Exception throws by this API
	 */
	void parse(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException;
}
