import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChessGameComponent } from './chess-game/components/chess-game/chess-game.component';
import { JoinOrInitializeGameComponent } from './chess-game/components/join-or-initialize-game/join-or-initialize-game.component';
import { MainPageComponent } from './components/main-page/main-page.component';

const routes: Routes = [
  { path: '', component: MainPageComponent},
  { path: 'join-or-initialize', component: JoinOrInitializeGameComponent},
  { path: 'game/:gameId', component: ChessGameComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
