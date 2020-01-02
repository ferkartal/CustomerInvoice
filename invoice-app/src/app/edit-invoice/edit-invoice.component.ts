import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material';
import { ApiService } from '../api.service';
import { InvoiceItem } from '../invoice-item.model';
import { ToastrService } from 'ngx-toastr';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import { ErrorService } from '../error.service';

@Component({
  selector: 'app-edit-invoice',
  templateUrl: './edit-invoice.component.html',
  styleUrls: ['./edit-invoice.component.css']
})
export class EditInvoiceComponent implements OnInit {

  invoiceId: string;
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ["description", 'amount', 'price', 'tax', 'action'];
  invoiceForm: FormGroup


  constructor(private apiService: ApiService, private fb: FormBuilder, private route: ActivatedRoute,
    private toastr: ToastrService, private router: Router, private errorService: ErrorService) { 
      this.invoiceForm = this.fb.group({
        invoiceItems: this.fb.array([])
      })
    }

  ngOnInit() {

    this.invoiceId = this.route.snapshot.params.id
    this.apiService.getInvoice(this.invoiceId).subscribe(data => {
      this.dataSource = new MatTableDataSource<InvoiceItem>(data.invoiceItems);
      this.initializeInvoiceItems()
    }, error => {
      this.errorService.handleError(error)
    })
  }

  private initializeInvoiceItems() {

    const invoiceItems = this.invoiceForm.get('invoiceItems') as FormArray;
    this.dataSource.data.forEach((invoiceItem) => {
      invoiceItems.push(this.fb.group({
        description: [invoiceItem.description],
        amount: [invoiceItem.amount],
        price: [invoiceItem.price],
        tax: [invoiceItem.tax]
      }))
    })
  }


  updateInvoice() {
    this.apiService.updateInvoice(this.invoiceId, this.invoiceForm.value).subscribe(data => {
      this.toastr.success('Invoice is updated successfully!');
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
      price: [0],
      tax: null
    }))
  }

  get invoiceArrayAliases() {
    return this.invoiceForm.controls.invoiceItems as FormArray;
  }
}
