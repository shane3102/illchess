import { Component, inject, Input } from '@angular/core';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { showOrHideBotsManagement } from 'src/app/shared/state/bot/bot.actions';
import { botManagmentShown } from 'src/app/shared/state/bot/bot.selectors';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';

@Component({
  selector: 'app-bots-header-button',
  templateUrl: './bots-header-button.component.html',
  styleUrls: ['./bots-header-button.component.scss']
})
export class BotsHeaderButtonComponent {
  @Input() text: string
  @Input() icon: IconProp

  store = inject(Store<ChessGameState>)

  botManagmentShown$: Observable<boolean | undefined | null> = this.store.select(botManagmentShown)

  isBeingHidden: boolean = false

  ngOnInit() {
    this.botManagmentShown$.subscribe(
      () => {
        this.isBeingHidden = true
        setTimeout(
          () => { this.isBeingHidden = false },
          250
        )
      }
    )
  }

  showOrHideBotsManagement() {
    this.store.dispatch(showOrHideBotsManagement({}))
  }
}
