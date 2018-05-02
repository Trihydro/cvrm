// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Location }               from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
	selector: 'participant-detail',
	templateUrl: './participant-detail.component.html',
	providers: []
})
export class ParticipantDetailComponent implements OnInit{ 
	tabParticipant: boolean;
	tabVehicles: boolean;
	tabTraining: boolean;
	participantId: number;

	constructor( private route: ActivatedRoute ){}

	ngOnInit(): void {
     	this.tabParticipant = true;

		this.route.params.forEach((params: Params) => {
	      	if (params['id'] !== undefined) {
	        	this.participantId = +params['id'];	        				
	        }
	        if (params['tab'] !== undefined) {
	        	if(params['tab'] == "tabVehiclesSelect")
	        		this.tabVehiclesSelect();   				
	        	if(params['tab'] == "tabTrainingSelect")
	        		this.tabTrainingSelect();   				
	        }
        });    	
 	}

	tabVehiclesSelect()
	{
		this.tabParticipant = false;
		this.tabVehicles = true;
		this.tabTraining = false;
	}

	tabTrainingSelect()
	{
		this.tabParticipant = false;
		this.tabVehicles = false;
		this.tabTraining = true;
	}

	tabParticipantSelect()
	{
		this.tabParticipant = true;
		this.tabVehicles = false;
		this.tabTraining = false;
	}
}