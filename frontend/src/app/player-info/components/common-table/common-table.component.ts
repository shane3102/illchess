import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faAngleDoubleDown, faAngleDoubleUp, faCaretDown, faCaretUp } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-common-table',
  templateUrl: './common-table.component.html',
  styleUrls: ['./common-table.component.scss']
})
export class CommonTableComponent implements OnInit {

  @Input() labels: LabelInfo[]
  @Input() dataList: TableCellData[][]
  @Input() totalPagesCount: number | null | undefined
  @Input() pageNumber: number
  @Input() pageSize: number

  pageSizeIndexNumbers: number[]

  @Output() nextPageEmitter: EventEmitter<{ pageNumber: number, pageSize: number, totalPages: number }> = new EventEmitter()
  @Output() previousPageEmitter: EventEmitter<{ pageNumber: number, pageSize: number, totalPages: number }> = new EventEmitter()
  @Output() loadDataEmitter: EventEmitter<{ pageNumber: number, pageSize: number }> = new EventEmitter()

  caretUp = faCaretUp
  caretDown = faCaretDown
  anglesUp = faAngleDoubleUp
  anglesDown = faAngleDoubleDown

  ngOnInit(): void {
    this.loadData()
  }

  nextPage() {
    this.dataList = []
    this.nextPageEmitter.next({ pageNumber: this.pageNumber + 1, pageSize: this.pageSize, totalPages: this.totalPagesCount! })
  }

  previousPage() {
    this.dataList = []
    this.previousPageEmitter.next({ pageNumber: this.pageNumber - 1, pageSize: this.pageSize, totalPages: this.totalPagesCount! })
  }

  loadData() {
    this.loadDataEmitter.next({ pageNumber: this.pageNumber, pageSize: this.pageSize })
    this.pageSizeIndexNumbers = Array(this.pageSize).fill(this.pageSize).map((_x, i) => i)
  }

  isDisabledUp() {
    return this.pageNumber <= 0
  }

  isDisabledDown() {
    return this.pageNumber >= this.totalPagesCount!
  }

}

export interface TableCellData {
  content: string,
  color?: string
  icon?: IconDefinition,
}

export interface LabelInfo {
  content: string,
  width?: string
}
