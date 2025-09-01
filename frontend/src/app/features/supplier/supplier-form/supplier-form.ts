import { Component, EventEmitter, Input, Output, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SupplierModel } from '../../../core/model/supplier-model-box';
import { CepService } from '../../../core/services/cep.service';
import { CepModel } from '../../../core/model/CepModel';
import { NotificationService } from '../../../core/services/notification.service';
import { EnterpriseService } from '../../../core/services/enterprise-service';
import { EnterpriseModel } from '../../../core/model/enterprise.model';

@Component({
  selector: 'app-supplier-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './supplier-form.html',
  styleUrls: ['./supplier-form.scss']
})
export class SupplierFormComponent implements OnInit, OnChanges {
  @Input() supplier: SupplierModel = {
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

  @Input() title: string = 'Fornecedor';
  @Input() submitButtonText: string = 'Salvar';

  @Output() submitForm = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  formSupplier: SupplierModel = { ...this.supplier };
  isLoadingCep: boolean = false;
  enterprises: EnterpriseModel[] = [];
  selectedEnterpriseId: number | null = null;
  enterpriseObjects: any[] = [];
  isPhysicalPerson: boolean = false;
  isSubmitting: boolean = false;

  constructor(
    private cepService: CepService,
    private notificationService: NotificationService,
    private enterpriseService: EnterpriseService
  ) {}

  ngOnInit() {
    this.formSupplier = { ...this.supplier };
    this.loadEnterprises();
    this.updateEnterpriseObjects();
    this.determineSupplierType();
  }

  ngOnChanges(changes: SimpleChanges) {
    console.log('SupplierFormComponent - ngOnChanges acionado:', changes);

    if (changes['supplier'] && changes['supplier'].currentValue) {
      console.log('Novos dados do fornecedor:', changes['supplier'].currentValue);
      this.formSupplier = { ...changes['supplier'].currentValue };
      this.updateEnterpriseObjects();
      this.determineSupplierType();
    }
  }

  determineSupplierType() {
    const cleanDocument = this.formSupplier.document.replace(/\D/g, '');
    this.isPhysicalPerson = cleanDocument.length === 11;
    this.formSupplier.type = this.isPhysicalPerson ? 0 : 1;
  }

  onDocumentChange() {
    this.determineSupplierType();
  }

  loadEnterprises() {
    this.enterpriseService.getEnterprises().subscribe({
      next: (data) => {
        this.enterprises = data;
      },
      error: (error) => {
        console.error('Error loading enterprises:', error);
        this.notificationService.error('Erro', 'Não foi possível carregar a lista de empresas');
      }
    });
  }

  private updateEnterpriseObjects() {
    if (this.formSupplier.enterprises && this.formSupplier.enterprises.length > 0) {
      if (typeof this.formSupplier.enterprises[0] === 'object') {
        this.enterpriseObjects = this.formSupplier.enterprises as any[];
      } else {
        this.enterpriseObjects = this.formSupplier.enterprises.map(id => ({
          enterpriseId: id,
          tradeName: `Empresa ${id}`
        }));
      }
    } else {
      this.enterpriseObjects = [];
    }
  }

  consultarCep() {
    const cep = this.formSupplier.cep?.replace(/\D/g, '');
    if (!cep || cep.length !== 8) {
      this.notificationService.error('CEP Inválido', 'Digite um CEP válido com 8 dígitos');
      return;
    }

    this.isLoadingCep = true;
    this.cepService.getAddressByCep(cep).subscribe({
      next: (endereco: CepModel) => {
        this.isLoadingCep = false;
        this.notificationService.success('CEP Encontrado', `CEP válido: ${endereco.localidade} - ${endereco.uf}`);
      },
      error: (error) => {
        this.isLoadingCep = false;
        this.notificationService.error('Erro na Consulta', 'CEP não encontrado');
      }
    });
  }

  onSubmit() {
    // Prevenir múltiplos envios
    if (this.isSubmitting) return;
    this.isSubmitting = true;

    // Garantir que o tipo esteja correto
    this.determineSupplierType();

    // Validar documento (CPF/CNPJ)
    if (!this.isValidDocument()) {
      this.notificationService.error('Erro', 'Documento deve ter 11 dígitos (CPF) ou 14 dígitos (CNPJ)');
      this.isSubmitting = false;
      return;
    }

    // Validar CEP
    if (!this.isValidCep()) {
      this.notificationService.error('Erro', 'CEP deve ter 8 dígitos');
      this.isSubmitting = false;
      return;
    }

    // Validar campos obrigatórios para pessoa física
    if (this.isPhysicalPerson) {
      if (!this.formSupplier.rg || this.formSupplier.rg.trim() === '') {
        this.notificationService.error('Erro', 'RG é obrigatório para Pessoa Física');
        this.isSubmitting = false;
        return;
      }

      if (!this.formSupplier.birthdayDate) {
        this.notificationService.error('Erro', 'Data de nascimento é obrigatória para Pessoa Física');
        this.isSubmitting = false;
        return;
      }

      // Validar se a data de nascimento é válida (não é futura)
      const birthDate = new Date(this.formSupplier.birthdayDate);
      const today = new Date();
      if (birthDate > today) {
        this.notificationService.error('Erro', 'Data de nascimento não pode ser uma data futura');
        this.isSubmitting = false;
        return;
      }
    }

    // Preparar para enviar apenas IDs das empresas
    const enterpriseIds = this.enterpriseObjects.map(e =>
      typeof e === 'object' ? e.enterpriseId : e
    );

    // Garantir que campos opcionais sejam undefined se vazios
    const supplierToSend = {
      document: this.formSupplier.document,
      name: this.formSupplier.name,
      email: this.formSupplier.email?.trim() || undefined,
      cep: this.formSupplier.cep,
      rg: this.isPhysicalPerson ? this.formSupplier.rg?.trim() : undefined,
      birthdayDate: this.isPhysicalPerson ? this.formSupplier.birthdayDate : undefined,
      enterprises: enterpriseIds
    };

    console.log('Enviando dados:', supplierToSend);
    this.submitForm.emit(supplierToSend);

    // Resetar o estado de submissão após um tempo (caso o emit não finalize o processo)
    setTimeout(() => {
      this.isSubmitting = false;
    }, 2000);
  }

  onCancel() {
    this.cancel.emit();
  }

  onCepInput(event: any) {
    let value = event.target.value.replace(/\D/g, '');
    if (value.length > 5) value = value.replace(/^(\d{5})(\d)/, '$1-$2');
    if (value.length > 9) value = value.substring(0, 9);
    this.formSupplier.cep = value;
  }

  isValidCep(): boolean {
    if (!this.formSupplier.cep) return false;
    const cleanCep = this.formSupplier.cep.replace(/\D/g, '');
    return cleanCep.length === 8;
  }

  isValidDocument(): boolean {
    if (!this.formSupplier.document) return false;
    const cleanDocument = this.formSupplier.document.replace(/\D/g, '');
    return cleanDocument.length === 11 || cleanDocument.length === 14;
  }

  addEnterprise() {
    if (!this.selectedEnterpriseId) {
      this.notificationService.warning('Atenção', 'Selecione uma empresa para adicionar');
      return;
    }

    const enterprise = this.enterprises.find(e => e.enterpriseId === this.selectedEnterpriseId);
    if (!enterprise) {
      this.notificationService.error('Erro', 'Empresa não encontrada');
      return;
    }

    // Verificar se a empresa já foi adicionada
    const enterpriseAlreadyAdded = this.enterpriseObjects.some(e =>
      (typeof e === 'object' ? e.enterpriseId : e) === enterprise.enterpriseId
    );

    if (enterpriseAlreadyAdded) {
      this.notificationService.warning('Atenção', 'Esta empresa já foi adicionada');
      this.selectedEnterpriseId = null;
      return;
    }

    // Adicionar a empresa
    this.enterpriseObjects.push(enterprise);
    this.selectedEnterpriseId = null;

    this.notificationService.success('Sucesso', 'Empresa adicionada com sucesso');
  }

  removeEnterprise(enterpriseId: number) {
    this.enterpriseObjects = this.enterpriseObjects.filter(e =>
      (typeof e === 'object' ? e.enterpriseId : e) !== enterpriseId
    );
    this.notificationService.info('Info', 'Empresa removida');
  }

  isEnterpriseAlreadyAdded(enterpriseId: number): boolean {
    return this.enterpriseObjects.some(e =>
      (typeof e === 'object' ? e.enterpriseId : e) === enterpriseId
    );
  }

  isEnterpriseAlreadySelected(): boolean {
    if (!this.selectedEnterpriseId) return false;
    return this.isEnterpriseAlreadyAdded(this.selectedEnterpriseId);
  }

  getSupplierTypeText(): string {
    return this.isPhysicalPerson ? 'Pessoa Física' : 'Pessoa Jurídica';
  }

  getEnterpriseName(enterprise: any): string {
    return typeof enterprise === 'object' ? enterprise.tradeName : `Empresa ${enterprise}`;
  }

  maxBirthDate(): string {
    const today = new Date();
    return today.toISOString().split('T')[0];
  }

  isFutureDate(dateString: string): boolean {
    const date = new Date(dateString);
    const today = new Date();
    return date > today;
  }
}
