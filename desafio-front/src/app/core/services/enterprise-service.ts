import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnterpriseModel } from "../model/enterprise.model";
import { EnterpriseCreateModel } from "../model/enterprise-create.model";

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

  updateEnterprise(enterprise: EnterpriseModel): Observable<EnterpriseModel> {
    return this.http.put<EnterpriseModel>(`${this.apiUrl}`, enterprise);
  }

  deleteEnterprise(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
