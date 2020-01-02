import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from "@angular/forms";
import { ApiService } from '../api.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  constructor(private router: Router, private fb: FormBuilder, private apiService: ApiService, private toastr: ToastrService) { }

  ngOnInit() {
    this.reactiveForm()
  }

  private reactiveForm() {
    this.loginForm = this.fb.group({
      email: [''],
      password: ['']
    })
  }

  login() {
    this.apiService.login(this.loginForm.get("email").value, this.loginForm.get("password").value).subscribe(
      () => {
        this.toastr.success('Customer logged in successfully!');
        this.router.navigate(['/customer'])
      },
      () => {
        this.toastr.error('Please enter valid email and password!');
      }
    );
  }

}
