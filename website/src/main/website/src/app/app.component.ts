// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component } from '@angular/core';
import { Auth } from './services/auth.service'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  providers: [Auth]
})
export class AppComponent {
	constructor(private auth: Auth) {
		this.auth.handleAuthenticationWithHash();
  	}
}
