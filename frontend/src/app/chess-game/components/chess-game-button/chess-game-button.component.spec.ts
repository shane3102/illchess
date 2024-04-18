import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessGameButtonComponent } from './chess-game-button.component';

describe('ChessGameButtonComponent', () => {
  let component: ChessGameButtonComponent;
  let fixture: ComponentFixture<ChessGameButtonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessGameButtonComponent]
    });
    fixture = TestBed.createComponent(ChessGameButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
