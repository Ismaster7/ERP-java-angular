import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EnterpriseModel } from '../../../core/model/enterprise.model';
import { EnterpriseService } from '../../../core/services/enterprise-service';
import { EnterpriseFormComponent } from '../../../features/enterprise/enterprise-form/enterprise-form';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-enterprise-edit',
  standalone: true,
  imports: [EnterpriseFormComponent],
  templateUrl: './enterprise-edit.component.html',
  styleUrl: './enterprise-edit.component.scss'
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
    this.enterpriseService.getEnterpriseById(id).subscribe(
      (data) => {
        const dataString = JSON.stringify(data);
        this.enterprise = JSON.parse(dataString);
      },
      (error) => {
        console.error('Error loading enterprise:', error);
      }
    );
  }

  onSubmit(enterpriseData: EnterpriseModel) {
    const enterpriseToSend = {
      enterpriseId: enterpriseData.enterpriseId,
      tradeName: enterpriseData.tradeName,
      cnpj: enterpriseData.cnpj,
      state: enterpriseData.state,
      cep: enterpriseData.cep,
      suppliers: enterpriseData.suppliers ?
                 enterpriseData.suppliers.map(s => s.supplierId) :
                 []
    };

    this.enterpriseService.updateEnterprise(enterpriseToSend).subscribe(
      () => {
        this.router.navigate(['/enterprise']);
      },
      (error) => {
        console.error('Error updating enterprise:', error);
      },() => {
        this.notificationService.success( 'Empresa atualizada com sucesso.', "sucesso");
      }
    );
  }

  onCancel() {
    this.router.navigate(['/enterprise']);
  }
}
