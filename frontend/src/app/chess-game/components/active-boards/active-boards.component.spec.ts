import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveBoardsComponent } from './active-boards.component';

describe('ActiveBoardsComponent', () => {
  let component: ActiveBoardsComponent;
  let fixture: ComponentFixture<ActiveBoardsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActiveBoardsComponent]
    });
    fixture = TestBed.createComponent(ActiveBoardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
