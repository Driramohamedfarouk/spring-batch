import { Component } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'batch-front';
  showAlert = false ;
  showAlertFailure = false ;
  executionTime = "" ;
  jobName = "" ;
  nbLinesFrom  : number = 1000 ;
  nbLinesTo : number = 10000 ;
  nbLinesStep : number = 1000 ;
  stepSizeFrom : number  = 1;
  stepSizeTo : number  = 10;
  stepSizeStep : number  = 2;
  chunkSizeFrom : number = 1;
  chunkSizeTo : number  = 10;
  chunkSizeStep : number = 1;

  constructor(private http: HttpClient) {
  }

  submitForm() {

    this.http.post<any>('http://localhost:8081/api', {
      jobName : this.jobName,
      nbLinesToReadRange : {
        from : this.nbLinesFrom ,
        to : this.nbLinesTo ,
        step : this.nbLinesStep
      },
      stepSizeRange :{
        from : this.stepSizeFrom ,
        to : this.stepSizeTo ,
        step : this.stepSizeStep
      },
      chunkSizeRange : {
        from : this.chunkSizeFrom ,
        to : this.chunkSizeTo ,
        step : this.chunkSizeStep
      }
    })
      .subscribe(
        response => {
          console.log("finished executing all jobs !!") ;
        },
        error => {
          console.error(error);
        }
      );
  }
}
