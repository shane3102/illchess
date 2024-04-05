import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CapturedPiecesComponent } from './captured-pieces.component';

describe('CapturedPiecesComponent', () => {
  let component: CapturedPiecesComponent;
  let fixture: ComponentFixture<CapturedPiecesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CapturedPiecesComponent]
    });
    fixture = TestBed.createComponent(CapturedPiecesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
