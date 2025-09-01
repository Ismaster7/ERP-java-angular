import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnterpriseModel } from "../model/enterprise.model";
import { EnterpriseCreateModel } from "../model/enterprise-create.model";
import { EnterpriseUpdateModel } from "../model/enterprise-update-model";
import { HttpParams } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class EnterpriseService {
  private apiUrl = "http://localhost:8086/api/enterprise/v1";

  constructor(private http: HttpClient) {}

  getEnterprises(): Observable<EnterpriseModel[]> {
    return this.http.get<EnterpriseModel[]>(`${this.apiUrl}`);
  }

  getEnterpriseById(id: number): Observable<EnterpriseModel> {
    return this.http.get<EnterpriseModel>(`${this.apiUrl}/${id}`);
  }

  createEnterprise(enterprise: EnterpriseCreateModel): Observable<EnterpriseModel> {
    return this.http.post<EnterpriseModel>(`${this.apiUrl}`, enterprise);
  }

  updateEnterprise(enterprise: EnterpriseUpdateModel): Observable<EnterpriseModel> {
    return this.http.put<EnterpriseModel>(
      `${this.apiUrl}/${enterprise.enterpriseId}`,
      enterprise
    );
  }

  deleteEnterprise(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
   searchEnterprises(tradeName?: string, cnpj?: string): Observable<EnterpriseModel[]> {
    let params = new HttpParams();

    if (tradeName) {
      params = params.set('tradeName', tradeName);
    }

    if (cnpj) {
      params = params.set('cnpj', cnpj);
    }

    return this.http.get<EnterpriseModel[]>(`${this.apiUrl}/search`, { params });
  }
}
