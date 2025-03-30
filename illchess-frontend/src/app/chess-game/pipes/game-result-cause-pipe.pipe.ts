import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'gameResultCausePipe',
  standalone: true
})
export class GameResultCausePipePipe implements PipeTransform {

  transform(value: 'CHECKMATE' | 'RESIGNATION' | 'STALEMATE' | 'INSUFFICIENT_MATERIAL' | 'PLAYER_AGREEMENT', ...args: unknown[]): string {
    if(value == 'CHECKMATE') {
      return 'mat'
    } else if(value == 'RESIGNATION') {
      return 'rezygnacje'
    } else if(value == 'STALEMATE') {
      return 'pat'
    } else if(value == 'INSUFFICIENT_MATERIAL') {
      return 'brak materiału matującego'
    } else {
      return 'zgodę graczy'
    } 
  }

}
