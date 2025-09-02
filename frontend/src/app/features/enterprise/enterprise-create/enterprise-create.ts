import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { EnterpriseModel } from '../../../core/model/enterprise.model';
import { EnterpriseService } from '../../../core/services/enterprise-service';
import { EnterpriseFormComponent } from '../../../features/enterprise/enterprise-form/enterprise-form';
import { EnterpriseCreateModel } from '../../../core/model/enterprise-create.model';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-enterprise-create',
  standalone: true,
  imports: [EnterpriseFormComponent],
  templateUrl: './enterprise-create.html',
  styleUrl: './enterprise-create.scss'
})
export class EnterpriseCreateComponent {
  enterprise: EnterpriseModel = {
    enterpriseId: 0,
    tradeName: '',
    cnpj: '',
    cep: '',
    state: '',
    suppliers: []
  };

  constructor(
    private router: Router,
    private enterpriseService: EnterpriseService,
    private notificationService: NotificationService
  ) {}

  onSubmit(enterpriseData: EnterpriseModel) {
    const enterpriseForCreate: EnterpriseCreateModel = {
      tradeName: enterpriseData.tradeName,
      cnpj: enterpriseData.cnpj,
      cep: enterpriseData.cep,
      state: enterpriseData.state,
      suppliers: enterpriseData.suppliers || []
    };

    this.enterpriseService.createEnterprise(enterpriseForCreate).subscribe({
      next: () => {
        this.notificationService.success('Sucesso!', 'Empresa criada com sucesso');
        this.router.navigate(['/enterprise']);
      },
      error: (error) => {
        console.error('Error creating enterprise:', error);
        if (error.status === 400 && error.error?.exeption?.includes('CNPJ')) {
          this.notificationService.error(error, error.error.message);
        } else {
          this.notificationService.error('Erro', 'Não foi possível criar a empresa');
        }
      }
    });
  }

  onCancel() {
    this.router.navigate(['/enterprise']);
  }
}
