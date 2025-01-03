import { Component } from '@angular/core';
import { faGamepad, faHome, faRobot } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  gamepad = faGamepad
  home = faHome
  robot = faRobot

}
