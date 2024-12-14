import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaitingForBlackPopupComponent } from './waiting-for-black-popup.component';

describe('WaitingForBlackPopupComponent', () => {
  let component: WaitingForBlackPopupComponent;
  let fixture: ComponentFixture<WaitingForBlackPopupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WaitingForBlackPopupComponent]
    });
    fixture = TestBed.createComponent(WaitingForBlackPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
