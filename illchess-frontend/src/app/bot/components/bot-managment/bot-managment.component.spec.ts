import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BotManagmentComponent } from './bot-managment.component';

describe('BotManagmentComponent', () => {
  let component: BotManagmentComponent;
  let fixture: ComponentFixture<BotManagmentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BotManagmentComponent]
    });
    fixture = TestBed.createComponent(BotManagmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
