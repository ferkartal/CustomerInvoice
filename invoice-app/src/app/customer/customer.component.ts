import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Invoice } from '../invoice.model';
import { MatTableDataSource } from '@angular/material/table';
import { ToastrService } from 'ngx-toastr';
import { ErrorService } from '../error.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent implements OnInit {

  customerForm: FormGroup;
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ['id', 'totalPrice', 'createdDate', 'action'];

  constructor(private apiService: ApiService, private fb: FormBuilder, private router: Router,
    private errorService: ErrorService, private toastr: ToastrService) {

    this.customerForm = this.fb.group({
      id: '',
      firstName: '',
      lastName: '',
      email: '',
      phoneNumber: '',
      address: '',
      birthDate: ''
    })

  }

  ngOnInit() {
    this.apiService.getCustomer().subscribe(data => {
      this.customerForm = this.fb.group({
        id: [data.id],
        firstName: [data.firstName],
        lastName: [data.lastName],
        email: [data.email],
        phoneNumber: [data.phoneNumber],
        address: [data.address],
        birthDate: [data.birthDate]
      })
      this.apiService.getInvoices().subscribe(data => {
        this.dataSource = new MatTableDataSource<Invoice>(data);
      }, error => {
        this.errorService.handleError(error)
      })
    }, error => {
      this.errorService.handleError(error)
    })
  }

  updateCustomer() {
    this.apiService.updateCustomer(this.customerForm.get("firstName").value, this.customerForm.get("lastName").value,
      this.customerForm.get("address").value).subscribe(() => {
        this.toastr.success('Customer information is updated successfully!');
      }, error => {
        this.errorService.handleError(error)
      })
  }

  deleteCustomer() {
    this.apiService.deleteCustomer().subscribe(() => {
      this.toastr.success('Customer is deleted successfully!');
      this.router.navigate(['/register'])
    }, error => {
      this.errorService.handleError(error)
    })
  }

  deleteInvoice(element: Invoice, index: number) {
    this.apiService.deleteInvoice(element.id).subscribe(data => {
      const ds = this.dataSource.data;
      ds.splice(index, 1);
      this.dataSource.data = ds;
      this.toastr.success('Invoice is deleted successfully!');
    }, error => {
      this.errorService.handleError(error)
    })
  }

  updateInvoice(element: Invoice) {
    this.router.navigate(['/invoice', element.id]);
  }

  createInvoice() {
    this.router.navigate(['/invoice'])
  }

}