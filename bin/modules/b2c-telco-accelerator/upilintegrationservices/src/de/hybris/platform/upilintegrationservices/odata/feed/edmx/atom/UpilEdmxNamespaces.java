/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.odata.feed.edmx.atom;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.builder.ToStringBuilder;




/**
 * This class defines Constants for EDMX name spaces and provides API to derive if element is start or end element.
 * 
 * @since 1911
 */
public class UpilEdmxNamespaces
{

	private static final String NAVIGATION_PROPERTY = "NavigationProperty";
	private static final String PROPERTY = "Property";
	private static final String PROPERTY_REF = "PropertyRef";
	private static final String PARAMETER = "Parameter";
	private static final String FUNCTION_IMPORT = "FunctionImport";
	private static final String ASSOCIATION_SET = "AssociationSet";
	private static final String ENTITY_SET = "EntitySet";
	private static final String ENTITY_CONTAINER = "EntityContainer";
	private static final String COMPLEX_TYPE = "ComplexType";
	private static final String ASSOCIATION = "Association";
	private static final String ENTITY_TYPE = "EntityType";
	private static final String SCHEMA = "Schema";
	public static final String NAME_SPACE_APP = "http://www.w3.org/2007/app";
	public static final String NAME_SPACE_XML = "http://www.w3.org/XML/1998/namespace";
	public static final String NAME_SPACE_ATOM = "http://www.w3.org/2005/Atom";

	public static final String NAME_SPACE_METADATA = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
	public static final String NAME_SPACE_DATASERVICES = "http://schemas.microsoft.com/ado/2007/08/dataservices";
	public static final String NAME_SPACE_EDMX_2006 = "http://schemas.microsoft.com/ado/2006/04/edm";
	public static final String EDM2007_NAPESPACE = "http://schemas.microsoft.com/ado/2007/05/edm";
	public static final String EDM2008_NAMESPACE = "http://schemas.microsoft.com/ado/2008/09/edm";
	public static final String EDM2009_NAMESPACE = "http://schemas.microsoft.com/ado/2009/08/edm";
	public static final String EDM2009_11_NAMESPACE = "http://schemas.microsoft.com/ado/2009/11/edm";

	public static final String EDMX_NAMESPACE = "http://schemas.microsoft.com/ado/2007/06/edmx";
	public static final QName EDMX = new QName(EDMX_NAMESPACE, "Edmx");

	public static final String EDMANNOTATION_NAMESPACE = "http://schemas.microsoft.com/ado/2009/02/edm/annotation";

	public static final QName EDMX_DATASERVICES = new QName(EDMX_NAMESPACE, "DataServices");

	public static final QName EDMSCHEMA_2006 = new QName(NAME_SPACE_EDMX_2006, SCHEMA);
	public static final QName EDMKEY_2006 = new QName(NAME_SPACE_EDMX_2006, "Key");
	public static final QName EDM_ENTITYTYPE_2006 = new QName(NAME_SPACE_EDMX_2006, ENTITY_TYPE);
	public static final QName EDM_ASSOCIATION_2006 = new QName(NAME_SPACE_EDMX_2006, ASSOCIATION);
	public static final QName EDM_COMPLEXTYPE_2006 = new QName(NAME_SPACE_EDMX_2006, COMPLEX_TYPE);
	public static final QName EDM_ENTITYCONTAINER_2006 = new QName(NAME_SPACE_EDMX_2006, ENTITY_CONTAINER);
	public static final QName EDM_ENTITYSET_2006 = new QName(NAME_SPACE_EDMX_2006, ENTITY_SET);
	public static final QName EDM_ASSOCIATIONSET_2006 = new QName(NAME_SPACE_EDMX_2006, ASSOCIATION_SET);
	public static final QName EDM_FUNCTIONIMPORT_2006 = new QName(NAME_SPACE_EDMX_2006, FUNCTION_IMPORT);
	public static final QName EDM_PARAMETER_2006 = new QName(NAME_SPACE_EDMX_2006, PARAMETER);
	public static final QName EDM_END_2006 = new QName(NAME_SPACE_EDMX_2006, "End");
	public static final QName EDM_PROPERTYREF_2006 = new QName(NAME_SPACE_EDMX_2006, PROPERTY_REF);
	public static final QName EDM_PROPERTY_2006 = new QName(NAME_SPACE_EDMX_2006, PROPERTY);
	public static final QName EDM_NAVIGATIONPROPERTY_2006 = new QName(NAME_SPACE_EDMX_2006, NAVIGATION_PROPERTY);

