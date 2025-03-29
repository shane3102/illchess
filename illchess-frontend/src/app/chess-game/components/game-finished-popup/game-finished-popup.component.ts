import { Component, Input } from '@angular/core';
import { faUser, faUserPlus, faUserMinus, faArrowDown, faArrowUp, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { BoardGameObtainedInfoView } from '../../../shared/model/game/BoardGameObtainedInfoView';
import { GameFinishedView } from '../../../shared/model/player-info/GameFinishedView';

@Component({
  selector: 'app-game-finished-popup',
  templateUrl: './game-finished-popup.component.html',
  styleUrls: ['./game-finished-popup.component.scss']
})
export class GameFinishedPopupComponent {

  @Input() boardGameObtainedInfoView: BoardGameObtainedInfoView | undefined | null
  @Input() gameFinishedView: GameFinishedView | undefined | null
  @Input() gameState: 'WHITE_WON' | 'BLACK_WON' | 'DRAW'

  user = faUser
  userWon = faUserPlus
  userLost = faUserMinus
  pointGain = faArrowUp
  pointLoss = faArrowDown
  pointNotChange = faArrowRight

}
