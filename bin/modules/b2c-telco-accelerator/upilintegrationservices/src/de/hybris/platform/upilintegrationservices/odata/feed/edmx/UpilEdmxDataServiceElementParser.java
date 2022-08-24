/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx;


import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import de.hybris.platform.upilintegrationservices.odata.feed.edmx.provider.UpilEdmxSchemaElementParserImpl;


/**
 * EDMX Data service element's interface
 * 
 * @since 1911
 */
public interface UpilEdmxDataServiceElementParser
{

	/**
	 * To parse edmx DataService element
	 * 
	 * @param startElement
	 *           DataService start element
	 * @param xmlEventReader
	 *           The XMLEventReader
	 * @throws XMLStreamException
	 *            Throws XMLStreamException
	 */
	void parse(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException;

	/**
	 * To get edmx schema element having entity types
	 * 
	 * @return List of EdmxSchemaImpl
	 */
	List<UpilEdmxSchemaElementParserImpl> getSchemas();

}
