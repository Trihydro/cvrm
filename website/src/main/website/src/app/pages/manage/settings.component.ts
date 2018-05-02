// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Auth } from '../../services/auth.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { AuthHttp } from 'angular2-jwt';
import { UserPassword } from '../../classes/user-password';
import { UserService } from '../../services/user.service';
import { UserTicket } from '../../classes/user-ticket';
import { environment } from '../../../environments/environment';

@Component({
	selector: 'pt-settings',
	templateUrl: './settings.component.html',
	providers: [Auth, UserService]
})
export class SettingsComponent implements OnInit{

	password: string;
	confirmPassword: string;
	organization: string;
	saveSuccess: boolean;
	lengthReq: boolean = true;
	lowerReq: boolean = true;
	upperReq: boolean = true;
	numberReq: boolean = true;
	symbolReq: boolean = true;
	passwordsMatch: boolean = true;
	private webUrl: string;
	errorMessage: string;

	constructor(private auth: Auth, private authHttp: AuthHttp, private router: Router, private userService: UserService) {  
   	 
	}
  
	ngOnInit(){		
		this.saveSuccess = false;				
		this.webUrl = environment.webUrl;
	}
	
	submitForm(): void { 

		let userTicket = new UserTicket();
		userTicket.resultUrl = `${this.webUrl}`;
		userTicket.userId = this.auth.userProfile.user_id;

		this.userService
		.getPasswordTicket(userTicket).subscribe(
			r => userTicket = r,
			e => this.errorMessage = e,
			() => { console.log(userTicket.ticketUrl); window.location.href=userTicket.ticketUrl; }
		);
	}
	
}