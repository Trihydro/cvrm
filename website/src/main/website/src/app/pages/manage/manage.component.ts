// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Response } from '@angular/http';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'pt-manage',
	templateUrl: './manage.component.html',
	providers: [Auth]
})
export class ManageComponent implements OnInit{ 
  
	tabSettings: boolean;
	tabAdmin: boolean;

 	constructor(private auth: Auth){ }

 	ngOnInit(){			 		
		this.tabSettings = true;			
	}

	tabSettingsSelect()
	{
		this.tabSettings = true;
		this.tabAdmin = false;
	}

	tabAdminSelect()
	{
		this.tabSettings = false;
		this.tabAdmin = true;
	}

}