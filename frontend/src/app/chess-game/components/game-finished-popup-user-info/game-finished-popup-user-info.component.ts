import { Component, Input } from '@angular/core';
import { UserGameInfoView } from '../../model/GameFinishedView';
import { faArrowDown, faArrowRight, faArrowUp, faUser, faUserMinus, faUserPlus } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-game-finished-popup-user-info',
  templateUrl: './game-finished-popup-user-info.component.html',
  styleUrls: ['./game-finished-popup-user-info.component.scss']
})
export class GameFinishedPopupUserInfoComponent {

  @Input() userGameInfoView: UserGameInfoView 

  user = faUser
  userWon = faUserPlus
  userLost = faUserMinus
  pointGain = faArrowUp
  pointLoss = faArrowDown
  pointNotChange = faArrowRight

}
