import { Component, Input, OnInit } from '@angular/core';
import { PieceColor } from 'src/app/chess-game/model/PieceInfo';

@Component({
  selector: 'app-bishop',
  templateUrl: './bishop.component.html',
  styleUrls: ['./bishop.component.scss']
})
export class BishopComponent implements OnInit {

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
  }

}
