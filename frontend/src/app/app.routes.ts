import { Routes } from '@angular/router';
import { EnterpriseComponent } from './features/enterprise/enterprise/enterprise';
import {  SupplierComponent } from './features/supplier/supplier/supplier';
import { Dashboard } from './features/dashboard/dashboard/dashboard';
import { EnterpriseEditComponent } from './features/enterprise/enterprise-edit.component/enterprise-edit.component';
import { EnterpriseCreateComponent } from './features/enterprise/enterprise-create/enterprise-create';
import { SupplierEdit } from './features/supplier/supplier-edit/supplier-edit';
import { SupplierCreate } from './features/supplier/supplier-create/supplier-create';
export const routes: Routes = [
  {
    path: 'enterprise',
    component: EnterpriseComponent,
  },
  {
    path: 'enterprise/edit/:id', component: EnterpriseEditComponent
  },
  {
    path: 'enterprise/create', component: EnterpriseCreateComponent
  },
  { path: 'supplier', component: SupplierComponent },
  { path: 'supplier/edit/:id', component: SupplierEdit },
  { path: 'supplier/create', component: SupplierCreate },
  {
    path: 'dashboard',
    component: Dashboard
  }
];
