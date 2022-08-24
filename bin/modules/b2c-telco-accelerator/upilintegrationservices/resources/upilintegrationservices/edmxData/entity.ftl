/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.data;

import java.io.Serializable;
import java.util.List;

/**
 * This class defines template for pojo's using className to typeWrapper and property wrapper for properties to define getter's and setters.
 * @since 1907
 */
public class ${typeWrapper.className} implements Serializable {

<#-- Private Fields -->
<#list typeWrapper.propWrappers?sort_by("javaName") as property>
    private ${property.javaClassType} ${property.javaName}<#if property.defaultValue??> = property.defaultValue</#if>;
</#list>
<#-- private Association Fields -->
<#list typeWrapper.navPropWrappers?sort_by("javaName") as navPropWrapper>
    private <#if navPropWrapper.multiple>List<${navPropWrapper.javaNavType}><#else>${navPropWrapper.javaNavType}</#if> ${navPropWrapper.javaName};
</#list>

<#-- public field getters -->
<#list typeWrapper.propWrappers?sort_by("javaName") as property>
    public ${property.javaClassType} ${property.getter}() {
        return this.${property.javaName};
    }

</#list>
<#-- public Association getters -->
<#list typeWrapper.navPropWrappers?sort_by("javaName") as navPropWrapper>
    public <#if navPropWrapper.multiple>List<${navPropWrapper.javaNavType}><#else> ${navPropWrapper.javaNavType}</#if> ${navPropWrapper.getter}() {
        return this.${navPropWrapper.javaName};
    }

</#list>
<#-- publid field setters -->
<#list typeWrapper.propWrappers?sort_by("javaName") as property>
    public void ${property.setter}(${property.javaClassType} ${property.javaName}) {
        this.${property.javaName} = ${property.javaName};
    }

</#list>
<#-- public Association setters -->
<#list typeWrapper.navPropWrappers?sort_by("javaName") as navPropWrapper>
    public void ${navPropWrapper.setter}(<#if navPropWrapper.multiple>List<${navPropWrapper.javaNavType}><#else> ${navPropWrapper.javaNavType}</#if> ${navPropWrapper.javaName}){
        this.${navPropWrapper.javaName} = ${navPropWrapper.javaName};
    }

</#list>

}



