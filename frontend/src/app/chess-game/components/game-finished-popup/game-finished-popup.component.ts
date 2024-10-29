import { Component, Input } from '@angular/core';
import { faUser, faUserPlus, faUserMinus, faArrowDown, faArrowUp, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { BoardGameObtainedInfoView } from '../../model/BoardGameObtainedInfoView';
import { GameFinishedView } from '../../model/GameFinishedView';

@Component({
  selector: 'app-game-finished-popup',
  templateUrl: './game-finished-popup.component.html',
  styleUrls: ['./game-finished-popup.component.scss']
})
export class GameFinishedPopupComponent {

  @Input() boardGameObtainedInfoView: BoardGameObtainedInfoView
  @Input() gameFinishedView: GameFinishedView | undefined | null

  user = faUser
  userWon = faUserPlus
  userLost = faUserMinus
  pointGain = faArrowUp
  pointLoss = faArrowDown
  pointNotChange = faArrowRight

}
