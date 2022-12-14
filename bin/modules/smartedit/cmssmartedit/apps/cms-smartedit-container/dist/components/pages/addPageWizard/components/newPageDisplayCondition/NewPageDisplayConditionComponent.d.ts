import { ChangeDetectorRef, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ICMSPage, IPageService } from 'cmscommons';
import { FetchStrategy, ICatalogVersion, IUriContext, IUrlService, L10nPipe, LogService } from 'smarteditcommons';
import { DisplayConditionsFacade } from '../../../../../facades';
import { HomepageService, IDisplayCondition, PageDisplayConditionsService } from '../../../../../services';
interface DisplayConditionDTO {
    homepage: boolean;
    isPrimary: boolean;
    primaryPage?: ICMSPage;
}
export declare class NewPageDisplayConditionComponent implements OnInit, OnChanges {
    private urlService;
    private homepageService;
    private displayConditionsFacade;
    private translateService;
    private pageService;
    private logService;
    private pageDisplayConditions;
    private l10n;
    private cdr;
    pageTypeCode: string;
    uriContext: IUriContext;
    resultFn?: (data: DisplayConditionDTO) => void;
    pageUuid?: string;
    initialConditionSelectedKey?: string;
    targetCatalogVersion?: ICatalogVersion;
    conditions: IDisplayCondition[];
    conditionSelected: IDisplayCondition;
    conditionSelectorFetchStrategy: FetchStrategy;
    currentHomePageName: string;
    isReady: boolean;
    homepage: boolean;
    showReplaceLabel: boolean;
    primarySelected: ICMSPage;
    primarySelectedModel: string;
    primaryPageChoicesFetchStrategy: FetchStrategy;
    onDataChange: () => void;
    private homepageDetails;
    constructor(urlService: IUrlService, homepageService: HomepageService, displayConditionsFacade: DisplayConditionsFacade, translateService: TranslateService, pageService: IPageService, logService: LogService, pageDisplayConditions: PageDisplayConditionsService, l10n: L10nPipe, cdr: ChangeDetectorRef);
    ngOnInit(): void;
    ngOnChanges(changes: SimpleChanges): Promise<void>;
    showPrimarySelector(): boolean;
    onConditionChange(selectedConditionLabel: string): void;
    onHomePageChange(isHomepage: boolean): void;
    showHomePageWidget(): boolean;
    primarySelectedModelOnChange(uid: string): Promise<void>;
    private dataChanged;
    private isPrimaryContentPage;
    private updateHomepageUiProperties;
    private getTranslatedNames;
    private getResults;
    private getSelectedPrimaryPageAndDisplayCondition;
    private getAllPrimaryDisplayCondition;
    private getOnlyPrimaryDisplayCondition;
    private isUriContextEqualToCatalogVersion;
    private setPrimarySelected;
}
export {};
