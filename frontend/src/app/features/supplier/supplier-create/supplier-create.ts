import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SupplierModel } from '../../../core/model/supplier-model-box';
import { SupplierCreateModel } from '../../../core/model/supplier-create-model';
import { SupplierServiceTs } from '../../../core/services/supplier-service.ts';
import { SupplierFormComponent } from '../../../features/supplier/supplier-form/supplier-form';
import { NotificationService } from '../../../core/services/notification.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-supplier-create',
  standalone: true,
  imports: [SupplierFormComponent],
  templateUrl: './supplier-create.html',
  styleUrl: './supplier-create.scss'
})
export class SupplierCreate {
  supplier: SupplierModel = {
    supplierId: 0,
    document: '',
    name: '',
    email: '',
    cep: '',
    type: 0,
    rg: '',
    birthdayDate: '',
    enterprises: []
  };

  constructor(
    private router: Router,
    private supplierService: SupplierServiceTs,
    private notificationService: NotificationService
  ) {}

  onSubmit(supplierData: any) {
    const cleanDocument = supplierData.document.replace(/\D/g, '');
    const isPhysicalPerson = cleanDocument.length === 11;

    const supplierForCreate: SupplierCreateModel = {
      document: supplierData.document,
      name: supplierData.name,
      email: supplierData.email || undefined,
      cep: supplierData.cep,
      rg: isPhysicalPerson ? supplierData.rg : undefined,
      birthDate: isPhysicalPerson ? supplierData.birthdayDate : undefined,
      enterprises: supplierData.enterprises || []
    };

    console.log('Enviando para API:', supplierForCreate);

    this.supplierService.createSupplier(supplierForCreate).subscribe({
      next: () => {
        this.notificationService.success('Sucesso!', 'Fornecedor criado com sucesso');
        this.router.navigate(['/supplier']);
      },
      error: (error: HttpErrorResponse) => {
  console.error('Erro ao criar fornecedor:', error);

  if (error.status === 400) {
    const errorPayload = error.error;

    if (Array.isArray(errorPayload)) {
      // É um array de mensagens
      errorPayload.forEach((msg: string) => {
        this.notificationService.error("Erro", msg);
      });
    } else if (typeof errorPayload === 'string') {
      // É uma única mensagem em string
      this.notificationService.error("Erro", errorPayload);
    } else {
      // Fallback genérico
      this.notificationService.error("Erro", error.error.exeption);
    }
  }
}
    });
  }

  onCancel() {
    this.router.navigate(['/supplier']);
  }
}
