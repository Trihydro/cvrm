// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.


import { Component, OnInit } from '@angular/core';
import { Organization } from '../../classes/organization';
import { OrganizationService } from '../../services/organization.service';
import { Router }	from '@angular/router';
import { Location }               from '@angular/common';
import { Response } from '@angular/http';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
	selector: 'organization-detail',
	templateUrl: './organization-detail.component.html',
	providers: [OrganizationService]
})
export class OrganizationDetailComponent implements OnInit{ 
    organization: Organization; 
	errorMessage: string = '';
   	isLoading: boolean = true;	

	constructor(private organizationService : OrganizationService, 
		private router: Router, 
		private location: Location,
		private route: ActivatedRoute ){  }

	 ngOnInit(): void {
     	this.organization = new Organization();    
	 }

  	submitForm(): void {   		
  		console.log(this.organization.isTruckingCompany);
  		if(this.organization.isTruckingCompany){
			this.organization.isTruckingCompany = "Y";

  		}
  		else{
  			this.organization.isTruckingCompany = "N";
  		}
		this.organizationService
	      	.add(this.organization)
	      	.subscribe((r: Response) => {console.log('success'); this.goBack(); })
  	}

	goBack(): void {
    	this.location.back();
  	}
}