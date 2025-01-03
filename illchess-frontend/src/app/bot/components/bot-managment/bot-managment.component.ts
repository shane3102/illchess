import { Component, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { BotView } from 'src/app/shared/model/stockfish/BotView';
import { addBots, deleteBots } from 'src/app/shared/state/bot/bot.actions';
import { botListSelector, botManagmentShown } from 'src/app/shared/state/bot/bot.selectors';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';

@Component({
  selector: 'app-bot-managment',
  templateUrl: './bot-managment.component.html',
  styleUrls: ['./bot-managment.component.scss']
})
export class BotManagmentComponent {

  store = inject(Store<ChessGameState>)
  bots$: Observable<BotView[] | undefined | null> = this.store.select(botListSelector)
  botManagmentShown$: Observable<boolean | undefined | null> = this.store.select(botManagmentShown)

  added: string
  deleted: string

  addBot() {
    this.store.dispatch(addBots({username: this.added}))
  }

  deleteBot() {
    this.store.dispatch(deleteBots({username: this.deleted}))
  }


}
