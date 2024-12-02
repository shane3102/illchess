import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessGameOfferFromOponentComponent } from './chess-game-offer-from-oponent.component';

describe('ChessGameOfferFromOponentComponent', () => {
  let component: ChessGameOfferFromOponentComponent;
  let fixture: ComponentFixture<ChessGameOfferFromOponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessGameOfferFromOponentComponent]
    });
    fixture = TestBed.createComponent(ChessGameOfferFromOponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
