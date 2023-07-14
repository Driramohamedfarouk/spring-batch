import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { AlertComponent } from './alert/alert.component';
import { AlertFailureComponent } from './alert-failure/alert-failure.component';

@NgModule({
  declarations: [
    AppComponent,
    AlertComponent,
    AlertFailureComponent
  ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
