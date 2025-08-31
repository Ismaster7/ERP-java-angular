import { Routes } from '@angular/router';
import { EnterpriseComponent } from './features/enterprise/enterprise/enterprise';
import { Supplier } from './features/supplier/supplier/supplier';
import { Dashboard } from './features/dashboard/dashboard/dashboard';

export const routes: Routes = [
  {
    path: 'enterprise',
    component: EnterpriseComponent,
  },
  {
    path: 'supplier',
    component: Supplier
  },
  {
    path: 'dashboard',
    component: Dashboard
  }
];
