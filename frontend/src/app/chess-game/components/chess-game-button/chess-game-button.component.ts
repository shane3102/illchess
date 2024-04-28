import { Component, Input } from '@angular/core';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';

@Component({
  selector: 'app-chess-game-button',
  templateUrl: './chess-game-button.component.html',
  styleUrls: ['./chess-game-button.component.scss']
})
export class ChessGameButtonComponent {
  @Input() info: string
  @Input() icon: IconDefinition
  @Input() backgroundColor?: string
  @Input() disabled: boolean
}
