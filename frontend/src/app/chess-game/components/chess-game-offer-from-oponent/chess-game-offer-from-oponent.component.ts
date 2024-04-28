import { Component, EventEmitter, Input, Output } from '@angular/core';
import { faCheck, faCircle, faXRay } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-chess-game-offer-from-oponent',
  templateUrl: './chess-game-offer-from-oponent.component.html',
  styleUrls: ['./chess-game-offer-from-oponent.component.scss']
})
export class ChessGameOfferFromOponentComponent {

  @Input() info: string
  @Output() accept: EventEmitter<void> = new EventEmitter()
  @Output() decline: EventEmitter<void> = new EventEmitter()

  check = faCheck
  x = faCircle

  acceptOffer() {
    this.accept.next()
  }

  declineOffer() {
    this.decline.next()
  }

}
