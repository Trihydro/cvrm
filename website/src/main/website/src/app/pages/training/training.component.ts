// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Training } from '../../classes/training';
import { TrainingService } from '../../services/training.service';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'pt-training',
	templateUrl: './training.component.html',
	providers: [TrainingService, Auth]
})
export class TrainingComponent implements OnInit{ 
 
    trainings: Training[] = [];
	errorMessage: string = '';
	isLoading: boolean = true;
	selectedTraining: Training;
  	rowsOnPage = 10;

 	constructor(private trainingService : TrainingService, private router: Router, private auth: Auth){ }

 	ngOnInit(){			
		this.trainingService.getAll().subscribe(
		 /* happy path */ t => this.trainings = t,
         /* error path */ e => this.errorMessage = e,
         /* onComplete */ () => { this.isLoading = false; console.log(this.trainings); }
		);
	}

	addNewDetail(): void {
		let link = ['/trainingDetail'];
  		this.router.navigate(link);
	}

	onSelect(t)
	{
		this.selectedTraining = t;
	}

  	delete(): void {
		this.trainingService.delete(this.selectedTraining.trainingId)
		.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}
}
