import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CepModel } from '../model/CepModel';
@Injectable({
  providedIn: 'root'
})
export class CepService {
  private cepApi: string = 'https://viacep.com.br/ws';

 constructor (private http: HttpClient){}

 getAddressByCep(cep: string): Observable<CepModel> {
   return this.http.get<CepModel>(`${this.cepApi}/${cep}/json`);
 }

}


