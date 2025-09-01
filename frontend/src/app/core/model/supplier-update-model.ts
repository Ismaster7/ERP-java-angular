export interface SupplierUpdateModel {
  supplierId: number;
  document: string;
  name: string;
  email?: string;
  cep: string;
    type: number;
  rg?: string;
  birthDate?: string;
  enterprises: number[];
}
