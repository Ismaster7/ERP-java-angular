import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NotificationService } from '../../../core/services/notification.service';
@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  template: `
    <div class="modal-overlay" (click)="onOverlayClick($event)">
      <div class="modal-container">
        <div class="modal-header">
          <h3>{{ title }}</h3>
          <button class="close-btn" (click)="onCancel()">×</button>
        </div>
        <div class="modal-body">
          <p>{{ message }}</p>
        </div>
        <div class="modal-footer">
          <button class="btn btn-cancel" (click)="onCancel()">Cancelar</button>
          <button class="btn btn-confirm" (click)="onConfirm()">Confirmar</button>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./confirm-modal.scss']
})
export class ConfirmModalComponent {

  constructor(private notificationService: NotificationService) {}
  @Input() title: string = 'Confirmação';
  @Input() message: string = 'Tem certeza que deseja realizar esta ação?';
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();


  onConfirm() {
    this.confirm.emit();
    this.notificationService.success( 'Ação confirmada com sucesso.', "sucesso");
  }

  onCancel() {
    this.cancel.emit();
  }

  onOverlayClick(event: MouseEvent) {
    if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.onCancel();
    }
  }
}
