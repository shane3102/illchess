import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderUsernameInputComponent } from './header-username-input.component';

describe('HeaderUsernameInputComponent', () => {
  let component: HeaderUsernameInputComponent;
  let fixture: ComponentFixture<HeaderUsernameInputComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderUsernameInputComponent]
    });
    fixture = TestBed.createComponent(HeaderUsernameInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
