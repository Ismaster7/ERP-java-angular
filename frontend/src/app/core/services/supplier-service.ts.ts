import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { SupplierModel } from '../model/supplier-model-box';
import { EnterpriseCreateModel } from '../model/enterprise-create.model';
import { EnterpriseModel } from '../model/enterprise.model';
import { SupplierCreateModel } from '../model/supplier-create-model';

@Injectable({
  providedIn: 'root'
})
export class SupplierServiceTs {
  constructor(private http: HttpClient) {}
  private apiUrl = 'http://localhost:8086/api/supplier/v1';

  getEnterprises(): Observable<SupplierModel[]> {
      return this.http.get<SupplierModel[]>(`${this.apiUrl}`);
    }

    getEnterpriseById(id: number): Observable<SupplierModel> {
      return this.http.get<SupplierModel>(`${this.apiUrl}/${id}`);
    }

    createEnterprise(enterprise: SupplierCreateModel): Observable<SupplierModel> {
    return this.http.post<SupplierModel>(`${this.apiUrl}`, enterprise);
    }

    updateEnterprise(enterprise: SupplierModel): Observable<SupplierModel> {
      return this.http.put<SupplierModel>(`${this.apiUrl}`, enterprise);
    }

    deleteEnterprise(id: number): Observable<void> {
      return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