	public static final QName EDM_SCHEMA_2007 = new QName(EDM2007_NAPESPACE, SCHEMA);
	public static final QName EDM_KEY_2007 = new QName(EDM2007_NAPESPACE, "Key");
	public static final QName EDM_ENTITYTYPE_2007 = new QName(EDM2007_NAPESPACE, ENTITY_TYPE);
	public static final QName EDM_ASSOCIATION_2007 = new QName(EDM2007_NAPESPACE, ASSOCIATION);
	public static final QName EDM_COMPLEXTYPE_2007 = new QName(EDM2007_NAPESPACE, COMPLEX_TYPE);
	public static final QName EDM_ENTITYCONTAINER_2007 = new QName(EDM2007_NAPESPACE, ENTITY_CONTAINER);
	public static final QName EDM_ENTITYSET_2007 = new QName(EDM2007_NAPESPACE, ENTITY_SET);
	public static final QName EDM_ASSOCIATIONSET_2007 = new QName(EDM2007_NAPESPACE, ASSOCIATION_SET);
	public static final QName EDM_FUNCTIONIMPORT_2007 = new QName(EDM2007_NAPESPACE, FUNCTION_IMPORT);
	public static final QName EDM_PARAMETER_2007 = new QName(EDM2007_NAPESPACE, PARAMETER);
	public static final QName EDM_END_2007 = new QName(EDM2007_NAPESPACE, "End");
	public static final QName EDM_PROPERTYREF_2007 = new QName(EDM2007_NAPESPACE, PROPERTY_REF);
	public static final QName EDM_PROPERTY_2007 = new QName(EDM2007_NAPESPACE, PROPERTY);
	public static final QName EDM_NAVIGATIONPROPERTY_2007 = new QName(EDM2007_NAPESPACE, NAVIGATION_PROPERTY);

	public static final QName EDM_SCHEMA_2008 = new QName(EDM2008_NAMESPACE, SCHEMA);
	public static final QName EDM_KEY_2008 = new QName(EDM2008_NAMESPACE, "Key");
	public static final QName EDM_ENTITYTYPE_2008 = new QName(EDM2008_NAMESPACE, ENTITY_TYPE);
	public static final QName EDM_ASSOCIATION_2008 = new QName(EDM2008_NAMESPACE, ASSOCIATION);
	public static final QName EDM_COMPLEXTYPE_2008 = new QName(EDM2008_NAMESPACE, COMPLEX_TYPE);
	public static final QName EDM_ENTITYCONTAINER_2008 = new QName(EDM2008_NAMESPACE, ENTITY_CONTAINER);
	public static final QName EDM_ENTITYSET_2008 = new QName(EDM2008_NAMESPACE, ENTITY_SET);
	public static final QName EDM_ASSOCIATIONSET_2008 = new QName(EDM2008_NAMESPACE, ASSOCIATION_SET);
	public static final QName EDM_FUNCTIONIMPORT_2008 = new QName(EDM2008_NAMESPACE, FUNCTION_IMPORT);
	public static final QName EDM_PARAMETER_2008 = new QName(EDM2008_NAMESPACE, PARAMETER);
	public static final QName EDM_END_2008 = new QName(EDM2008_NAMESPACE, "End");
	public static final QName EDM_PROPERTYREF_2008 = new QName(EDM2008_NAMESPACE, PROPERTY_REF);
	public static final QName EDM_PROPERTY_2008 = new QName(EDM2008_NAMESPACE, PROPERTY);
	public static final QName EDM_NAVIGATIONPROPERTY_2008 = new QName(EDM2008_NAMESPACE, NAVIGATION_PROPERTY);

