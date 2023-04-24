import { Component, Input, OnInit } from '@angular/core';
import { PieceColor } from 'src/app/chess-game/model/PieceInfo';

@Component({
  selector: 'app-rook',
  templateUrl: './rook.component.html',
  styleUrls: ['./rook.component.scss']
})
export class RookComponent implements OnInit {

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
  }

}
