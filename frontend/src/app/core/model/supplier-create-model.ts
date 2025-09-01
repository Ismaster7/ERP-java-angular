export interface SupplierCreateModel {
  document: string;
  name: string;
  email?: string;
  cep: string;
  rg?: string;
  birthdayDate?: string;
  enterprises: number[];
}
