import { Component, inject } from '@angular/core';
import { faDice, faInfoCircle, faMinus, faPlus } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { BotView } from 'src/app/shared/model/stockfish/BotView';
import { GenerateRandomNameService } from 'src/app/shared/service/GenerateRandomNameService';
import { addBots, deleteBots } from 'src/app/shared/state/bot/bot.actions';
import { botExpirationMinutes, botListSelector, botManagmentShown, botMaxCount } from 'src/app/shared/state/bot/bot.selectors';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';

@Component({
  selector: 'app-bot-managment',
  templateUrl: './bot-managment.component.html',
  styleUrls: ['./bot-managment.component.scss']
})
export class BotManagmentComponent {


  plusIcon = faPlus
  minusIcon = faMinus
  randomIcon = faDice
  infoIcon = faInfoCircle

  generateRandomNameService = inject(GenerateRandomNameService)
  store = inject(Store<ChessGameState>)

  bots$: Observable<BotView[] | undefined | null> = this.store.select(botListSelector)
  botManagmentShown$: Observable<boolean | undefined | null> = this.store.select(botManagmentShown)
  maxBotCount$: Observable<number | undefined | null> = this.store.select(botMaxCount)
  botExpirationMinutes$: Observable<number | undefined | null> = this.store.select(botExpirationMinutes)

  added: string

  addBot() {
    if(this.added != "") {
      this.store.dispatch(addBots({ username: this.added }))
      this.added = ""
    }
  }

  deleteBot(username: string) {
    this.store.dispatch(deleteBots({ username: username }))
  }

  randomName() {
    this.added = this.generateRandomNameService.generateRandomName()
  }

}
