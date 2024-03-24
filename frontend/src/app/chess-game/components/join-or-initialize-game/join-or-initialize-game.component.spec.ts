import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinOrInitializeGameComponent } from './join-or-initialize-game.component';

describe('JoinOrInitializeGameComponent', () => {
  let component: JoinOrInitializeGameComponent;
  let fixture: ComponentFixture<JoinOrInitializeGameComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [JoinOrInitializeGameComponent]
    });
    fixture = TestBed.createComponent(JoinOrInitializeGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
