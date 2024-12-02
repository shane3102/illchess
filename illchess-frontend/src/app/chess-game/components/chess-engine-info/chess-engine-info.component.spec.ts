import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessEngineInfoComponent } from './chess-engine-info.component';

describe('ChessEngineInfoComponent', () => {
  let component: ChessEngineInfoComponent;
  let fixture: ComponentFixture<ChessEngineInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChessEngineInfoComponent]
    });
    fixture = TestBed.createComponent(ChessEngineInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
