export interface SupplierCreateModel {
  document: string;
  name: string;
  email?: string;
  cep: string;
  rg?: string;
  birthDate?: string;
  enterprises: any[];
}
