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
    suppliers: [] // Garanta que sempre seja um array, não undefined
  };

  constructor(
    private router: Router,
    private enterpriseService: EnterpriseService,
    private notificationService: NotificationService
  ) {}

  onSubmit(enterpriseData: EnterpriseModel) {
    // Corrija o type casting - converta para EnterpriseCreateModel
    const enterpriseForCreate: EnterpriseCreateModel = {
      tradeName: enterpriseData.tradeName,
      cnpj: enterpriseData.cnpj,
      cep: enterpriseData.cep,
      state: enterpriseData.state,
      suppliers: enterpriseData.suppliers || [] // Garanta que não seja undefined
    };

    this.enterpriseService.createEnterprise(enterpriseForCreate).subscribe(
      () => {
        this.router.navigate(['/enterprise']);
      },
      (error) => {
        console.error('Error creating enterprise:', error);
      },
      () => {
        this.notificationService.success( 'Empresa criada com sucesso.', "sucesso");
      }
    );
  }

  onCancel() {
    this.router.navigate(['/enterprise']);
  }
}
