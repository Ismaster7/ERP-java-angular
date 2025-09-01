import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EnterpriseModel } from '../../../core/model/enterprise.model';
import { EnterpriseUpdateModel } from '../../../core/model/enterprise-update-model';
import { EnterpriseService } from '../../../core/services/enterprise-service';
import { EnterpriseFormComponent } from '../../../features/enterprise/enterprise-form/enterprise-form';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-enterprise-edit',
  standalone: true,
  imports: [EnterpriseFormComponent],
  templateUrl: './enterprise-edit.component.html',
  styleUrls: ['./enterprise-edit.component.scss']
})
export class EnterpriseEditComponent implements OnInit {
  enterprise: EnterpriseModel = {
    enterpriseId: 0,
    tradeName: '',
    cnpj: '',
    cep: '',
    state: '',
    suppliers: []
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private enterpriseService: EnterpriseService,
    private notificationService: NotificationService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadEnterprise(parseInt(id));
    }
  }

  loadEnterprise(id: number) {
    this.enterpriseService.getEnterpriseById(id).subscribe({
      next: (data) => {
        console.log('Dados completos da empresa recebidos:', data);

        // CORREÇÃO CRÍTICA: Criar um NOVO objeto para forçar a detecção de mudanças
        this.enterprise = {
          enterpriseId: data.enterpriseId,
          tradeName: data.tradeName,
          cnpj: data.cnpj,
          cep: data.cep,
          state: data.state,
          suppliers: Array.isArray(data.suppliers) ? [...data.suppliers] : [] // Clone do array
        };

        console.log('Enterprise após carga:', this.enterprise);
        console.log('Fornecedores da empresa:', this.enterprise.suppliers);
      },
      error: (error) => {
        console.error('Error loading enterprise:', error);
        this.notificationService.error('Erro', 'Não foi possível carregar os dados da empresa');
      }
    });
  }

  onSubmit(enterpriseData: EnterpriseModel) {
    const enterpriseToUpdate: EnterpriseUpdateModel = {
      enterpriseId: enterpriseData.enterpriseId,
      tradeName: enterpriseData.tradeName,
      cnpj: enterpriseData.cnpj,
      cep: enterpriseData.cep,
      state: enterpriseData.state,
      suppliers: enterpriseData.suppliers || []
    };

    console.log('Enviando para atualização:', enterpriseToUpdate);

    this.enterpriseService.updateEnterprise(enterpriseToUpdate).subscribe({
      next: () => {
        this.notificationService.success('Sucesso!', 'Empresa atualizada com sucesso');
        this.router.navigate(['/enterprise']);
      },
      error: (error) => {
        console.error('Error updating enterprise:', error);
        if (error.status === 400 && error.error?.exeption?.includes('CNPJ')) {
          this.notificationService.error('Erro', 'CNPJ já cadastrado em outra empresa');
        } else {
          this.notificationService.error('Erro', 'Não foi possível atualizar a empresa');
        }
      }
    });
  }

  onCancel() {
    this.router.navigate(['/enterprise']);
  }
}
