export interface SupplierModel {
  supplierId: number;
  document: string;
  name: string;
  email?: string;
  cep: string;
  type: number;
  rg?: string;
  birthdayDate?: string;
  enterprises?: number[];
}
