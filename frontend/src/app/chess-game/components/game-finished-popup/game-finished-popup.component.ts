import { Component } from '@angular/core';
import { faUser, faUserPlus, faUserMinus, faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-game-finished-popup',
  templateUrl: './game-finished-popup.component.html',
  styleUrls: ['./game-finished-popup.component.scss']
})
export class GameFinishedPopupComponent {

  user = faUser
  userWon = faUserPlus
  userLost = faUserMinus
  pointGain = faArrowUp
  pointLoss = faArrowDown

}
