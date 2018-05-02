// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Vehicle } from '../../classes/vehicle';
import { Participant } from '../../classes/participant';
import { ParticipantVehicle } from '../../classes/participant-vehicle';
import { VehicleService } from '../../services/vehicle.service';
import { ParticipantService } from '../../services/participant.service';
import { ParticipantVehicleService } from '../../services/participant-vehicle.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { Location }               from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
	selector: 'pt-vehicles',
	templateUrl: './participant-vehicle-available-list.component.html',
	providers: [VehicleService, ParticipantService, ParticipantVehicleService]
})
export class ParticipantVehicleAvailableListComponent implements OnInit{ 
 
    vehicles: Vehicle[] = [];
	errorMessage: string = '';
	isLoading: boolean = true;
	selectedVehicle: Vehicle;
	participantId: number;
	rowsOnPage = 10;
	participant: Participant;
    isPrimaryVehicle: boolean;

 	constructor(private vehicleService: VehicleService, private participantService: ParticipantService, 
 		private participantVehicleService: ParticipantVehicleService, private route: ActivatedRoute, 
 		private router: Router, private location: Location){ }

 	ngOnInit(){		
 		this.selectedVehicle = new Vehicle();
		this.participant = new Participant();
 		this.route.params.forEach((params: Params) => {
	      	if (params['id'] !== undefined) {
	        	this.participantId = +params['id'];	        				
	        }
        });  	
		this.vehicleService.getAvailableVehicles(this.participantId).subscribe(
			v => this.vehicles = v,
		  	e => this.errorMessage = e,
		 	() => { this.isLoading = false; console.log(this.vehicles); }
		);
		this.participantService.getParticipant(this.participantId).subscribe(
			p => this.participant = p,
		  	e => this.errorMessage = e,
		 	() => { this.isLoading = false; console.log(this.participant); }
		);		
	}

	addVehicleToParticipant(): void {
		let pv = new ParticipantVehicle();
		pv.vehicleId = this.selectedVehicle.vehicleId;
		pv.participantId = this.participantId;
		if(this.isPrimaryVehicle)
			pv.isPrimary = "Y";
		else
			pv.isPrimary = "N";
	
		this.participantVehicleService
	      	.addParticipantVehicle(pv)
	      	.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
      	this.isPrimaryVehicle = false;
	}

	goBack(): void {
    	this.location.back();
  	}	

	onSelect(v)
	{
		this.selectedVehicle = v;
	}

	cancel(v)
	{
		this.isPrimaryVehicle = false;
	}
}
