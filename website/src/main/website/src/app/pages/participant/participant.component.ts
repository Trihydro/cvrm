// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Participant } from '../../classes/participant';
import { ParticipantService } from '../../services/participant.service';
import { Auth } from '../../services/auth.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';

@Component({
	selector: 'pt-participants',
	templateUrl: './participant.component.html',
	providers: [ParticipantService, Auth]
})
export class ParticipantComponent implements OnInit{ 
  
	participants: Participant[] = [];
	errorMessage: string = '';
	isLoading: boolean = true;
	selectedParticipant: Participant;
    rowsOnPage = 10;
 	constructor(private participantService : ParticipantService, private router: Router, private auth: Auth){ }

 	ngOnInit(){			
		this.participantService.getAll().subscribe(
		 /* happy path */ p => this.participants = p,
         /* error path */ e => this.errorMessage = e,
         /* onComplete */ () => this.isLoading = false
		);
		console.log("Can edit: " + this.auth.canEditParticipantData());
	}

	addNewDetail(): void {
		let link = ['/participantDetail'];
  		this.router.navigate(link);
	}

	onSelect(p)
	{
		this.selectedParticipant = p;
	}

  	delete(): void {
		this.participantService.delete(this.selectedParticipant.participantId)
		.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}
}