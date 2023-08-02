import { Component } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'batch-front';
  chunkSize: number = 10 ;
  stepSize: number = 1 ;
  nbLinesToRead: number = 1000;
  showAlert = false ;
  showAlertFailure = false ;
  executionTime = "" ;
  jobName = "" ;


  constructor(private http: HttpClient) {
  }

  submitForm() {
    this.http.post<any>('http://localhost:8080/batch',null,{
      params : {
        jobName : this.jobName,
        chunkSize : this.chunkSize ,
        stepSize : this.stepSize,
        nbLinesToRead : this.nbLinesToRead
      }
    })
      .subscribe(
        response => {
          if(response.status == "COMPLETED"){
            this.executionTime = response.executionTime ;
            this.showAlert = true ;
          }else{
            this.showAlertFailure = true ;
          }
        },
        error => {
          console.error(error);
        }
      );
  }
}
