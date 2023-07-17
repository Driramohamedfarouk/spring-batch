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
  showAlert = false ;
  showAlertFailure = false ;
  executionTime = "" ;


  constructor(private http: HttpClient) {
  }

  submitForm() {
    //const queryParams = { chunkSize: this.chunkSize };
    this.http.post<any>('http://localhost:8080/batch/multi',null,{
      params : {
        chunkSize : this.chunkSize ,
        stepSize : this.stepSize
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
