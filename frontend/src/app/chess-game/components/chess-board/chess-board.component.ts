import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit {

  ranks: number[] = [8,7,6,5,4,3,2,1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  constructor() { }

  ngOnInit(): void {
  }

}
