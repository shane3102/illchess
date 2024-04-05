import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessBoardAdditionalInfoComponent } from './chess-board-additional-info.component';

describe('ChessBoardAdditionalInfoComponent', () => {
  let component: ChessBoardAdditionalInfoComponent;
  let fixture: ComponentFixture<ChessBoardAdditionalInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessBoardAdditionalInfoComponent]
    });
    fixture = TestBed.createComponent(ChessBoardAdditionalInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
