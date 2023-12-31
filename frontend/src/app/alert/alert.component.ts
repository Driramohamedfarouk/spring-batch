import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit {

  @Input() show: boolean = false;
  @Input() executionTime : String = "";


  constructor() { }

  ngOnInit(): void {
  }

    close() {
        this.show = false
    }
}
