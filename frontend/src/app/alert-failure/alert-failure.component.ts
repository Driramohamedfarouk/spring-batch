import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-alert-failure',
  templateUrl: './alert-failure.component.html',
  styleUrls: ['./alert-failure.component.css']
})
export class AlertFailureComponent implements OnInit {

  @Input() show: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  close() {
    this.show = false
  }
}
