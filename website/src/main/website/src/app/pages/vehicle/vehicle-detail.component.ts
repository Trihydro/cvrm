// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Vehicle } from '../../classes/vehicle';
import { VehicleClass } from '../../classes/vehicle-class';
import { Organization } from '../../classes/organization';
import { OrganizationService } from '../../services/organization.service';
import { VehicleService } from '../../services/vehicle.service';
import { VehicleClassService } from '../../services/vehicle-class.service';
import { Router }	from '@angular/router';
import { Location }               from '@angular/common';
import { Response } from '@angular/http';
import { ActivatedRoute, Params } from '@angular/router';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'vehicle-detail',
	templateUrl: './vehicle-detail.component.html',
	providers: [VehicleService, VehicleClassService, OrganizationService, Auth]
})
export class VehicleDetailComponent implements OnInit{ 
	vehicle: Vehicle;
	errorMessage: string = '';
   	isLoading: boolean = true;	
	tabVehicle: boolean;
	tabEquipment: boolean;
	vehicleClasses: VehicleClass[] = []; 
    organizations: Organization[] = [];

	constructor(private vehicleService : VehicleService, 
		private vehicleClassService : VehicleClassService,
		private organizationService : OrganizationService, 
		private router: Router, 
		private location: Location,
		private route: ActivatedRoute,
		private auth: Auth ){  }

	 ngOnInit(): void {
     	this.vehicle = new Vehicle();
     	this.tabVehicle = true;

		this.route.params.forEach((params: Params) => {
			if (params['id'] !== undefined) {
				let id = +params['id'];
				this.vehicleService.getVehicle(id).subscribe(
					v => this.vehicle = v,
					e => this.errorMessage = e,
					() => {this.isLoading = false; console.log(this.vehicle); }
				);				
			}
		    if (params['tab'] !== undefined) {
	        	if(params['tab'] == "tabEquipmentSelect")
	        	this.tabEquipmentSelect();   				
	        }
        });

		this.vehicleClassService.getAll().subscribe(
			v => this.vehicleClasses = v,
			e => this.errorMessage = e,
			() => this.isLoading = false
		);		

    	this.organizationService.getAll().subscribe(
			o => this.organizations = o,
			e => this.errorMessage = e,
			() => this.isLoading = false
		);
	}

	submitForm(): void { 
		if(this.vehicle.vehicleId == null){
			this.vehicleService
		  	.add(this.vehicle)
		  	.subscribe((r: Response) => {console.log('success'); this.goBack(); })
		}
		else {
			this.vehicleService
	  		.update(this.vehicle)
	  		.subscribe((r: Response) => {console.log('success'); this.goBack(); })
		}
	}

	goBack(): void {
    	this.location.back();
  	}

	tabVehicleSelect()
	{
		this.tabVehicle = true;
		this.tabEquipment = false;
	}

	tabEquipmentSelect()
	{
		this.tabVehicle = false;
		this.tabEquipment = true;
	}
}