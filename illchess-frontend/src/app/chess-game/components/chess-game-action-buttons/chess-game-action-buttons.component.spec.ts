import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessGameActionButtonsComponent } from './chess-game-action-buttons.component';

describe('ChessGameActionButtonsComponent', () => {
  let component: ChessGameActionButtonsComponent;
  let fixture: ComponentFixture<ChessGameActionButtonsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessGameActionButtonsComponent]
    });
    fixture = TestBed.createComponent(ChessGameActionButtonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
