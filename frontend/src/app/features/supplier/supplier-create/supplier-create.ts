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
    // Determinar se é pessoa física com base no documento
    const cleanDocument = supplierData.document.replace(/\D/g, '');
    const isPhysicalPerson = cleanDocument.length === 11;

    // Criar objeto para envio com campos condicionais
    const supplierForCreate: SupplierCreateModel = {
      document: supplierData.document,
      name: supplierData.name,
      email: supplierData.email || undefined,
      cep: supplierData.cep,
      // Incluir RG e data de nascimento apenas para pessoa física
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
        console.error('Error creating supplier:', error);

        if (error.status === 400) {
          const errorMessage = error.error?.message || error.error?.exeption || '';

          if (errorMessage.includes('Documento')) {
            this.notificationService.error('Erro', 'Documento (CPF/CNPJ) já cadastrado');
          } else if (errorMessage.includes('RG')) {
            this.notificationService.error('Erro', 'RG é obrigatório para Pessoa Física');
          } else if (errorMessage.includes('birthdayDate') || errorMessage.includes('data de nascimento')) {
            this.notificationService.error('Erro', 'Data de nascimento é obrigatória para Pessoa Física');
          } else {
            this.notificationService.error('Erro', 'Dados inválidos. Verifique os campos obrigatórios.');
          }
        } else {
          this.notificationService.error('Erro', 'Não foi possível criar o fornecedor');
        }
      }
    });
  }

  onCancel() {
    this.router.navigate(['/supplier']);
  }
}
