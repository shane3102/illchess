import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameFinishedPopupUserInfoComponent } from './game-finished-popup-user-info.component';

describe('GameFinishedPopupUserInfoComponent', () => {
  let component: GameFinishedPopupUserInfoComponent;
  let fixture: ComponentFixture<GameFinishedPopupUserInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GameFinishedPopupUserInfoComponent]
    });
    fixture = TestBed.createComponent(GameFinishedPopupUserInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
