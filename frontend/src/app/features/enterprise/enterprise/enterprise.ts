import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActionButton } from '../../../shared/buttons/action-button/action-button';
import { trigger, state, style, transition, animate, group, query, stagger } from '@angular/animations';
import { EnterpriseModel } from '../../../core/model/enterprise.model';
import { EnterpriseService } from '../../../core/services/enterprise-service';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ConfirmModalComponent } from '../../../shared/components/confirm-modal/confirm-modal';
import { NotificationService } from '../../../core/services/notification.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-enterprise',
  standalone: true,
  imports: [ActionButton, FormsModule, HttpClientModule, ConfirmModalComponent],
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
  templateUrl: './enterprise.html',
  styleUrl: './enterprise.scss'
})
export class EnterpriseComponent implements OnInit {
  enterprises: EnterpriseModel[] = [];
  showFilter: boolean = false;
  showConfirmModal: boolean = false;
  enterpriseToDelete: number | null = null;
  deleteMessage: string = '';

  // Variáveis para filtros
  tradeNameFilter: string = '';
  cnpjFilter: string = '';

  constructor(
    private enterpriseService: EnterpriseService,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  ngOnInit() {
    this.loadEnterprises();
  }

  loadEnterprises() {
    this.enterpriseService.getEnterprises().subscribe({
      next: (data) => {
        this.enterprises = data;
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error fetching enterprises:', error);
        this.notificationService.error('Erro', 'Não foi possível carregar as empresas');
      }
    });
  }

  searchEnterprises() {
    this.enterpriseService.searchEnterprises(this.tradeNameFilter, this.cnpjFilter).subscribe({
      next: (data) => {
        this.enterprises = data;
        if (data.length === 0) {
          this.notificationService.info('Info', 'Nenhuma empresa encontrada com os filtros aplicados');
        }
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error searching enterprises:', error);
        this.notificationService.error('Erro', 'Não foi possível realizar a pesquisa');
      }
    });
  }

  editEnterprise(enterpriseId: number) {
    this.router.navigate(['/enterprise/edit', enterpriseId]);
  }

  openDeleteConfirm(enterpriseId: number, enterpriseName: string) {
    this.enterpriseToDelete = enterpriseId;
    this.deleteMessage = `Tem certeza que deseja excluir a empresa "${enterpriseName}"? Esta ação não pode ser desfeita.`;
    this.showConfirmModal = true;
  }

  confirmDelete() {
    if (this.enterpriseToDelete !== null) {
      this.enterpriseService.deleteEnterprise(this.enterpriseToDelete).subscribe({
        next: () => {
          this.enterprises = this.enterprises.filter(e => e.enterpriseId !== this.enterpriseToDelete);
          this.closeModal();
          this.notificationService.success('Sucesso', 'Empresa excluída com sucesso');
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error deleting enterprise:', error);
          this.notificationService.error('Erro', 'Não foi possível excluir a empresa');
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
    this.enterpriseToDelete = null;
    this.deleteMessage = '';
  }

  toggleFilter() {
    this.showFilter = !this.showFilter;
    // Se estiver fechando o filtro, limpar os campos e recarregar a lista completa
    if (!this.showFilter) {
      this.clearFilters();
    }
  }

  applyFilters() {
    if (this.tradeNameFilter || this.cnpjFilter) {
      this.searchEnterprises();
    } else {
      this.notificationService.warning('Atenção', 'Preencha pelo menos um campo para filtrar');
    }
  }

  clearFilters() {
    this.tradeNameFilter = '';
    this.cnpjFilter = '';
    this.loadEnterprises();
  }
}
