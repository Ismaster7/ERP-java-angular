import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-action-button',
  imports: [],
  templateUrl: './action-button.html',
  styleUrl: './action-button.scss'
})
export class ActionButton {
@Input() label: string = '';
}
