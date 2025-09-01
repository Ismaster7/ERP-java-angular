import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SupplierModel } from '../../../core/model/supplier-model-box';
import { SupplierServiceTs } from '../../../core/services/supplier-service.ts';
import { NotificationService } from '../../../core/services/notification.service';
import { ConfirmModalComponent } from '../../../shared/components/confirm-modal/confirm-modal';
import { ActionButton } from '../../../shared/buttons/action-button/action-button';
import { HttpErrorResponse } from '@angular/common/http';
import { trigger, state, style, transition, animate, group, query, stagger } from '@angular/animations';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-supplier',
  standalone: true,
  imports: [ActionButton, ConfirmModalComponent, FormsModule],
  animations: [
    trigger('portalAnimation', [
      transition(':enter', [
        style({
          opacity: 0,
          transform: 'scale(0.3)',
          transformOrigin: 'left center'
        }),
        animate('400ms ease-out', style({
          opacity: 1,
          transform: 'scale(1)'
        }))
      ]),
      transition(':leave', [
        animate('400ms ease-in', style({
          opacity: 0,
          transform: 'scale(0.3)',
          transformOrigin: 'left center'
        }))
      ])
    ]),
     trigger('listAnimation', [
      transition('* => *', [
        query(':enter', [
          style({ opacity: 0, transform: 'translateY(-20px)' }),
          stagger('100ms', [
            animate('200ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
          ])
        ], { optional: true })
      ])
    ]),
    trigger('filterAnimation', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-20px)' }),
        animate('400ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ opacity: 0, transform: 'translateY(-10px)' }))
      ])
    ])
  ],
  templateUrl: './supplier.html',
  styleUrls: ['../../enterprise/enterprise/enterprise.scss']
})
export class SupplierComponent implements OnInit {
  suppliers: SupplierModel[] = [];
  showFilter: boolean = false;
  showConfirmModal: boolean = false;
  supplierToDelete: number | null = null;
  deleteMessage: string = '';

  // Variáveis para filtros
  nameFilter: string = '';
  documentFilter: string = '';

  constructor(
    private supplierService: SupplierServiceTs,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  ngOnInit() {
    this.loadSuppliers();
  }

  loadSuppliers() {
    this.supplierService.getSuppliers().subscribe({
      next: (data: SupplierModel[]) => {
        this.suppliers = data;
        console.log('Suppliers carregados:', this.suppliers);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error fetching suppliers:', error);
        this.notificationService.error('Erro', 'Não foi possível carregar os fornecedores');
      }
    });
  }

  searchSuppliers() {
    if (this.nameFilter || this.documentFilter) {
      this.supplierService.searchSuppliers(this.nameFilter, this.documentFilter).subscribe({
        next: (data: SupplierModel[]) => {
          this.suppliers = data;
          if (data.length === 0) {
            this.notificationService.info('Info', 'Nenhum fornecedor encontrado com os filtros aplicados');
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error searching suppliers:', error);
          this.notificationService.error('Erro', 'Não foi possível realizar a pesquisa');
        }
      });
    } else {
      this.notificationService.warning('Atenção', 'Preencha pelo menos um campo para filtrar');
    }
  }

  clearFilters() {
    this.nameFilter = '';
    this.documentFilter = '';
    this.loadSuppliers();
  }

  editSupplier(supplierId: number) {
    this.router.navigate(['/supplier/edit', supplierId]);
  }

  createSupplier() {
    this.router.navigate(['/supplier/create']);
  }

  openDeleteConfirm(supplierId: number, supplierName: string) {
    this.supplierToDelete = supplierId;
    this.deleteMessage = `Tem certeza que deseja excluir o fornecedor "${supplierName}"?`;
    this.showConfirmModal = true;
  }

  confirmDelete() {
    if (this.supplierToDelete !== null) {
      this.supplierService.deleteSupplier(this.supplierToDelete).subscribe({
        next: () => {
          this.suppliers = this.suppliers.filter(s => s.supplierId !== this.supplierToDelete);
          this.closeModal();
          this.notificationService.success('Sucesso', 'Fornecedor excluído com sucesso');
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error deleting supplier:', error);
          this.notificationService.error('Erro', 'Não foi possível excluir o fornecedor');
          this.closeModal();
        }
      });
    }
  }

  cancelDelete() {
    this.closeModal();
  }

  closeModal() {
    this.showConfirmModal = false;
    this.supplierToDelete = null;
    this.deleteMessage = '';
  }

  toggleFilter() {
    this.showFilter = !this.showFilter;
    // Se estiver fechando o filtro, limpar os campos e recarregar a lista completa
    if (!this.showFilter) {
      this.clearFilters();
    }
  }

  getSupplierTypeText(type: number): string {
    return type === 0 ? 'Física' : 'Jurídica';
  }

  getEnterpriseCount(supplier: SupplierModel): number {
    if (!supplier.enterprises) return 0;

    // Se for array de objetos
    if (supplier.enterprises.length > 0 && typeof supplier.enterprises[0] === 'object') {
      return supplier.enterprises.length;
    }

    // Se for array de IDs
    return supplier.enterprises.length;
  }

  getEnterpriseNames(supplier: SupplierModel): string {
    if (!supplier.enterprises || supplier.enterprises.length === 0) return 'Nenhuma empresa';

    // Se for array de objetos
    if (typeof supplier.enterprises[0] === 'object') {
      const enterprises = supplier.enterprises as any[];
      if (enterprises.length <= 2) {
        return enterprises.map(e => e.tradeName).join(', ');
      } else {
        return `${enterprises[0].tradeName}, ${enterprises[1].tradeName} +${enterprises.length - 2}`;
      }
    }

    // Se for array de IDs
    return `${supplier.enterprises.length} empresa(s)`;
  }
}
