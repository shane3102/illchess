import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BotManagmentComponent } from './components/bot-managment/bot-managment.component';



@NgModule({
  declarations: [
    BotManagmentComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    BotManagmentComponent
  ]
})
export class BotModule { }