	public static final QName EDM_SCHEMA_2009 = new QName(EDM2009_NAMESPACE, SCHEMA);
	public static final QName EDM_KEY_2009 = new QName(EDM2009_NAMESPACE, "Key");
	public static final QName EDM_ENTITYTYPE_2009 = new QName(EDM2009_NAMESPACE, ENTITY_TYPE);
	public static final QName EDM_ASSOCIATION_2009 = new QName(EDM2009_NAMESPACE, ASSOCIATION);
	public static final QName EDM_COMPLEXTYPE_2009 = new QName(EDM2009_NAMESPACE, COMPLEX_TYPE);
	public static final QName EDM_ENTITYCONTAINER_2009 = new QName(EDM2009_NAMESPACE, ENTITY_CONTAINER);
	public static final QName EDM_ENTITYSET_2009 = new QName(EDM2009_NAMESPACE, ENTITY_SET);
	public static final QName EDM_ASSOCIATIONSET_2009 = new QName(EDM2009_NAMESPACE, ASSOCIATION_SET);
	public static final QName EDM_FUNCTIONIMPORT_2009 = new QName(EDM2009_NAMESPACE, FUNCTION_IMPORT);
	public static final QName EDM_PARAMETER_2009 = new QName(EDM2009_NAMESPACE, PARAMETER);
	public static final QName EDM_END_2009 = new QName(EDM2009_NAMESPACE, "End");
	public static final QName EDM_PROPERTYREF_2009 = new QName(EDM2009_NAMESPACE, PROPERTY_REF);
	public static final QName EDM_PROPERTY_2009 = new QName(EDM2009_NAMESPACE, PROPERTY);
	public static final QName EDM_NAVIGATIONPROPERTY_2009 = new QName(EDM2009_NAMESPACE, NAVIGATION_PROPERTY);

	public static final QName EDM_SCHEMA_2009_11 = new QName(EDM2009_11_NAMESPACE, SCHEMA);
	public static final QName EDM_KEY_2009_11 = new QName(EDM2009_11_NAMESPACE, "Key");
	public static final QName EDM_ENTITYTYPE_2009_11 = new QName(EDM2009_11_NAMESPACE, ENTITY_TYPE);
	public static final QName EDM_ASSOCIATION_2009_11 = new QName(EDM2009_11_NAMESPACE, ASSOCIATION);
	public static final QName EDM_COMPLEXTYPE_2009_11 = new QName(EDM2009_11_NAMESPACE, COMPLEX_TYPE);
	public static final QName EDM_ENTITYCONTAINER_2009_11 = new QName(EDM2009_11_NAMESPACE, ENTITY_CONTAINER);
	public static final QName EDM_ENTITYSET_2009_11 = new QName(EDM2009_11_NAMESPACE, ENTITY_SET);
	public static final QName EDM_ASSOCIATIONSET_2009_11 = new QName(EDM2009_11_NAMESPACE, ASSOCIATION_SET);
	public static final QName EDM_FUNCTIONIMPORT_2009_11 = new QName(EDM2009_11_NAMESPACE, FUNCTION_IMPORT);
	public static final QName EDM_PARAMETER_2009_11 = new QName(EDM2009_11_NAMESPACE, PARAMETER);
	public static final QName EDM_END_2009_11 = new QName(EDM2009_11_NAMESPACE, "End");
	public static final QName EDM_PROPERTYREF_2009_11 = new QName(EDM2009_11_NAMESPACE, PROPERTY_REF);
	public static final QName EDM_PROPERTY_2009_11 = new QName(EDM2009_11_NAMESPACE, PROPERTY);
	public static final QName EDM_NAVIGATIONPROPERTY_2009_11 = new QName(EDM2009_11_NAMESPACE, NAVIGATION_PROPERTY);

