import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="notification-container" *ngIf="notifications$ | async as notifications">
      <div
        *ngFor="let notification of notifications"
        class="notification"
        [ngClass]="notification.type"
      >
        <div class="message">{{ notification.title }}</div>
        <div class="progress">
          <div
            class="progress-bar"
            [style.width.%]="notification.progress"
          ></div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .notification-container {
      position: fixed;
      top: 20px;
      right: 20px;
      width: 300px;
      display: flex;
      flex-direction: column;
      gap: 12px;
      z-index: 1050;
    }

    .notification {
      padding: 12px 16px;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.2);
      color: #fff;
      animation: fadeIn 0.3s ease-out;
    }

    .notification.success { background-color: #4caf50; }
    .notification.error   { background-color: #f44336; }
    .notification.info    { background-color: #2196f3; }
    .notification.warning { background-color: #ff9800; }

    .progress {
      height: 4px;
      margin-top: 8px;
      background: rgba(255,255,255,0.3);
      border-radius: 2px;
      overflow: hidden;
    }
    .message {
      font-weight: 700;
      font-size: 1.2em;
    }

    .progress-bar {
      height: 100%;
      background: white;
      transition: width 0.2s linear;
    }

    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(-10px); }
      to { opacity: 1; transform: translateY(0); }
    }
  `]
})
export class NotificationComponent implements OnInit {
  notifications$!: Observable<any[]>;

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.notifications$ = this.notificationService.notifications$;
  }
}
