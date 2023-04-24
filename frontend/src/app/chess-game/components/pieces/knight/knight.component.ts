import { Component, Input, OnInit } from '@angular/core';
import { PieceColor } from 'src/app/chess-game/model/PieceInfo';

@Component({
  selector: 'app-knight',
  templateUrl: './knight.component.html',
  styleUrls: ['./knight.component.scss']
})
export class KnightComponent implements OnInit {

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
  }

}
