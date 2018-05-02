// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit, Input } from '@angular/core';
import { Participant } from '../../classes/participant';
import { Organization } from '../../classes/organization';
import { OrganizationService } from '../../services/organization.service';
import { ParticipantService } from '../../services/participant.service';
import { Location }               from '@angular/common';
import { Response } from '@angular/http';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'participant-form',
	templateUrl: './participant-form.component.html',
	providers: [OrganizationService, ParticipantService, Auth]
})
export class ParticipantFormComponent implements OnInit{ 

    @Input() participantId: number;
	participant: Participant;
    organizations: Organization[] = []; 
	errorMessage: string = '';
   	isLoading: boolean = true;	
	
	constructor(private organizationService : OrganizationService, 
		private participantService : ParticipantService,
		private location: Location, private auth: Auth ){  }

	 ngOnInit(): void {

     	this.participant = new Participant();

		if(this.participantId != null)
		{
        	this.participantService.getParticipant(this.participantId).subscribe(
				p => this.participant = p,
				e => this.errorMessage = e,
				() => {this.isLoading = false; console.log(this.participant); }
			);				
        }

    	this.organizationService.getAll().subscribe(
			o => this.organizations = o,
			e => this.errorMessage = e,
			() => this.isLoading = false
		);
	 }

  	submitForm(): void { 
  		console.log(this.participant);
  		if(this.participant.participantId == null){
			this.participantService
	      	.add(this.participant)
	      	.subscribe((r: Response) => {console.log('success'); this.goBack(); })
	  	}
	  	else {
	  		this.participantService
	      	.update(this.participant)
	      	.subscribe((r: Response) => {console.log('success'); this.goBack(); })
	  	}
  	}

	goBack(): void {
    	this.location.back();
  	}	
}