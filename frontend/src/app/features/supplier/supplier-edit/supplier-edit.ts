import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SupplierModel } from '../../../core/model/supplier-model-box';
import { SupplierServiceTs } from '../../../core/services/supplier-service.ts';
import { SupplierFormComponent } from '../supplier-form/supplier-form';
import { NotificationService } from '../../../core/services/notification.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-supplier-edit',
  standalone: true,
  imports: [SupplierFormComponent],
  templateUrl: './supplier-edit.html',
  styleUrls: ['./supplier-edit.scss']
})
export class SupplierEdit implements OnInit {
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
    private route: ActivatedRoute,
    private router: Router,
    private supplierService: SupplierServiceTs,
    private notificationService: NotificationService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadSupplier(Number(id));
    } else {
      this.notificationService.error('Erro', 'ID do fornecedor não encontrado');
      this.router.navigate(['/supplier']);
    }
  }

  loadSupplier(id: number) {
    console.log('Carregando fornecedor com ID:', id);
    this.supplierService.getSupplierById(id).subscribe({
      next: (data: SupplierModel) => {
        console.log('Dados recebidos da API:', data);
        this.supplier = {
          ...data,
          enterprises: data.enterprises || []
        };
        console.log('Fornecedor atribuído:', this.supplier);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error fetching supplier:', error);
        this.notificationService.error('Erro', 'Fornecedor não encontrado');
        this.router.navigate(['/supplier']);
      }
    });
  }

  onSubmit(supplierData: any) {
    // Determinar se é pessoa física com base no documento
    const cleanDocument = supplierData.document.replace(/\D/g, '');
    const isPhysicalPerson = cleanDocument.length === 11;
    const supplierType = isPhysicalPerson ? 0 : 1;

    // Criar objeto para envio com todas as propriedades necessárias
    const supplierForUpdate: SupplierModel = {
      supplierId: this.supplier.supplierId,
      document: supplierData.document,
      name: supplierData.name,
      email: supplierData.email || undefined,
      cep: supplierData.cep,
      type: supplierType, // Incluindo a propriedade type
      rg: isPhysicalPerson ? supplierData.rg : undefined,
      birthdayDate: isPhysicalPerson ? supplierData.birthdayDate : undefined,
      enterprises: supplierData.enterprises || []
    };

    console.log('Enviando para atualização:', supplierForUpdate);

    this.supplierService.updateSupplier(supplierForUpdate).subscribe({
      next: () => {
        this.notificationService.success('Sucesso!', 'Fornecedor atualizado com sucesso');
        this.router.navigate(['/supplier']);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error updating supplier:', error);

        if (error.status === 400) {
          const errorMessage = error.error?.message || error.error?.exeption || '';

          if (errorMessage.includes('Documento')) {
            this.notificationService.error('Erro', 'Documento (CPF/CNPJ) já cadastrado');
          } else if (errorMessage.includes('RG')) {
            this.notificationService.error('Erro', 'RG é obrigatório para Pessoa Física');
          } else if (errorMessage.includes('birthdayDate') || errorMessage.includes('birthDate')) {
            this.notificationService.error('Erro', 'Data de nascimento é obrigatória para Pessoa Física');
          } else {
            this.notificationService.error('Erro', 'Dados inválidos. Verifique os campos obrigatórios.');
          }
        } else {
          this.notificationService.error('Erro', 'Não foi possível atualizar o fornecedor');
        }
      }
    });
  }

  onCancel() {
    this.router.navigate(['/supplier']);
  }
}
