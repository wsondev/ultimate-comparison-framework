import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { ComparisonModule } from './components/comparison/comparison/comparison.module';
import { StoreModule } from '@ngrx/store';
import { masterReducer } from './redux/uc.reducers';
import { RouterModule } from '@angular/router';
import { ComparisonComponent } from './components/comparison/comparison/comparison.component';
import { RouterStateSerializer, StoreRouterConnectingModule } from '@ngrx/router-store';
import { APP_BASE_HREF } from '@angular/common';
import { CustomRouterStateSerializer } from './redux/custom-router-state-serializer';
import { ComparisonWrapperModule } from './components/comparison/comparison-wrapper/comparison-wrapper.module';

@NgModule({
    imports: [
        BrowserModule,
        ComparisonModule,
        ComparisonWrapperModule,
        StoreModule.forRoot({
            state: masterReducer
        }),
        RouterModule.forRoot([{
            path: '', component: ComparisonComponent
        }], {useHash: false}),
        StoreRouterConnectingModule
    ],
    declarations: [
        AppComponent,
    ],
    providers: [
        {provide: APP_BASE_HREF, useValue: window['_app_base'] || '/'},
        {provide: RouterStateSerializer, useClass: CustomRouterStateSerializer}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
