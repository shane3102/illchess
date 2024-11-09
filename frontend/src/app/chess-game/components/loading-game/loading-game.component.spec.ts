import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadingGameComponent } from './loading-game.component';

describe('LoadingGameComponent', () => {
  let component: LoadingGameComponent;
  let fixture: ComponentFixture<LoadingGameComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoadingGameComponent]
    });
    fixture = TestBed.createComponent(LoadingGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
