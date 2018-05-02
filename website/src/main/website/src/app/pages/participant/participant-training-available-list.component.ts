// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Training } from '../../classes/training';
import { Participant } from '../../classes/participant';
import { ParticipantTraining } from '../../classes/participant-training';
import { TrainingService } from '../../services/training.service';
import { ParticipantService } from '../../services/participant.service';
import { ParticipantTrainingService } from '../../services/participant-training.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { Location }               from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
	selector: 'pt-training',
	templateUrl: './participant-training-available-list.component.html',
	providers: [TrainingService, ParticipantService, ParticipantTrainingService]
})
export class ParticipantTrainingAvailableListComponent implements OnInit{ 
 
    trainings: Training[] = [];
	errorMessage: string = '';
	isLoading: boolean = true;
	selectedTraining: Training;
	participantId: number;
	rowsOnPage = 10;
	participant: Participant;

 	constructor(private trainingService: TrainingService, private participantService: ParticipantService, 
 		private participantTrainingService: ParticipantTrainingService, private route: ActivatedRoute, 
 		private router: Router, private location: Location){ }

 	ngOnInit(){		
 		this.selectedTraining = new Training();
		this.participant = new Participant();
 		this.route.params.forEach((params: Params) => {
	      	if (params['id'] !== undefined) {
	        	this.participantId = +params['id'];	        				
	        }
        });  	
		this.trainingService.getAvailableTraining(this.participantId).subscribe(
			t => this.trainings = t,
		  	e => this.errorMessage = e,
		 	() => { this.isLoading = false; console.log(this.trainings); }
		);
		this.participantService.getParticipant(this.participantId).subscribe(
			p => this.participant = p,
		  	e => this.errorMessage = e,
		 	() => { this.isLoading = false; console.log(this.participant); }
		);		
	}

	addTrainingToParticipant(): void {
		let pt = new ParticipantTraining();
		pt.trainingId = this.selectedTraining.trainingId;
		pt.participantId = this.participantId;		
	
		this.participantTrainingService
	      	.addParticipantTraining(pt)
	      	.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}

	goBack(): void {
    	this.location.back();
  	}	

	onSelect(t)
	{
		this.selectedTraining = t;
	}

}
