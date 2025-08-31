import { Component,  } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Main } from './core/layout/main/main';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Main],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'desafio-front';
}
