import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { ChessGameState } from '../../state/chess-game.state';
import { initializeBoard } from '../../state/board/board.actions';
import { InitializeBoardRequest } from '../../model/InitializeBoardRequest';
import { initializedBoardIdSelector } from '../../state/board/board.selectors';
import { Router } from '@angular/router';
import { RefreshBoardDto } from '../../model/RefreshBoardRequest';

@Component({
  selector: 'app-join-or-initialize-game',
  templateUrl: './join-or-initialize-game.component.html',
  styleUrls: ['./join-or-initialize-game.component.scss']
})
export class JoinOrInitializeGameComponent implements OnInit {

  randomNames: string[] = ["Mark", "Tom", "Pablo", "Jose", "William"]

  username: string = this.randomNames[Math.floor(Math.random() * this.randomNames.length)] + Math.floor(100 * Math.random())

  constructor(
    private store: Store<ChessGameState>,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    let initializeNewBoardRequest: InitializeBoardRequest = {
      'username': this.username
    }
    this.store.dispatch(initializeBoard(initializeNewBoardRequest))

    this.store.select(initializedBoardIdSelector).subscribe(
      (dto: RefreshBoardDto) => {
        this.router.navigateByUrl(`/board/${dto.boardId}/${this.username}`)
      }
    )
  }

}