	public static final QName ATOM_FEED = new QName(NAME_SPACE_ATOM, "feed");
	public static final QName ATOM_ENTRY = new QName(NAME_SPACE_ATOM, "entry");
	public static final QName ATOM_ID = new QName(NAME_SPACE_ATOM, "id");
	public static final QName ATOM_TITLE = new QName(NAME_SPACE_ATOM, "title");
	public static final QName ATOM_SUMMARY = new QName(NAME_SPACE_ATOM, "summary");
	public static final QName ATOM_UPDATED = new QName(NAME_SPACE_ATOM, "updated");
	public static final QName ATOM_CATEGORY = new QName(NAME_SPACE_ATOM, "category");
	public static final QName ATOM_CONTENT = new QName(NAME_SPACE_ATOM, "content");
	public static final QName ATOM_LINK = new QName(NAME_SPACE_ATOM, "link");

	public static final QName APP_WORKSPACE = new QName(NAME_SPACE_APP, "workspace");
	public static final QName APP_SERVICE = new QName(NAME_SPACE_APP, "service");
	public static final QName APP_COLLECTION = new QName(NAME_SPACE_APP, "collection");
	public static final QName APP_ACCEPT = new QName(NAME_SPACE_APP, "accept");

	public static final QName METADATA_ETAG = new QName(NAME_SPACE_METADATA, "etag");
	public static final QName METADATA_PROPERTIES = new QName(NAME_SPACE_METADATA, "properties");
	public static final QName METADATA_INLINE = new QName(NAME_SPACE_METADATA, "inline");
	public static final QName METADATA_COUNT = new QName(NAME_SPACE_METADATA, "count");
	public static final QName METADATA_TYPE = new QName(NAME_SPACE_METADATA, "type");
	public static final QName METADATA_NULL = new QName(NAME_SPACE_METADATA, "null");
	public static final QName METADATA_FC_TARGETPATH = new QName(NAME_SPACE_METADATA, "FC_TargetPath");
	public static final QName METADATA_FC_CONTENTKIND = new QName(NAME_SPACE_METADATA, "FC_ContentKind");
	public static final QName METADATA_FC_KEEPINCONTENT = new QName(NAME_SPACE_METADATA, "FC_KeepInContent");
	public static final QName METADATA_FC_EPMCONTENTKIND = new QName(NAME_SPACE_METADATA, "FC_EpmContentKind");
	public static final QName METADATA_FC_EPMKEEPINCONTENT = new QName(NAME_SPACE_METADATA, "FC_EpmKeepInContent");

	public static final List<QName> SCHEMAS = Collections
			.unmodifiableList(Arrays.asList(EDMSCHEMA_2006, EDM_SCHEMA_2007, EDM_SCHEMA_2008,
					EDM_SCHEMA_2009,
					EDM_SCHEMA_2009_11));
	public static final List<QName> KEYS = Collections
			.unmodifiableList(Arrays.asList(EDMKEY_2006, EDM_KEY_2007, EDM_KEY_2008, EDM_KEY_2009,
					EDM_KEY_2009_11));
	public static final List<QName> ENTITY_TYPES = Collections
			.unmodifiableList(Arrays.asList(EDM_ENTITYTYPE_2006, EDM_ENTITYTYPE_2007,
					EDM_ENTITYTYPE_2008, EDM_ENTITYTYPE_2009, EDM_ENTITYTYPE_2009_11));
	public static final List<QName> PROPERTIES = Collections
			.unmodifiableList(Arrays.asList(EDM_PROPERTY_2006, EDM_PROPERTY_2007, EDM_PROPERTY_2008,
					EDM_PROPERTY_2009, EDM_PROPERTY_2009_11));
	public static final List<QName> NAV_PROPS = Collections
			.unmodifiableList(Arrays.asList(EDM_NAVIGATIONPROPERTY_2006, EDM_NAVIGATIONPROPERTY_2007,
					EDM_NAVIGATIONPROPERTY_2008, EDM_NAVIGATIONPROPERTY_2009, EDM_NAVIGATIONPROPERTY_2009_11));
	public static final List<QName> PROP_REFS = Collections
			.unmodifiableList(Arrays.asList(EDM_PROPERTYREF_2006, EDM_PROPERTYREF_2007,
					EDM_PROPERTYREF_2008, EDM_PROPERTYREF_2009, EDM_PROPERTYREF_2009_11));
	public static final List<QName> ASSOCIATIONS = Collections
			.unmodifiableList(Arrays.asList(EDM_ASSOCIATION_2006, EDM_ASSOCIATION_2007,
					EDM_ASSOCIATION_2008, EDM_ASSOCIATION_2009, EDM_ASSOCIATION_2009_11));
	public static final List<QName> ENDS = Collections
			.unmodifiableList(Arrays.asList(EDM_END_2006, EDM_END_2007,
					EDM_END_2008, EDM_END_2009, EDM_END_2009_11));
	public static final List<QName> COMPLEX_TYPES = Collections
			.unmodifiableList(Arrays.asList(EDM_COMPLEXTYPE_2006, EDM_COMPLEXTYPE_2007,
					EDM_COMPLEXTYPE_2008, EDM_COMPLEXTYPE_2009, EDM_COMPLEXTYPE_2009_11));
	public static final List<QName> ENTITY_CONTAINERS = Collections
			.unmodifiableList(Arrays.asList(EDM_ENTITYCONTAINER_2006, EDM_ENTITYCONTAINER_2007,
					EDM_ENTITYCONTAINER_2008, EDM_ENTITYCONTAINER_2009, EDM_ENTITYCONTAINER_2009_11));
	public static final List<QName> ENTITY_SETS = Collections
			.unmodifiableList(Arrays.asList(EDM_ENTITYSET_2006, EDM_ENTITYSET_2007, EDM_ENTITYSET_2008,
					EDM_ENTITYSET_2009, EDM_ENTITYSET_2009_11));
	public static final List<QName> ASSOCIATION_SETS = Collections
			.unmodifiableList(Arrays.asList(EDM_ASSOCIATIONSET_2006, EDM_ASSOCIATIONSET_2007,
					EDM_ASSOCIATIONSET_2008, EDM_ASSOCIATIONSET_2009, EDM_ASSOCIATIONSET_2009_11));


