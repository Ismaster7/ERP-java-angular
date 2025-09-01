export interface CepModel {
  cep: string;
  logradouro: string;
  complemento: string;
  bairro: string;
  localidade: string;
  estado: string; // Usando estado em vez de uf
  uf: string;
  ibge: string;
  gia: string;
  ddd: string;
  siafi: string;
}
