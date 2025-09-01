import { Component,  } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Main } from './core/layout/main/main';
import { NotificationComponent } from './shared/components/notification/notification';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Main, NotificationComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'desafio-front';
}
