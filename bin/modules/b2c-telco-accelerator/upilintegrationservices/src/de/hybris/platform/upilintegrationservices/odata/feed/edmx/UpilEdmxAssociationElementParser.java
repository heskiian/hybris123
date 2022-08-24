/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;


/**
 * Class to parse EDMX association if its not an end element in reader
 *
 * @since 1911
 */
public interface UpilEdmxAssociationElementParser
{

	/**
	 * To parse EDMX association entity type.
	 *
	 * @param startElement
	 *           The StartElement
	 * @param xmlEventReader
	 *           The XMLEventReader
	 * @throws XMLStreamException
	 *            Throws exception XMLStreamException
	 */
	void parse(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException;

}
