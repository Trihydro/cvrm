// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Equipment } from '../../classes/equipment';
import { EquipmentComponentType } from '../../classes/equipment-component-type';
import { EquipmentTypeService } from '../../services/equipment-type.service';
import { EquipmentService } from '../../services/equipment.service';
import { EquipmentComponentService } from '../../services/equipment-component.service';
import { EquipmentComponentTypeService } from '../../services/equipment-component-type.service';
import { EquipmentComp  } from '../../classes/equipment-component';
import { Router }	from '@angular/router';
import { Location }               from '@angular/common';
import { Response } from '@angular/http';
import { ActivatedRoute, Params } from '@angular/router';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'equipment-component-detail',
	templateUrl: './equipment-component-detail.component.html',
	providers: [EquipmentTypeService, EquipmentService, EquipmentComponentService, EquipmentComponentTypeService, Auth]
})
export class EquipmentComponentDetailComponent implements OnInit{
	equipmentComp: EquipmentComp;
	errorMessage: string = '';
  	isLoading: boolean = true;	
  	navigated = false;
  	equipmentComponentTypes: EquipmentComponentType[] = [];
	
	constructor(private equipmentTypeService : EquipmentTypeService, 
		private equipmentComponentService : EquipmentComponentService,
		private equipmentComponentTypeService: EquipmentComponentTypeService,
		private equipmentService : EquipmentService, 
		private router: Router, 
		private location: Location,
		private route: ActivatedRoute,
		private auth: Auth ){  }

	ngOnInit(): void {
	    this.equipmentComp = new EquipmentComp();

		this.route.params.forEach((params: Params) => {
	      	if (params['equipmentId'] !== undefined) {
	        	let equipmentId = +params['equipmentId'];
	        	this.equipmentComp.equipmentId = equipmentId;
	        	if(params['equipmentComponentId'] !== undefined){
	        		let equipmentComponentId = +params['equipmentComponentId'];
	        		this.equipmentComponentService.getEquipmentComponent(equipmentComponentId).subscribe(
						eq => this.equipmentComp = eq,
						e => this.errorMessage = e,
						() => {this.isLoading = false; console.log(this.equipmentComp); }
					);
	        	}
	        }
        });

    	this.equipmentComponentTypeService.getAll().subscribe(
			eq => this.equipmentComponentTypes = eq,
			e => this.errorMessage = e,
			() => { this.isLoading = false; console.log(this.equipmentComponentTypes); }
		);
	}

  	submitForm(): void {
  		console.log("comp: " + this.equipmentComp);
		this.equipmentComponentService
	      .save(this.equipmentComp)
	      .subscribe((r: Response) => {console.log('success'); this.goBack(); })
  	}

	goBack(): void {
    	this.location.back();
  	}
}