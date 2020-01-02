import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from "@angular/forms";
import { ApiService } from '../api.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ErrorService } from '../error.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;

  constructor(private router: Router, private fb: FormBuilder, private apiService: ApiService,
     private toastr: ToastrService, private errorService: ErrorService) { }

  ngOnInit() {
    this.reactiveForm()
  }

  reactiveForm() {
    this.registerForm = this.fb.group({
      firstName: [''],
      lastName: [''],
      email: [''],
      password: [''],
      phoneNumber: [''],
      address: [''],
      birthDate: ['']
    })
  }

  register() {

    this.apiService.createCustomer(this.registerForm.value).subscribe(
      () => {
        this.toastr.success('Your account is created successfully!');
        this.router.navigate(['/login'])
      },
      error => {
        this.errorService.handleError(error)
      }
    )
  }
}