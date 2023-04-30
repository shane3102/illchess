import { Component, OnInit } from '@angular/core';
import { Piece, PieceColor, PieceInfo } from '../../model/PieceInfo';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit {

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  constructor() { }

  ngOnInit(): void {
  }

  getpieceBySquare(file: string, rank: number): PieceInfo | undefined {

    let piece: PieceInfo = new PieceInfo()

    if (rank > 2 && rank < 7) {
      return undefined;
    }
    if (rank == 1 || rank == 2) {
      piece.color = PieceColor.WHITE
    }
    if (rank == 8 || rank == 7) {
      piece.color = PieceColor.BLACK
    }


    if (file == 'A' || file == 'H') {
      piece.piece = Piece.ROOK
    }

    if (file == 'B' || file == 'G') {
      piece.piece = Piece.KNIGHT
    }

    if (file == 'C' || file == 'F') {
      piece.piece = Piece.BISHOP
    }

    if (file == 'E') {
      piece.piece = Piece.KING
    }

    if (file == 'D') {
      piece.piece = Piece.QUEEN
    }

    if (rank == 7 || rank == 2) {
      piece.piece = Piece.PAWN
    }

    return piece;
  }

}
