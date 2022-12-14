import { TranslateService } from '@ngx-translate/core';
import { DropdownPopulatorFetchPageResponse, DropdownPopulatorInterface, DropdownPopulatorItemPayload, DropdownPopulatorPagePayload, GenericEditorOption, LanguageService, UriDropdownPopulator } from 'smarteditcommons';
import { ContextAwareCatalogService } from '../../../services';
import { PopulatorItem } from './types';
export declare class ProductDropdownPopulator extends DropdownPopulatorInterface {
    private contextAwareCatalogService;
    private uriDropdownPopulator;
    constructor(contextAwareCatalogService: ContextAwareCatalogService, languageService: LanguageService, translateService: TranslateService, uriDropdownPopulator: UriDropdownPopulator);
    fetchPage(payload: DropdownPopulatorPagePayload): Promise<DropdownPopulatorFetchPageResponse<PopulatorItem>>;
    isPaged(): boolean;
    getItem(payload: DropdownPopulatorItemPayload): Promise<GenericEditorOption>;
}
