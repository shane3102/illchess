import { Pipe, PipeTransform } from '@angular/core';
import { GameSnippetView } from 'src/app/shared/model/player-info/GameSnippetView';
import { TableCellData } from '../components/common-table/common-table.component';
import { faArrowDown, faArrowRight, faArrowUp } from '@fortawesome/free-solid-svg-icons';

@Pipe({
  name: 'latestGamesToTableCellData'
})
export class LatestGamesToTableCellDataPipe implements PipeTransform {

  pointGain = faArrowUp
  pointLoss = faArrowDown
  pointNotChange = faArrowRight

  transform(value: GameSnippetView[] | null | undefined): TableCellData[][]  {
    if (!value) {
      return []
    }
    return value.map(
      it => [
        {
          content: it.whiteUsername
        },
        {
          content: it.whiteUserPointChange.toString(),
          color: it.whiteUserPointChange < 0 ? 'red' : (it.whiteUserPointChange > 0 ? 'green' : undefined),
          icon: it.whiteUserPointChange < 0 ? this.pointLoss : (it.whiteUserPointChange > 0 ? this.pointGain : this.pointNotChange)
        },
        {
          content: it.blackUserPointChange.toString(),
          color: it.blackUserPointChange < 0 ? 'red' : (it.blackUserPointChange > 0 ? 'green' : undefined),
          icon: it.blackUserPointChange < 0 ? this.pointLoss : (it.blackUserPointChange > 0 ? this.pointGain : this.pointNotChange)
        },
        {
          content: it.blackUsername
        }
      ]
    );
  }

}
