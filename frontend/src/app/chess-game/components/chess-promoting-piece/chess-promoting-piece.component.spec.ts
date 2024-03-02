import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessPromotingPieceComponent } from './chess-promoting-piece.component';

describe('ChessPromotingPieceComponent', () => {
  let component: ChessPromotingPieceComponent;
  let fixture: ComponentFixture<ChessPromotingPieceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessPromotingPieceComponent]
    });
    fixture = TestBed.createComponent(ChessPromotingPieceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
