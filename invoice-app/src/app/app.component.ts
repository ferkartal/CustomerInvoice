import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'invoice-app';

  isLoggedIn$: Observable<boolean>;

  constructor(private apiService: ApiService, private router: Router) { }

  ngOnInit() {
    this.isLoggedIn$ = this.apiService.isLoggedIn;
  }

  onLogout() {
    this.apiService.logout();
    this.router.navigate(["login"])
  }

}