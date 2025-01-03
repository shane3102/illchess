import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BotManagmentComponent } from './components/bot-managment/bot-managment.component';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';



@NgModule({
  declarations: [
    BotManagmentComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    FontAwesomeModule
  ],
  exports: [
    BotManagmentComponent
  ]
})
export class BotModule { }
