import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { ApiService } from '../api.service';
import { InvoiceItem } from '../invoice-item.model';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import { ErrorService } from '../error.service';

@Component({
  selector: 'app-add-invoice',
  templateUrl: './add-invoice.component.html',
  styleUrls: ['./add-invoice.component.css']
})
export class AddInvoiceComponent implements OnInit {

  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ["description", 'amount', 'price', 'action'];
  invoiceForm: FormGroup

  constructor(private apiService: ApiService, private fb: FormBuilder, private router: Router, private toastr: ToastrService,
    private errorService: ErrorService) { }

  ngOnInit() {

    this.dataSource = new MatTableDataSource<InvoiceItem>();
    this.invoiceForm = this.fb.group({
      invoiceItems: this.fb.array([])
    })

  }

  addInvoice() {
    this.apiService.createInvoice(this.invoiceForm.value).subscribe(data => {
      this.toastr.success('Invoice is created successfully!');
      this.router.navigate(['/customer'])
    }, error => {
      this.errorService.handleError(error)
    })
  }

  deleteInvoiceItem(index: number) {
    const ds = this.dataSource.data;
    ds.splice(index, 1);
    this.dataSource.data = ds;
    this.invoiceArrayAliases.removeAt(index)
  }

  addInvoiceItem() {
    let model = { 'amount': 0, 'description': 'description', 'price': 0 };
    this.dataSource.data.push(model)
    this.dataSource.filter = "";

    this.invoiceArrayAliases.push(this.fb.group({
      description: ['description'],
      amount: [0],
      price: [0]
    }))
  }

  get invoiceArrayAliases() {
    return this.invoiceForm.controls.invoiceItems as FormArray;
  }
}
