/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate;

/**
 * Interface for EdmxNavigationPropertyWrapper
 * 
 * @since 1911
 */
public interface UpilEdmxNavigationPropertyWrapper
{

	/**
	 * To get Navigation properties, associated elements java Pojo Name
	 * 
	 * @return {@link String} returns nav type
	 */
	String getJavaNavType();

	/**
	 * To get an indicator if association is with many.
	 * 
	 * @return returns boolean flag if multiple elements
	 */
	boolean isMultiple();

	/**
	 * To get Navigation properties java Pojo file name
	 * 
	 * @return {@link String} returns java name
	 */
	String getJavaName();

	/**
	 * To get the getter for Java name
	 * 
	 * @return {@link String} returns getter
	 */
	String getGetter();

	/**
	 * To get the setter for the Java name
	 * 
	 * @return {@link String} returns setter
	 */
	String getSetter();
}
