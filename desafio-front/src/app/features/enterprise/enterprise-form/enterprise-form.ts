import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EnterpriseModel } from '../../../core/model/enterprise.model';
import { CommonModule } from '@angular/common';
import { CepService } from '../../../core/services/cep.service';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-enterprise-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './enterprise-form.html',
  styleUrl: './enterprise-form.scss'
})
export class EnterpriseFormComponent implements OnInit {
  @Input() enterprise: EnterpriseModel = {
    enterpriseId: 0,
    tradeName: '',
    cnpj: '',
    cep: '',
    state: '',
    suppliers: []
  };

  @Input() title: string = 'Empresa';
  @Input() submitButtonText: string = 'Salvar';

  @Output() submitForm = new EventEmitter<EnterpriseModel>();
  @Output() cancel = new EventEmitter<void>();

  formEnterprise: EnterpriseModel = { ...this.enterprise };
  isLoadingCep: boolean = false;

  constructor(
    private cepService: CepService,
    private notificationService: NotificationService
  ) {}

  ngOnInit() {
    this.formEnterprise = { ...this.enterprise };
  }

  ngOnChanges() {
    this.formEnterprise = { ...this.enterprise };
  }

  consultarCep() {
    const cep = this.formEnterprise.cep?.replace(/\D/g, '');

    if (!cep || cep.length !== 8) {
      this.notificationService.error('CEP Inválido', 'Digite um CEP válido com 8 dígitos');
      return;
    }

    this.isLoadingCep = true;

    this.cepService.getAddressByCep(cep).subscribe({
      next: (endereco) => {
        if(endereco.uf && endereco.localidade){
        this.formEnterprise.state = endereco.uf;
        console.log('Endereço encontrado:', endereco);
        this.isLoadingCep = false;
        this.notificationService.success('CEP Encontrado', `Estado: ${endereco.uf} - ${endereco.localidade}`);
      }else{this.isLoadingCep = false; this.notificationService.error('CEP Não encontrado', "Erro"); throw new Error('Endereço não encontrado');}
      },
      error: (error) => {
        this.isLoadingCep = false;
        this.notificationService.error('Erro na Consulta', 'Não foi possível consultar o CEP. Verifique o número e tente novamente.');
        console.error('Erro ao consultar CEP:', error);
      }
    });
  }

  onSubmit() {
    const cep = this.formEnterprise.cep?.replace(/\D/g, '');
    // Valida se o estado foi preenchido (via CEP)
    if (!this.formEnterprise.state) {
      this.notificationService.error('Campo Obrigatório', 'É necessário consultar o CEP para obter o estado');
      return;
    }
    this.cepService.getAddressByCep(cep).subscribe({
      next: (endereco) => {
        if(endereco.uf && endereco.localidade){
        this.formEnterprise.state = endereco.uf;
        this.isLoadingCep = false;
      this.submitForm.emit(this.formEnterprise);
      }else{this.isLoadingCep = false; this.notificationService.error('CEP Não encontrado', "Erro"); throw new Error('Endereço não encontrado');}
      },
      error: (error) => {
        this.isLoadingCep = false;
        this.notificationService.error('Erro na Consulta', 'Não foi possível consultar o CEP. Verifique o número e tente novamente.');
        console.error('Erro ao consultar CEP:', error);
      }
    })


  }

  onCancel() {
    this.cancel.emit();
  }

  // Máscara para o CEP
  onCepInput(event: any) {
    let value = event.target.value.replace(/\D/g, '');

    if (value.length > 5) {
      value = value.replace(/^(\d{5})(\d)/, '$1-$2');
    }

    if (value.length > 9) {
      value = value.substring(0, 9);
    }

    this.formEnterprise.cep = value;
  }
}
