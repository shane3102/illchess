import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessPieceMiniComponent } from './chess-piece-mini.component';

describe('ChessPieceMiniComponent', () => {
  let component: ChessPieceMiniComponent;
  let fixture: ComponentFixture<ChessPieceMiniComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessPieceMiniComponent]
    });
    fixture = TestBed.createComponent(ChessPieceMiniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
