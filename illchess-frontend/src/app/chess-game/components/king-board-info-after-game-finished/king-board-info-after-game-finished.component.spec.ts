import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KingBoardInfoAfterGameFinishedComponent } from './king-board-info-after-game-finished.component';

describe('KingBoardInfoAfterGameFinishedComponent', () => {
  let component: KingBoardInfoAfterGameFinishedComponent;
  let fixture: ComponentFixture<KingBoardInfoAfterGameFinishedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KingBoardInfoAfterGameFinishedComponent]
    });
    fixture = TestBed.createComponent(KingBoardInfoAfterGameFinishedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
