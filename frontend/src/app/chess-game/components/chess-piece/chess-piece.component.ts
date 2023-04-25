import { Component, Input, OnInit } from '@angular/core';
import { Piece, PieceInfo } from '../../model/PieceInfo';

@Component({
  selector: 'app-chess-piece',
  templateUrl: './chess-piece.component.html',
  styleUrls: ['./chess-piece.component.scss']
})
export class ChessPieceComponent implements OnInit {

  Piece = Piece

  @Input() pieceInfo: PieceInfo;

  constructor() { }

  ngOnInit(): void {
  }

}
