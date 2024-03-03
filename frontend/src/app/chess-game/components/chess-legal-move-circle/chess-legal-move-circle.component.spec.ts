import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessLegalMoveCircleComponent } from './chess-legal-move-circle.component';

describe('ChessLegalMoveCircleComponent', () => {
  let component: ChessLegalMoveCircleComponent;
  let fixture: ComponentFixture<ChessLegalMoveCircleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessLegalMoveCircleComponent]
    });
    fixture = TestBed.createComponent(ChessLegalMoveCircleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
