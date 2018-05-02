// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit, Input } from '@angular/core';
import { ParticipantTraining } from '../../classes/participant-training';
import { ParticipantTrainingService } from '../../services/participant-training.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { ActivatedRoute, Params } from '@angular/router';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'pt-participant-training',
	templateUrl: './participant-training.component.html',
	providers: [ParticipantTrainingService, Auth]
})
export class ParticipantTrainingComponent implements OnInit{ 
 
   	@Input() participantId: number;
    participantTrainings: ParticipantTraining[] = [];
	errorMessage: string = '';
	isLoading: boolean = true;
	selectedTraining: ParticipantTraining;
	rowsOnPage = 10;
	hoursInput: number;
	minutesInput: number;
	dateCompleted: string;

 	constructor(private participantTrainingService : ParticipantTrainingService, private router: Router, private route: ActivatedRoute, private auth: Auth){ }

 	ngOnInit(){			
 		this.selectedTraining = new ParticipantTraining();
		this.route.params.forEach((params: Params) => {
	      	if (params['id'] !== undefined) {
	        	this.participantId = +params['id'];	        				
	        }
        });  

		this.participantTrainingService.getParticipantTraining(this.participantId).subscribe(
		 /* happy path */ t => this.participantTrainings = t,
         /* error path */ e => this.errorMessage = e,
         /* onComplete */ () => { this.isLoading = false; console.log(this.participantTrainings); this.processTimes();}
		);

	}

	processTimes(): void {
		for(let pt of this.participantTrainings){
			if(pt.timeToComplete > 0)
				pt.timeToCompleteString = this.convertMinutesToHoursMinutes(pt.timeToComplete);		
		}
	}

	convertMinutesToHoursMinutes(minutes): string {
		if(minutes == null){
			this.hoursInput = 0;
			this.minutesInput = 0;
			return null;
		}
		let hours: number = Math.floor(minutes / 60);
		let mins = minutes - (hours * 60);
		if(hours > 0){
			this.hoursInput = hours;
			this.minutesInput = mins;
			return `${hours} hours ${mins} minutes`;
		}
		else{
			this.minutesInput = mins;
			return `${mins} minutes`;
		}
	}

	addNewDetail(): void {
		let link = ['/trainingDetail'];
  		this.router.navigate(link);
	}

	onSelect(t): void {
		this.selectedTraining = t;
		this.convertMinutesToHoursMinutes(this.selectedTraining.timeToComplete);
	}

	save(): void {
		if(this.dateCompleted != null)
			this.selectedTraining.dateCompleted = this.dateCompleted;
		this.selectedTraining.timeToComplete = this.convertHoursMinutesToMinutes();
		this.participantTrainingService.updateParticipantTraining(this.selectedTraining)
      	.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}

	convertHoursMinutesToMinutes(): number {
		let minutes: number = 0;
		if(this.hoursInput != null){
			minutes = this.hoursInput * 60;
		}
		if(this.minutesInput != null){
			minutes = minutes + this.minutesInput; 
		}
		
		return minutes;		
	}

  	delete(): void {
		this.participantTrainingService.removeParticipantTraining(this.selectedTraining.participantTrainingId)
		.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}
}
