import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { SupplierModel } from '../model/supplier-model-box';
import { EnterpriseCreateModel } from '../model/enterprise-create.model';
import { EnterpriseModel } from '../model/enterprise.model';
import { SupplierCreateModel } from '../model/supplier-create-model';
import { HttpParams } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class SupplierServiceTs {
  constructor(private http: HttpClient) {}
  private apiUrl = 'http://localhost:8086/api/supplier/v1';

  getSuppliers(): Observable<SupplierModel[]> {
      return this.http.get<SupplierModel[]>(`${this.apiUrl}`);
    }

    getSupplierById(id: number): Observable<SupplierModel> {
      return this.http.get<SupplierModel>(`${this.apiUrl}/${id}`);
    }

    createSupplier(supplier: SupplierCreateModel): Observable<SupplierModel> {
    return this.http.post<SupplierModel>(`${this.apiUrl}`, supplier);
    }

    updateSupplier(supplier: SupplierModel): Observable<SupplierModel> {
      return this.http.put<SupplierModel>(`${this.apiUrl}`, supplier);
    }

    deleteSupplier(id: number): Observable<void> {
      return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

      searchSuppliers(name?: string, document?: string): Observable<SupplierModel[]> {
    let params = new HttpParams();
    if (name) params = params.set('name', name);
    if (document) params = params.set('document', document);

    return this.http.get<SupplierModel[]>(`${this.apiUrl}/search`, { params });
  }
}
