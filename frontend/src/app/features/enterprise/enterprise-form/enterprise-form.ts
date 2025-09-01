import { Component, EventEmitter, Input, Output, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EnterpriseModel } from '../../../core/model/enterprise.model';
import { SupplierModel } from '../../../core/model/supplier-model-box';
import { CepService } from '../../../core/services/cep.service';
import { CepModel } from '../../../core/model/CepModel';
import { NotificationService } from '../../../core/services/notification.service';
import { SupplierServiceTs } from '../../../core/services/supplier-service.ts';

@Component({
  selector: 'app-enterprise-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './enterprise-form.html',
  styleUrls: ['./enterprise-form.scss']
})
export class EnterpriseFormComponent implements OnInit, OnChanges {
  @Input() enterprise: EnterpriseModel = {
    enterpriseId: 0,
    tradeName: '',
    cnpj: '',
    cep: '',
    state: '',
    suppliers: [] // AGORA ESTE ARRAY CONTÉM OBJETOS COMPLETOS
  };

  @Input() title: string = 'Empresa';
  @Input() submitButtonText: string = 'Salvar';

  @Output() submitForm = new EventEmitter<EnterpriseModel>();
  @Output() cancel = new EventEmitter<void>();

  formEnterprise: EnterpriseModel = { ...this.enterprise };
  supplierObjects: SupplierModel[] = [];
  isLoadingCep: boolean = false;

  suppliers: SupplierModel[] = [];
  selectedSupplierId: number | null = null;

  constructor(
    private cepService: CepService,
    private notificationService: NotificationService,
    private supplierService: SupplierServiceTs
  ) {}

  ngOnInit() {
    this.formEnterprise = { ...this.enterprise };
    this.loadSuppliers();

    // CORREÇÃO: Usar os suppliers que já vieram do backend
    this.updateSupplierObjectsFromInput();
  }

  ngOnChanges(changes: SimpleChanges) {
    console.log('EnterpriseFormComponent - ngOnChanges acionado:', changes);

    if (changes['enterprise'] && changes['enterprise'].currentValue) {
      const currentEnterprise = changes['enterprise'].currentValue as EnterpriseModel;

      console.log('Enterprise atual:', currentEnterprise);
      console.log('Suppliers atual:', currentEnterprise.suppliers);

      this.formEnterprise = { ...currentEnterprise };
      this.updateSupplierObjectsFromInput();
    }
  }

  private updateSupplierObjectsFromInput() {
    console.log('Suppliers recebidos do backend:', this.enterprise.suppliers);

    if (this.enterprise.suppliers && this.enterprise.suppliers.length > 0) {
      // CORREÇÃO: Usar diretamente os objetos que vieram do backend
      this.supplierObjects = this.enterprise.suppliers as unknown as SupplierModel[];
      console.log('SupplierObjects atualizados:', this.supplierObjects);
    } else {
      this.supplierObjects = [];
      console.log('Nenhum supplier recebido do backend');
    }
  }

  // Método para carregar todos os fornecedores para o dropdown
  loadSuppliers() {
    this.supplierService.getSuppliers().subscribe({
      next: (data) => {
        this.suppliers = data;
        console.log('Todos os fornecedores carregados para dropdown:', this.suppliers);
      },
      error: (err) => {
        console.error('Erro ao carregar fornecedores', err);
        this.notificationService.error('Erro', 'Não foi possível carregar a lista de fornecedores');
      }
    });
  }

  consultarCep() {
    const cep = this.formEnterprise.cep?.replace(/\D/g, '');
    if (!cep || cep.length !== 8) {
      this.notificationService.error('CEP Inválido', 'Digite um CEP válido com 8 dígitos');
      return;
    }

    this.isLoadingCep = true;
    this.cepService.getAddressByCep(cep).subscribe({
      next: (endereco: CepModel) => {
        if (endereco.estado) {
          this.formEnterprise.state = endereco.estado;
          this.isLoadingCep = false;
          this.notificationService.success(
            'CEP Encontrado',
            `Estado: ${endereco.estado} - ${endereco.localidade}`
          );
        } else {
          this.isLoadingCep = false;
          this.notificationService.error('CEP Não encontrado', 'CEP não encontrado na base de dados');
        }
      },
      error: (error) => {
        this.isLoadingCep = false;
        this.notificationService.error(
          'Erro na Consulta',
          'Não foi possível consultar o CEP. Verifique o número e tente novamente.'
        );
        console.error('Erro ao consultar CEP:', error);
      }
    });
  }

