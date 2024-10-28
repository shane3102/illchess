import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameFinishedPopupComponent } from './game-finished-popup.component';

describe('GameFinishedPopupComponent', () => {
  let component: GameFinishedPopupComponent;
  let fixture: ComponentFixture<GameFinishedPopupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GameFinishedPopupComponent]
    });
    fixture = TestBed.createComponent(GameFinishedPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
