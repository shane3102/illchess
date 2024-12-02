import { Pipe, PipeTransform } from '@angular/core';
import { PlayerView } from 'src/app/shared/model/player-info/PlayerView';
import { TableCellData } from '../components/common-table/common-table.component';

@Pipe({
  name: 'playerViewToTableCellData'
})
export class PlayerViewToTableCellDataPipe implements PipeTransform {

  transform(value: PlayerView[] | null | undefined): TableCellData[][] {
    if (!value) {
      return []
    }
    return value.map(
      it => [
        {
          content: it.username
        },
        {
          content: it.currentRanking.toString()
        }
      ]
    );
  }

}
