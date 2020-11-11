import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { VersionInformation } from '../../../../assets/VersionInformation';
import { PaperCardComponent } from '../../polymer/paper-card/paper-card.component';
import { LatexTableComponent } from '../../output/latex-table/latex-table.component';
import { select, Store, Action } from '@ngrx/store';
import { IUCAppState } from '../../../redux/uc.app-state';
import { ConfigurationService } from '../configuration/configuration.service';
import { UCClickAction, UCDetailsAction, UCNewStateAction, UCSearchUpdateAction, UCTableOrderAction } from '../../../redux/uc.action';
import { isNullOrUndefined } from 'util';

import { saveAs } from 'file-saver';
import { Criteria, DataElement, Label } from '../../../../../lib/gulp/model/model.module';
import { ActivatedRouteSnapshot, Router, RouterState, RouterStateSnapshot } from '@angular/router';
import { state } from '@angular/core/src/animation/dsl';
import { masterReducer, NEW_STATE_ACTION } from '../../../redux/uc.reducers';

@Component({
    selector: 'comparison-wrapper',
    templateUrl: './comparison-wrapper.template.html',
    styleUrls: ['./comparison-wrapper.component.css']
})
export class ComparisonWrapperComponent {
    static instance;

    public repository: string;

    public routeSnapshot: RouterStateSnapshot;

    //@ViewChild(LatexTableComponent) latexTable: LatexTableComponent;
    @ViewChild('genericTableHeader') genericTableHeader: PaperCardComponent;
    public activeRow: DataElement = new DataElement('placeholder', '', '', new Map());

    public changed = 0;
    private versionInformation: VersionInformation = new VersionInformation();

    constructor(public configurationService: ConfigurationService,
                private cd: ChangeDetectorRef,
                private router: Router,
                public store: Store<IUCAppState>) {
        if (isNullOrUndefined(ComparisonComponent.instance)) {
            ComparisonComponent.instance = this;
        }
        const routerState: RouterState = router.routerState;
        this.routeSnapshot = routerState.snapshot;
        const root: ActivatedRouteSnapshot = this.routeSnapshot.root;
        this.configurationService.loadComparison(this.cd);
        this.repository = this.configurationService.configuration.repository;
        this.routeState();
        console.log('======> comparison-wrapper - routerState: ', routerState);
        console.log('======> comparison-wrapper - snapshot: ', this.routeSnapshot);
        console.log('======> comparison-wrapper - root: ', root);


    }

    routeState() {
        this.store.dispatch({type: 'NEW_STATE_ACTION', NEW_STATE_ACTION});
      }

    public getVersionInformation(): VersionInformation {
        return this.versionInformation;
    }

    public criteriaChanged(value: string, crit: Criteria) {
        const map = new Map<string, string>();
        map.set(crit.id, value || null);
        this.store.dispatch(new UCSearchUpdateAction(map));
        this.cd.markForCheck();
    }

    public getActive(state: { state: IUCAppState }, crit: Criteria) {
        if (isNullOrUndefined(state)) {
            return [];
        }
        const active = state.state.currentSearch.get(crit.id);

        if (!isNullOrUndefined(active)) {
            return Array.from(active).map(name => {
                return {
                    id: name,
                    text: name
                }
            });
        }

        return [];
    }

    public showDetails(index: number) {
        this.store.dispatch(new UCDetailsAction(ConfigurationService.data.dataElements[index]));
    }

    public deferredUpdate() {
        setTimeout(() => {
            this.changed > 0 ? (this.changed = this.changed - 100) : (this.changed = this.changed + 100);
        }, 100);
    }

    /**
     * Callback functions dispatching to redux store
     */
    public changeOrder(change: { index: number, ctrl: boolean }) {
        if (!isNullOrUndefined(change)) {
            this.store.dispatch(new UCTableOrderAction(change.index, change.ctrl));
        }
    }

    public criteriaClicked(val: { event: MouseEvent, key: Label, index: number }) {
        this.store.dispatch(new UCClickAction(val.key.name, val.index));
    }

    public dispatchNewState(newState: any) {
        this.store.dispatch(new UCNewStateAction(<IUCAppState>newState));
    }

    public closeDetails() {
        this.store.dispatch(new UCDetailsAction(null));
    }
}
