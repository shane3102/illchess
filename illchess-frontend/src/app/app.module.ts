import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ChessGameModule } from './chess-game/chess-game.module';
import { HttpClientModule } from '@angular/common/http';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { MainPageComponent } from './components/main-page/main-page.component';
import { HeaderComponent } from './components/header/header.component';
import { HeaderButtonComponent } from './components/header-button/header-button.component';
import { PlayerInfoModule } from "./player-info/player-info.module";
import { HeaderUsernameInputComponent } from './components/header-username-input/header-username-input.component';
import { BotModule } from './bot/bot.module';
import { BotsHeaderButtonComponent } from './components/bots-header-button/bots-header-button.component';

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    HeaderComponent,
    HeaderButtonComponent,
    HeaderUsernameInputComponent,
    BotsHeaderButtonComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule,
    ChessGameModule,
    PlayerInfoModule,
    BotModule,
    HttpClientModule,
    FontAwesomeModule
  ],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
