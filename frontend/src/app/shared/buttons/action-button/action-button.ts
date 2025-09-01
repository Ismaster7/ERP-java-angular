import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-action-button',
  imports: [RouterModule],
  templateUrl: './action-button.html',
  styleUrl: './action-button.scss'
})
export class ActionButton {
@Input() label: string = '';
@Input() link: string = '';

 @Output() click = new EventEmitter<Event>();

handleClick(event: Event) {
  // Impede o comportamento padrão do botão
  this.click.emit(event);
}
}
