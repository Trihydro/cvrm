// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Training } from '../../classes/training';
import { TrainingType } from '../../classes/training-type';
import { TrainingService } from '../../services/training.service';
import { TrainingTypeService } from '../../services/training-type.service';
import { Router }	from '@angular/router';
import { Location }               from '@angular/common';
import { Response } from '@angular/http';
import { ActivatedRoute, Params } from '@angular/router';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'training-detail',
	templateUrl: './training-detail.component.html',
	providers: [TrainingService, TrainingTypeService, Auth]
})
export class TrainingDetailComponent implements OnInit{ 
	training: Training;
	trainingTypes: TrainingType[] = [];
	errorMessage: string = '';
   	isLoading: boolean = true;	 

	constructor(private trainingService : TrainingService, 
		private trainingTypeService : TrainingTypeService, 
		private router: Router, 
		private location: Location,
		private route: ActivatedRoute,
		private auth: Auth ){  }

	 ngOnInit(): void {
     	this.training = new Training();

		this.route.params.forEach((params: Params) => {
			if (params['id'] !== undefined) {
				let id = +params['id'];
				this.trainingService.getTraining(id).subscribe(
					v => this.training = v,
					e => this.errorMessage = e,
					() => {this.isLoading = false; console.log(this.training); }
				);				
			}
        });

  		this.trainingTypeService.getAll().subscribe(
			t => this.trainingTypes = t,
			e => this.errorMessage = e,
			() => this.isLoading = false
		);
	}

	submitForm(): void { 
		if(this.training.trainingId == null){
			this.trainingService
		  	.add(this.training)
		  	.subscribe((r: Response) => {console.log('success'); this.goBack(); })
		}
		else {
			this.trainingService
	  		.update(this.training)
	  		.subscribe((r: Response) => {console.log('success'); this.goBack(); })
		}
	}

	goBack(): void {
    	this.location.back();
  	}

}