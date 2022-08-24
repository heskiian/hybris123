/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx;

import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;


/**
 * Interface for the API to parse edmx meta data
 * 
 * @since 1911
 */
public interface UpilEdmxMetadataElementParser
{


	/**
	 * To parse meta data of edmx
	 * 
	 * @param startElement
	 *           metaData start element
	 * @param xmlEventReader
	 *           XML event reader
	 * @throws XMLStreamException
	 *            Throws exception XMLStreamException
	 */
	void parse(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException;

	/**
	 * To get list of data service elements
	 * 
	 * @return List of UpilEdmxDataService elements
	 */
	List<UpilEdmxDataServiceElementParser> getDataServices();

}
