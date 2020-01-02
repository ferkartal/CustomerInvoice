import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  constructor(private router: Router, private toastr: ToastrService) {
  }

  handleError(error: HttpErrorResponse) {
    if (error.status == 400 || error.status == 409 || error.status == 404) {
      error.error.errors.forEach((element: { message: string; }) => {
        this.toastr.error(element.message)
      });
    } else if (error.status == 401) {
      this.toastr.error('Unauthorized request!');
      this.router.navigate(['/login'])
    } else {
      this.toastr.error('Something wrong. Please try again later!');
      this.router.navigate(['/login'])
    }

  }

}