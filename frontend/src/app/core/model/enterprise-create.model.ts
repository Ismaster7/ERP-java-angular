export interface EnterpriseCreateModel {
  tradeName: string;
  cnpj: string;
  cep: string;
  state: string;
  suppliers?: number[];
}
