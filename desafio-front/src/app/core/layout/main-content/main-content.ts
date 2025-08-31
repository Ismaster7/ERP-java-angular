import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-main-content',
  imports: [RouterOutlet],
  templateUrl: './main-content.html',
  styleUrl: './main-content.scss'
})
export class MainContent {
contador = signal(2);

addContador(){
  this.contador.update(contador => contador *2);
}

}
