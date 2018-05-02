// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Vehicle } from '../../classes/vehicle';
import { Equipment } from '../../classes/equipment';
import { VehicleEquipment } from '../../classes/vehicle-equipment';
import { VehicleService } from '../../services/vehicle.service';
import { EquipmentService } from '../../services/equipment.service';
import { VehicleEquipmentService } from '../../services/vehicle-equipment.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { Location }               from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
	selector: 'pt-vehicles',
	templateUrl: './vehicle-equipment-available-list.component.html',
	providers: [VehicleService, EquipmentService, VehicleEquipmentService]
})
export class VehicleEquipmentAvailableListComponent implements OnInit{ 
 
     equipment: Equipment[] = [];
	 errorMessage: string = '';
	 isLoading: boolean = true;
	 selectedEquipment: Equipment;
	 vehicleId: number;
	 rowsOnPage = 10;
	 vehicle: Vehicle;

 	constructor(private vehicleService: VehicleService, private route: ActivatedRoute, private equipmentService: EquipmentService,
 		private router: Router, private location: Location, private vehicleEquipmentService: VehicleEquipmentService){ }

 	ngOnInit(){		
 	 	this.selectedEquipment = new Equipment();
		this.vehicle = new Vehicle();
 		this.route.params.forEach((params: Params) => {
	      	if (params['id'] !== undefined) {
	        	this.vehicleId = +params['id'];	        				
	        }
         });  	

		this.equipmentService.getAvailableEquipment(this.vehicleId).subscribe(
			eq => this.equipment = eq,
		  	e => this.errorMessage = e,
		 	() => { this.isLoading = false; console.log(this.equipment); }
		);

		this.vehicleService.getVehicle(this.vehicleId).subscribe(
			v => this.vehicle = v,
		  	e => this.errorMessage = e,
		 	() => { this.isLoading = false; console.log(this.vehicle); }
		);		
	}

	addEquipmentToVehicle(): void {

		let ve = new VehicleEquipment();		
		ve.vehicleId = this.vehicleId;
		ve.equipmentId = this.selectedEquipment.equipmentId;		
	
		this.vehicleEquipmentService
	      	.addVehicleEquipment(ve)
	      	.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });      	
	}

	goBack(): void {
    	this.location.back();
  	}	

	onSelect(e)
	{
		this.selectedEquipment = e;
	}
}
