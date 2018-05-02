// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Vehicle } from '../../classes/vehicle';
import { VehicleService } from '../../services/vehicle.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'pt-vehicles',
	templateUrl: './vehicle.component.html',
	providers: [VehicleService, Auth]
})
export class VehicleComponent implements OnInit{ 
 
    vehicles: Vehicle[] = [];
	errorMessage: string = '';
	isLoading: boolean = true;
	selectedVehicle: Vehicle;
	rowsOnPage = 10;

 	constructor(private vehicleService : VehicleService, private router: Router, private auth: Auth){ }

 	ngOnInit(){			
		this.vehicleService.getAll().subscribe(
		 /* happy path */ v => this.vehicles = v,
         /* error path */ e => this.errorMessage = e,
         /* onComplete */ () => { this.isLoading = false; console.log(this.vehicles); }
		);
	}

	addNewDetail(): void {
		let link = ['/vehicleDetail'];
  		this.router.navigate(link);
	}

	onSelect(v)
	{
		this.selectedVehicle = v;
	}

  	delete(): void {
		this.vehicleService.delete(this.selectedVehicle.vehicleId)
		.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}
}
