export interface EnterpriseUpdateModel {
  enterpriseId: number;
  tradeName: string;
  cnpj: string;
  cep: string;
  state: string;
  suppliers?: number[];
}
