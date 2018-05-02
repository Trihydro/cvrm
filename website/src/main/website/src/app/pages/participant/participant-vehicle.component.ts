// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit, Input } from '@angular/core';
import { ParticipantVehicle } from '../../classes/participant-vehicle';
import { ParticipantVehicleService } from '../../services/participant-vehicle.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { ActivatedRoute, Params } from '@angular/router';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'pt-participant-vehicles',
	templateUrl: './participant-vehicle.component.html',
	providers: [ParticipantVehicleService, Auth]
})
export class ParticipantVehicleComponent implements OnInit{ 
 
   	@Input() participantId: number;
    participantVehicles: ParticipantVehicle[] = [];
	errorMessage: string = '';
	isLoading: boolean = true;
	selectedVehicle: ParticipantVehicle;
	rowsOnPage = 10;

 	constructor(private participantVehicleService : ParticipantVehicleService, private router: Router, private route: ActivatedRoute,
 		private auth: Auth){ }

 	ngOnInit(){			
		this.route.params.forEach((params: Params) => {
	      	if (params['id'] !== undefined) {
	        	this.participantId = +params['id'];	        				
	        }
        });  

		this.participantVehicleService.getParticipantVehicles(this.participantId).subscribe(
		 /* happy path */ v => this.participantVehicles = v,
         /* error path */ e => this.errorMessage = e,
         /* onComplete */ () => { this.isLoading = false; console.log(this.participantVehicles); }
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
		this.participantVehicleService.removeParticipantVehicle(this.selectedVehicle.participantVehicleId)
		.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}
}
