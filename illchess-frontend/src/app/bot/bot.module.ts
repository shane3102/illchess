import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BotManagmentComponent } from './components/bot-managment/bot-managment.component';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    BotManagmentComponent
  ],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
    BotManagmentComponent
  ]
})
export class BotModule { }
