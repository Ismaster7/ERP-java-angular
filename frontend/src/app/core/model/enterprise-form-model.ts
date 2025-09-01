export interface EnterpriseModel {
  enterpriseId: number;
  tradeName: string;
  cnpj: string;
  cep: string;
  state: string;
  suppliers?: any[]; // Pode conter objetos completos OU apenas IDs
}
