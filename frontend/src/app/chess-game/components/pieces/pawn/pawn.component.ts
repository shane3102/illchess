import { Component, Input, OnInit } from '@angular/core';
import { PieceColor } from 'src/app/chess-game/model/PieceInfo';

@Component({
  selector: 'app-pawn',
  templateUrl: './pawn.component.html',
  styleUrls: ['./pawn.component.scss']
})
export class PawnComponent implements OnInit {

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
  }

}