  onSubmit() {
    if (!this.formEnterprise.state) {
      this.notificationService.error(
        'Campo Obrigatório',
        'É necessário consultar o CEP para obter o estado'
      );
      return;
    }

    // Validação: Paraná não pode ter fornecedores menores de idade
    if (this.isParanaState()) {
      const invalidSuppliers = this.supplierObjects.filter(supplier =>
        this.isSupplierUnderagePhysicalPerson(supplier)
      );

      if (invalidSuppliers.length > 0) {
        const names = invalidSuppliers.map(s => s.name).join(', ');
        this.notificationService.error(
          'Restrição do Paraná',
          `Não é permitido fornecedores Pessoa Física menores de idade no Paraná: ${names}`
        );
        return;
      }
    }

    // CORREÇÃO: Preparar para enviar apenas IDs para o backend
    const enterpriseToSend: EnterpriseModel = {
      enterpriseId: this.formEnterprise.enterpriseId,
      tradeName: this.formEnterprise.tradeName,
      cnpj: this.formEnterprise.cnpj,
      cep: this.formEnterprise.cep,
      state: this.formEnterprise.state,
      suppliers: this.supplierObjects.map(s => s.supplierId) // Envia apenas IDs
    };

    console.log('Enviando para o backend:', enterpriseToSend);
    this.submitForm.emit(enterpriseToSend);
  }

  onCancel() {
    this.cancel.emit();
  }

  onCepInput(event: any) {
    let value = event.target.value.replace(/\D/g, '');
    if (value.length > 5) value = value.replace(/^(\d{5})(\d)/, '$1-$2');
    if (value.length > 9) value = value.substring(0, 9);
    this.formEnterprise.cep = value;
  }

  addSupplier() {
    if (!this.selectedSupplierId) {
      this.notificationService.warning('Atenção', 'Selecione um fornecedor');
      return;
    }

    const supplier = this.suppliers.find(s => s.supplierId === this.selectedSupplierId);
    if (!supplier) {
      this.notificationService.error('Erro', 'Fornecedor não encontrado');
      return;
    }

    if (this.supplierObjects.some(s => s.supplierId === supplier.supplierId)) {
      this.notificationService.warning('Atenção', 'Fornecedor já adicionado');
      return;
    }

    // Adiciona à lista de exibição
    this.supplierObjects.push(supplier);
    this.selectedSupplierId = null;

    // Notificação para menores de idade
    if (this.isSupplierUnderagePhysicalPerson(supplier)) {
      this.notificationService.warning(
        'Fornecedor Menor de Idade',
        `${supplier.name} é Pessoa Física menor de idade. Não será permitido se a empresa for do Paraná.`
      );
    }
  }

  removeSupplier(supplierId: number) {
    this.supplierObjects = this.supplierObjects.filter(s => s.supplierId !== supplierId);
  }

  getAge(birthDate: string): number {
    if (!birthDate) return 0;

    const birth = new Date(birthDate);
    const today = new Date();
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
      age--;
    }

    return age;
  }

  // CORREÇÃO: Nova função para validar Pessoa Física menor de idade
isSupplierUnderagePhysicalPerson(supplier: SupplierModel): boolean {
  const type = supplier.type ?? 0;
  const birthday = supplier.birthdayDate;

  // Verifica se o tipo é Pessoa Física e se a data de nascimento é válida
  if (type === 0 && typeof birthday === 'string') {
    return this.getAge(birthday) < 18; // Verifica se a idade é menor que 18
  }

  // Retorna falso caso não seja Pessoa Física ou a data de nascimento não seja uma string válida
  return false;
}

  // Função mantida para compatibilidade
  isSupplierMinor(supplier: SupplierModel): boolean {
    return this.isSupplierUnderagePhysicalPerson(supplier);
  }

  // CORREÇÃO: Texto simplificado para "Física" e "Jurídica"
  getSupplierTypeText(type: number): string {
    return type === 0 ? 'Física' : 'Jurídica';
  }

  isParanaState(): boolean {
    return this.isNotNullOrUndefined(this.formEnterprise.state) &&
           (this.formEnterprise.state.toLowerCase().includes('paraná') ||
            this.formEnterprise.state.toLowerCase().includes('parana'));
  }

  isNotNullOrUndefined(value: any): boolean {
    return value !== null && value !== undefined;
  }
}
