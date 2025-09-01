import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Notification {
  id: number;
  type: 'success' | 'error' | 'info' | 'warning';
  title: string;
  message: string;
  progress: number;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  notifications$ = this.notificationsSubject.asObservable();

  private idCounter = 0;

  private addNotification(type: Notification['type'], title: string, message: string, duration: number = 5000) {
    const id = ++this.idCounter;
    const notification: Notification = {
      id,
      type,
      title,
      message,
      progress: 100
    };

    const current = this.notificationsSubject.getValue();
    this.notificationsSubject.next([...current, notification]);

    // Atualiza a barra de progresso
    const step = 100 / (duration / 100);
    const interval = setInterval(() => {
      const currentList = this.notificationsSubject.getValue();
      const index = currentList.findIndex(n => n.id === id);

      if (index !== -1) {
        currentList[index].progress -= step;
        if (currentList[index].progress <= 0) {
          this.removeNotification(id);
          clearInterval(interval);
        } else {
          this.notificationsSubject.next([...currentList]);
        }
      } else {
        clearInterval(interval);
      }
    }, 100);
  }

  private removeNotification(id: number) {
    const current = this.notificationsSubject.getValue().filter(n => n.id !== id);
    this.notificationsSubject.next(current);
  }

  // Métodos helpers
  success(title: string, message: string) {
    this.addNotification('success', title, message);
  }

  error(title: string, message: string) {
    this.addNotification('error', title, message);
  }

  info(title: string, message: string) {
    this.addNotification('info', title, message);
  }

  warning(title: string, message: string) {
    this.addNotification('warning', title, message);
  }
}
