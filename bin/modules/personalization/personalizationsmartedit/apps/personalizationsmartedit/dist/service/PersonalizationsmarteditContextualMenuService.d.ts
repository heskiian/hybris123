import { CrossFrameEventService, TypedMap } from 'smarteditcommons';
import { LoDashStatic } from 'lodash';
import { PersonalizationsmarteditContextService } from 'personalizationsmartedit/service/PersonalizationsmarteditContextServiceInner';
import { PersonalizationsmarteditComponentHandlerService } from 'personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService';
import { PersonalizationsmarteditUtils } from 'personalizationcommons';
import { PersonalizationsmarteditContextMenuServiceProxy } from 'personalizationsmartedit/contextMenu/PersonalizationsmarteditContextMenuServiceInnerProxy';
export declare class PersonalizationsmarteditContextualMenuService {
    protected personalizationsmarteditContextService: PersonalizationsmarteditContextService;
    protected personalizationsmarteditComponentHandlerService: PersonalizationsmarteditComponentHandlerService;
    protected personalizationsmarteditUtils: PersonalizationsmarteditUtils;
    protected personalizationsmarteditContextMenuServiceProxy: PersonalizationsmarteditContextMenuServiceProxy;
    protected crossFrameEventService: CrossFrameEventService;
    protected EVENTS: TypedMap<string>;
    private lodash;
    static readonly EDIT_PERSONALIZATION_IN_WORKFLOW = "personalizationsmartedit.editPersonalizationInWorkflow.enabled";
    contextPersonalization: any;
    contextCustomize: any;
    contextCombinedView: any;
    contextSeData: any;
    private isWorkflowRunningBoolean;
    constructor(personalizationsmarteditContextService: PersonalizationsmarteditContextService, personalizationsmarteditComponentHandlerService: PersonalizationsmarteditComponentHandlerService, personalizationsmarteditUtils: PersonalizationsmarteditUtils, personalizationsmarteditContextMenuServiceProxy: PersonalizationsmarteditContextMenuServiceProxy, crossFrameEventService: CrossFrameEventService, EVENTS: TypedMap<string>, lodash: LoDashStatic);
    init(): void;
    refreshContext(): void;
    openDeleteAction(config: any): void;
    openAddAction(config: any): void;
    openEditAction(config: any): void;
    openEditComponentAction(config: any): void;
    isCustomizeObjectValid(customize: any): boolean;
    isContextualMenuEnabled(): boolean;
    isElementHighlighted(config: any): boolean;
    isSlotInCurrentCatalog(config: any): boolean;
    isComponentInCurrentCatalog(config: any): boolean;
    isSelectedCustomizationFromCurrentCatalog(): boolean;
    isCustomizationFromCurrentCatalog(config: any): boolean;
    isEditPersonalizationInWorkflowAllowed(condition: boolean): boolean;
    isPersonalizationAllowedInWorkflow(): boolean;
    isContextualMenuAddItemEnabled(config: any): boolean;
    isContextualMenuEditItemEnabled(config: any): boolean;
    isContextualMenuDeleteItemEnabled(config: any): boolean;
    isContextualMenuShowActionListEnabled(config: any): boolean;
    isContextualMenuInfoEnabled(): boolean;
    isContextualMenuInfoItemEnabled(): boolean;
    isContextualMenuEditComponentItemEnabled(config: any): boolean;
    private getSelectedVariationCode;
    private getSelectedCustomization;
    private getSlotsToRefresh;
    private updateWorkflowStatus;
}
