// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Auth } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, Params } from '@angular/router';
import { User } from '../../classes/user';
import { UserPassword } from '../../classes/user-password';
import { AuthorizeToken } from '../../classes/authorize-token';
import { Response } from '@angular/http';
import { Router }  from '@angular/router';
import { UserTicket } from '../../classes/user-ticket';
import { environment } from '../../../environments/environment';
declare var jQuery:any;

@Component({
  selector: 'pt-set-password',
  templateUrl: './set-password.component.html',
  providers: [UserService]
})
export class SetPasswordComponent implements OnInit{
  password: string;
  confirmPassword: string;
  jwt: string;
  newUser: User;
  userToken: string;
  errorMessage: string = '';
  isLoading: boolean = true;
  userVerified: boolean;
  lengthReq: boolean = true;
  lowerReq: boolean = true;
  upperReq: boolean = true;
  numberReq: boolean = true;
  symbolReq: boolean = true;
  passwordsMatch: boolean = true;
  private webUrl: string;

  constructor(private auth: Auth, private userService: UserService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(){			
    this.userVerified = false;
    this.webUrl = environment.webUrl;
  	this.route.queryParams.subscribe((params: Params) => {
        this.userToken = params['userToken'];
        console.log("user token: " + this.userToken);
    this.newUser = new User();    
    this.newUser.token = this.userToken;
	  this.userService
      	.verifyUser(this.newUser)
      	.subscribe(r => this.newUser = r, e => this.errorMessage = e,  () => { this.isLoading = false; this.showPage(); })
 	  });  
  }
 
  showPage(){
    if(this.newUser.userId != null){
      this.userVerified = true;
    }
  }

  resetPassword(){
      let userPassword: UserPassword = new UserPassword();
      userPassword.password = this.password;
      userPassword.userId = this.newUser.userId;
      // this.userService
      //     .updatePassword(userPassword)
      //     .subscribe((r: Response) => { console.log(r); this.auth.logout(); this.router.navigate(['/login']); this.auth.login(); })

          this.userService
          .updatePassword(userPassword)
          .subscribe((r: Response) => { console.log(r); })    
  }

  submitForm(){

    let userTicket = new UserTicket();
		userTicket.resultUrl = `${this.webUrl}`;
		userTicket.userId = this.newUser.userId;
    console.log(this.errorMessage);

		this.userService.getNewUserTicket(userTicket).subscribe(
			r => userTicket = r,
			e => this.errorMessage = e,
			() => { console.log(this.errorMessage); console.log(userTicket.ticketUrl); window.location.href=userTicket.ticketUrl; }
		);
  }
   
}