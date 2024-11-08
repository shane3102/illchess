import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedPlayerInfoTablesComponent } from './shared-player-info-tables.component';

describe('SharedPlayerInfoTablesComponent', () => {
  let component: SharedPlayerInfoTablesComponent;
  let fixture: ComponentFixture<SharedPlayerInfoTablesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SharedPlayerInfoTablesComponent]
    });
    fixture = TestBed.createComponent(SharedPlayerInfoTablesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
