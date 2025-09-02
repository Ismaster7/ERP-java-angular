import { Component } from '@angular/core';
import { SidebarComponent } from '../sidebar/sidebar';
import { MainContent } from '../main-content/main-content';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-main',
  imports: [RouterOutlet, SidebarComponent, MainContent],
  standalone: true,
  templateUrl: './main.html',
  styleUrl: './main.scss'
})
export class Main {

}
