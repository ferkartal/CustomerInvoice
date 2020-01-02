import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Customer } from './customer.model';
import { map } from 'rxjs/operators';
import { Observable, BehaviorSubject } from 'rxjs';
import { Invoice } from './invoice.model';
import { environment } from './../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private CUSTOMER_URL = "/customers";
  private INVOICE_URL = "/invoices";
  private AUTH_URL = "/auth";
  private loggedIn: BehaviorSubject<boolean>;

  constructor(private httpClient: HttpClient) {
    if (sessionStorage.getItem('token')) {
      this.loggedIn = new BehaviorSubject<boolean>(true);
    } else {
      this.loggedIn = new BehaviorSubject<boolean>(false);
    }
  }

  createCustomer(customer: Customer): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };

    return this.httpClient.post(environment.apiUrl + this.CUSTOMER_URL, customer, httpOptions)
      .pipe(
        map(
          userData => {
            return userData;
          }
        )
      );
  }

  login(username: string, password: string): Observable<Object> {

    const token = btoa(username + ':' + password);

    const header = {
      Authorization: 'Basic ' + token
    };

    const options = { headers: header, responseType: 'text' as 'text' };

    return this.httpClient.get(environment.apiUrl + this.AUTH_URL, options).pipe(
      map(
        userData => {
          sessionStorage.setItem('token', token);
          sessionStorage.setItem('customerId', userData);
          this.loggedIn.next(true);
          return userData;
        }
      )
    );
  }

  logout() {
    this.loggedIn.next(false);
    sessionStorage.clear();
    localStorage.clear();
  }

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  getCustomer(): Observable<Customer> {

    const headers = this.getHeaders();

    const url = this.getCustomerUrl();

    return this.httpClient.get<Customer>(url, headers).pipe(
      map(
        userData => {
          return userData;
        }
      )
    );
  }

  updateCustomer(firstName: string, lastName: string, address: string): Observable<Customer> {

    const headers = this.getHeaders();

    const updateRequest = {
      "firstName": firstName,
      "lastName": lastName,
      "address": address
    }

    const url = this.getCustomerUrl();

    return this.httpClient.put<Customer>(url, updateRequest, headers).pipe(
      map(
        userData => {
          return userData;
        }
      )
    );
  }

  deleteCustomer(): Observable<any> {

    const headers = this.getHeaders();

    const url = this.getCustomerUrl();

    return this.httpClient.delete(url, headers).pipe(
      map(
        userData => {
          this.loggedIn.next(false);
          sessionStorage.clear();
          localStorage.clear();
          return userData;
        }
      )
    );
  }

  getInvoices(): Observable<Array<Invoice>> {

    const headers = this.getHeaders();

    const url = this.getInvoiceUrl()

    return this.httpClient.get<Array<Invoice>>(url, headers).pipe(
      map(
        userData => {
          return userData;
        }
      )
    );
  }

  getInvoice(invoiceId: string): Observable<Invoice> {

    const headers = this.getHeaders();

    const url = this.getInvoiceUrl() + '/' + invoiceId;
    return this.httpClient.get<Invoice>(url, headers).pipe(
      map(
        userData => {
          return userData;
        }
      )
    );
  }

  updateInvoice(invoiceId: string, invoice: Invoice): Observable<Invoice> {

    const headers = this.getHeaders();

    const url = this.getInvoiceUrl() + '/' + invoiceId;

    return this.httpClient.put<Invoice>(url, invoice, headers).pipe(
      map(
        userData => {
          return userData;
        }
      )
    );
  }

  createInvoice(invoice: Invoice): Observable<any> {

    const headers = this.getHeaders();

    const url = this.getInvoiceUrl();

    return this.httpClient.post(url, invoice, headers).pipe(
      map(
        userData => {
          return userData;
        }
      )
    );
  }

  deleteInvoice(invoiceId: string): Observable<any> {

    const headers = this.getHeaders();

    const url = this.getInvoiceUrl() + '/' + invoiceId;

    return this.httpClient.delete(url, headers).pipe(
      map(
        userData => {
          return userData;
        }
      )
    );
  }

  private getCustomerUrl(): string {
    return environment.apiUrl + this.CUSTOMER_URL + '/' + sessionStorage.getItem('customerId');
  }

  private getInvoiceUrl(): string {
    return environment.apiUrl + this.CUSTOMER_URL + '/' + sessionStorage.getItem('customerId') + this.INVOICE_URL;
  }

  private getHeaders() {
    return {
      headers: new HttpHeaders({
        Authorization: 'Basic ' + sessionStorage.getItem('token')
      })
    };
  }

}