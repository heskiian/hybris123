/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate;


/**
 * Interface for Edmx Type wrapper
 * 
 * @since 1911
 */
public interface UpilEdmxTypeWrapper
{

	/**
	 * To get the java name for the entity type
	 * 
	 * @return {@link String} returns java name
	 */
	String getJavaName();

	/**
	 * To get the capitalize name of the entity types java name
	 * 
	 * @return {@link String} returns class name
	 */
	String getClassName();

	/**
	 * To get full class name of entity type along with package name
	 * 
	 * @return {@link String} returns full class name
	 */
	String getFullClassName();

	/**
	 * To get package name for the entity type
	 * 
	 * @return {@link String} returns package name
	 */
	String getPackageName();

	/**
	 * Provides entity type's formatted class name with its package name
	 * 
	 * @return {@link String} returns edmx type class path name
	 */
	String print();

}
