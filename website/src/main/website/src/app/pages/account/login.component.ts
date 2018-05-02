// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component } from '@angular/core';
import { Auth } from '../../services/auth.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
	devEnv: boolean;
  	constructor(private auth: Auth) {
  		if(environment.envName == "dev"){
    		this.devEnv = true;
    	}
    	else{
    		this.devEnv = false;
    	}
  	}
};