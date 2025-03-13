import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessGamePlayerPanelComponent } from './chess-game-player-panel.component';

describe('ChessGamePlayerPanelComponent', () => {
  let component: ChessGamePlayerPanelComponent;
  let fixture: ComponentFixture<ChessGamePlayerPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChessGamePlayerPanelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChessGamePlayerPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
