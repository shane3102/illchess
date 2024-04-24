import { Component, Input } from '@angular/core';
import { faCheck, faCircle, faXRay } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-chess-game-offer-from-oponent',
  templateUrl: './chess-game-offer-from-oponent.component.html',
  styleUrls: ['./chess-game-offer-from-oponent.component.scss']
})
export class ChessGameOfferFromOponentComponent {

  @Input() info: string

  check = faCheck
  x = faCircle

}
