/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modeling.data;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.services.WriteService;
import de.hybris.platform.integrationbackoffice.widgets.common.data.IntegrationFilterState;
import de.hybris.platform.integrationbackoffice.widgets.modeling.builders.DataStructureBuilder;
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tree;

import com.hybris.cockpitng.util.notifications.NotificationService;

public class IntegrationObjectPresentation implements Serializable
{
	private final transient WriteService writeService;
	private final transient ReadService readService;
	private final transient DataStructureBuilder dataStructureBuilder;
	private final transient NotificationService notificationService;

	private Tree composedTypeTree;
	private Listbox attributesListBox;

	private transient IntegrationFilterState filterState = IntegrationFilterState.SHOW_ALL;
	private transient Map<ComposedTypeModel, List<AbstractListItemDTO>> currentAttributesMap = new HashMap<>();
	private transient Map<ComposedTypeModel, ItemTypeMatchEnum> itemTypeMatchMap = new HashMap<>();
	private transient Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> attributeDuplicationMap = new HashMap<>();
	private transient Set<SubtypeData> subtypeDataSet = new HashSet<>();
	private transient boolean isModified = false;

	private IntegrationObjectModel selectedIntegrationObject = null;
	private ComposedTypeModel selectedComposedType = null;
	private Deque<ComposedTypeModel> ancestors = new ArrayDeque<>();

	private boolean editModeFlag;

	public IntegrationObjectPresentation(final WriteService writeService, final ReadService readService,
	                                     final DataStructureBuilder dataStructureBuilder,
	                                     final NotificationService notificationService)
	{
		this.writeService = writeService;
		this.readService =  readService;
		this.dataStructureBuilder = dataStructureBuilder;
		this.notificationService = notificationService;
	}

	public void resetState()
	{
		currentAttributesMap.clear();
		itemTypeMatchMap.clear();
		attributeDuplicationMap.clear();
		subtypeDataSet.clear();
		isModified = false;

		selectedComposedType = null;
		selectedIntegrationObject = null;
		ancestors.clear();

		composedTypeTree.getTreechildren().getChildren().clear();
		attributesListBox.getItems().clear();
	}

	public boolean hasRoot()
	{
		return selectedIntegrationObject.getRootItem() != null;
	}

	public boolean isValidIOSelected()
	{
		return selectedIntegrationObject != null && hasRoot();
	}

	public IntegrationFilterState getFilterState()
	{
		return filterState;
	}

	public void setFilterState(final IntegrationFilterState filterState)
	{
		this.filterState = filterState;
	}

	public Map<ComposedTypeModel, List<AbstractListItemDTO>> getCurrentAttributesMap()
	{
		return currentAttributesMap;
	}

	public void setCurrentAttributesMap(
			final Map<ComposedTypeModel, List<AbstractListItemDTO>> currentAttributesMap)
	{
		this.currentAttributesMap = currentAttributesMap;
	}

	public Map<ComposedTypeModel, ItemTypeMatchEnum> getItemTypeMatchMap()
	{
		return itemTypeMatchMap;
	}

	public void setItemTypeMatchMap(
			final Map<ComposedTypeModel, ItemTypeMatchEnum> itemTypeMatchMap)
	{
		this.itemTypeMatchMap = itemTypeMatchMap;
	}

	public Set<SubtypeData> getSubtypeDataSet()
	{
		return subtypeDataSet;
	}

	public void setSubtypeDataSet(final Set<SubtypeData> subtypeDataSet)
	{
		this.subtypeDataSet = subtypeDataSet;
	}

	public boolean isModified()
	{
		return isModified;
	}

	public void setModified(final boolean modified)
	{
		isModified = modified;
	}

	public IntegrationObjectModel getSelectedIntegrationObject()
	{
		return selectedIntegrationObject;
	}

	public void setSelectedIntegrationObject(final IntegrationObjectModel selectedIntegrationObject)
	{
		this.selectedIntegrationObject = selectedIntegrationObject;
	}

	public Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> getAttributeDuplicationMap()
	{
		return attributeDuplicationMap;
	}

	public void setAttributeDuplicationMap(
			final Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> attributeDuplicationMap)
	{
		this.attributeDuplicationMap = attributeDuplicationMap;
	}

	public boolean isEditModeFlag()
	{
		return editModeFlag;
	}

	public void setEditModeFlag(final boolean editModeFlag)
	{
		this.editModeFlag = editModeFlag;
	}

	public WriteService getWriteService()
	{
		return writeService;
	}

	public ReadService getReadService()
	{
		return readService;
	}

	public NotificationService getNotificationService()
	{
		return notificationService;
	}

	public DataStructureBuilder getDataStructureBuilder()
	{
		return dataStructureBuilder;
	}

	public Tree getComposedTypeTree()
	{
		return composedTypeTree;
	}

	public void setComposedTypeTree(final Tree composedTypeTree)
	{
		this.composedTypeTree = composedTypeTree;
	}

	public Listbox getAttributesListBox()
	{
		return attributesListBox;
	}

	public void setAttributesListBox(final Listbox attributesListBox)
	{
		this.attributesListBox = attributesListBox;
	}

	public ComposedTypeModel getSelectedComposedType()
	{
		return selectedComposedType;
	}

	public void setSelectedComposedType(final ComposedTypeModel selectedComposedType)
	{
		this.selectedComposedType = selectedComposedType;
	}

	public Deque<ComposedTypeModel> getAncestors()
	{
		return ancestors;
	}

	public void setAncestors(final Deque<ComposedTypeModel> ancestors)
	{
		this.ancestors = ancestors;
	}

	public void clearTreeAndListbox()
	{
		getComposedTypeTree().getTreechildren().getChildren().clear();
		getAttributesListBox().getItems().clear();
		setAncestors(new ArrayDeque<>());
	}
}
