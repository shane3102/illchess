import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ChessGameModule } from './chess-game/chess-game.module';
import { StompService } from './chess-game/service/StompService';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule,
    ChessGameModule,
    HttpClientModule
  ],
  providers: [StompService],
  bootstrap: [AppComponent]
})
export class AppModule { }
