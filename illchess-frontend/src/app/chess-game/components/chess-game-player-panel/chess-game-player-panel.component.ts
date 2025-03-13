import { Component, Input } from '@angular/core';
import { BoardAdditionalInfoView } from 'src/app/shared/model/game/BoardAdditionalInfoView';
import { PieceColor } from 'src/app/shared/model/game/PieceInfo';

@Component({
  selector: 'app-chess-game-player-panel',
  templateUrl: './chess-game-player-panel.component.html',
  styleUrl: './chess-game-player-panel.component.scss'
})
export class ChessGamePlayerPanelComponent {

  PieceColor = PieceColor

  @Input() username: string;
  @Input() pieceColor: PieceColor
  @Input() boardAdditionalInfoView: BoardAdditionalInfoView
}
