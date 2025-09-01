export interface EnterpriseModel {
  enterpriseId: number;
  tradeName: string;
  cnpj: string;
  cep: string;
  state: string;
  suppliers?: number[];
}
