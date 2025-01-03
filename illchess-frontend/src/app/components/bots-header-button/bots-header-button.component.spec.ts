import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BotsHeaderButtonComponent } from './bots-header-button.component';

describe('BotsHeaderButtonComponent', () => {
  let component: BotsHeaderButtonComponent;
  let fixture: ComponentFixture<BotsHeaderButtonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BotsHeaderButtonComponent]
    });
    fixture = TestBed.createComponent(BotsHeaderButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
