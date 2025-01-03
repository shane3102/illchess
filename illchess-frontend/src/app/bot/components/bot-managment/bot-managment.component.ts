import { Component, inject } from '@angular/core';
import { faMinus, faPlus } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { BotView } from 'src/app/shared/model/stockfish/BotView';
import { addBots, deleteBots, loadCurrentllyRunBots } from 'src/app/shared/state/bot/bot.actions';
import { botListSelector, botManagmentShown } from 'src/app/shared/state/bot/bot.selectors';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';

@Component({
  selector: 'app-bot-managment',
  templateUrl: './bot-managment.component.html',
  styleUrls: ['./bot-managment.component.scss']
})
export class BotManagmentComponent {

  plusIcon = faPlus
  minusIcon = faMinus

  store = inject(Store<ChessGameState>)
  bots$: Observable<BotView[] | undefined | null> = this.store.select(botListSelector)
  botManagmentShown$: Observable<boolean | undefined | null> = this.store.select(botManagmentShown)

  added: string

  addBot() {
    this.store.dispatch(addBots({username: this.added}))
  }

  deleteBot(username: string) {
    this.store.dispatch(deleteBots({username: username}))
  }


}
