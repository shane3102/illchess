import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessBoardMiniComponent } from './chess-board-mini.component';

describe('ChessBoardMiniComponent', () => {
  let component: ChessBoardMiniComponent;
  let fixture: ComponentFixture<ChessBoardMiniComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessBoardMiniComponent]
    });
    fixture = TestBed.createComponent(ChessBoardMiniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
