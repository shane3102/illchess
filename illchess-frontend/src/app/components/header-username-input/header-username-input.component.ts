import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { changeUsername, generateRandomUsername } from 'src/app/shared/state/player-info/player-info.actions';
import { PlayerInfoState } from 'src/app/shared/state/player-info/player-info.reducers';
import { username } from 'src/app/shared/state/player-info/player-info.selectors';

@Component({
  selector: 'app-header-username-input',
  templateUrl: './header-username-input.component.html',
  styleUrls: ['./header-username-input.component.scss']
})
export class HeaderUsernameInputComponent implements OnInit {

  store = inject(Store<PlayerInfoState>)
  username$: Observable<string | undefined> = this.store.select(username)

  changeUsername(usernameInput: HTMLInputElement) {
    this.store.dispatch(changeUsername({ username: usernameInput.value }))
  }

  ngOnInit() {
    if (!localStorage.getItem('username')) {
      this.store.dispatch(generateRandomUsername({}))
    }
  }

}
