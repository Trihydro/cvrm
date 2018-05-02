// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit, Input } from '@angular/core';
import { VehicleEquipment } from '../../classes/vehicle-equipment';
import { VehicleEquipmentService } from '../../services/vehicle-equipment.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { ActivatedRoute, Params } from '@angular/router';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'pt-vehicle-equipment',
	templateUrl: './vehicle-equipment.component.html',
	providers: [VehicleEquipmentService, Auth]
})
export class VehicleEquipmentComponent implements OnInit{ 
 
   	@Input() vehicleId: number;
    vehicleEquipment: VehicleEquipment[] = [];
	errorMessage: string = '';
	isLoading: boolean = true;
	selectedEquipment: VehicleEquipment;
	rowsOnPage = 10;

 	constructor(private vehicleEquipmentService : VehicleEquipmentService, private router: Router, private route: ActivatedRoute,
 		private auth: Auth){ }

 	ngOnInit(){			
		this.route.params.forEach((params: Params) => {
			if (params['id'] !== undefined) {
				this.vehicleId = +params['id'];	        				
			}
		});  

		this.vehicleEquipmentService.getVehicleEquipment(this.vehicleId).subscribe(
	  		ve => this.vehicleEquipment = ve,
         	e => this.errorMessage = e,
         	() => { this.isLoading = false; console.log(this.vehicleEquipment); }
		);
	}

	onSelect(ve)
	{
		this.selectedEquipment = ve;
	}

  	delete(): void {
		this.vehicleEquipmentService.removeVehicleEquipment(this.selectedEquipment.vehicleEquipmentId)
		.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}
}
