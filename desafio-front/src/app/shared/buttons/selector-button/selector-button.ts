import { Component,  EventEmitter,  Input, Output } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-selector-button',
  imports: [RouterModule, MatIcon],
  standalone: true,
  templateUrl: './selector-button.html',
  styleUrl: './selector-button.scss'
})
export class SelectorButton {
@Input() link: string = '';
@Input() label: string = '';


}