	private static final String BASE = "base";
	private static final String NAME = "Name";
	private static final String NAME_SPACE_CONTEXT = "NamespaceContext";
	private static final String SCHEMA_TYPE = "SchemaType";
	public static final String NULL = "null";
	public static final String EDM_BINARY = "Edm.Binary";
	public static final String EDM_BOOLEAN = "Edm.Boolean";
	public static final String EDM_BYTE = "Edm.Byte";
	public static final String EDM_DATE_TIME = "Edm.DateTime";
	public static final String EDM_DECIMAL = "Edm.Decimal";
	public static final String EDM_DOUBLE = "Edm.Double";
	public static final String EDM_SINGLE = "Edm.Single";
	public static final String EDM_GUID = "Edm.Guid";
	public static final String EDM_INT_16 = "Edm.Int16";
	public static final String EDM_INT_32 = "Edm.Int32";
	public static final String EDM_INT_64 = "Edm.Int64";
	public static final String EDM_S_BYTE = "Edm.SByte";
	public static final String EDM_STRING = "Edm.String";
	public static final String EDM_TIME = "Edm.Time";
	public static final String EDM_DATE_TIME_OFFSET = "Edm.DateTimeOffset";

	private UpilEdmxNamespaces()
	{
		throw new IllegalStateException("EdmxNamespaces class");
	}

	public static final QName XML_BASE = new QName(NAME_SPACE_XML, BASE);


	public static boolean isStartElement(final XMLEvent event, final List<QName> names)
	{
		return event.isStartElement() && names.contains(event.asStartElement().getName());
	}


	public static boolean isStartElement(final XMLEvent event, final QName name)
	{
		return event.isStartElement() && name.equals(event.asStartElement().getName());
	}


	public static boolean isEndElement(final XMLEvent event, final List<QName> names)
	{
		return event.isEndElement() && names.contains(event.asEndElement().getName());
	}


	public static boolean isEndElement(final XMLEvent event, final QName name)
	{
		return event.isEndElement() && name.equals(event.asEndElement().getName());
	}


	public static String printStartElement(final StartElement startElement)
	{
		return new ToStringBuilder(startElement).append(NAME, startElement.getName().toString())
				.append(NAME_SPACE_CONTEXT, startElement.getNamespaceContext()).append(SCHEMA_TYPE, startElement.getSchemaType())
				.toString();

	}



}
