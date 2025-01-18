import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent {

  @Input() size: number = 10
  ringSize: number
  marginAndBorderSize: number

  ngOnInit(): void {
    this.ringSize = this.size * 4/5
    this.marginAndBorderSize = this.size * 1/10
  }

}
