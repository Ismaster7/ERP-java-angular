import { Component } from '@angular/core';
import { Sidebar } from '../sidebar/sidebar';
import { MainContent } from '../main-content/main-content';

@Component({
  selector: 'app-main',
  imports: [Sidebar, MainContent],
  standalone: true,
  templateUrl: './main.html',
  styleUrl: './main.scss'
})
export class Main {

}
