import { Component } from '@angular/core';
import { faAngleDoubleLeft, faArrowAltCircleUp, faFlag, faHandshake } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-chess-game-action-buttons',
  templateUrl: './chess-game-action-buttons.component.html',
  styleUrls: ['./chess-game-action-buttons.component.scss']
})
export class ChessGameActionButtonsComponent {

  handshake = faHandshake
  flag = faFlag
  back=faAngleDoubleLeft

}
