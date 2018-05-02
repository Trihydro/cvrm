// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Equipment } from '../../classes/equipment';
import { EquipmentType } from '../../classes/equipment-type';
import { EquipmentTypeService } from '../../services/equipment-type.service';
import { EquipmentService } from '../../services/equipment.service';
import { EquipmentComponentService } from '../../services/equipment-component.service';
import { EquipmentComp } from '../../classes/equipment-component';
import { EquipmentComponentType } from '../../classes/equipment-component-type';
import { Router }	from '@angular/router';
import { Location }               from '@angular/common';
import { Response } from '@angular/http';
import { ActivatedRoute, Params } from '@angular/router';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'equipment-detail',
	templateUrl: './equipment-detail.component.html',
	providers: [EquipmentTypeService, EquipmentService, EquipmentComponentService, Auth]
})
export class EquipmentDetailComponent implements OnInit{
	equipment: Equipment;
	equipmentTypes: EquipmentType[] = []; 
	errorMessage: string = '';
  	isLoading: boolean = true;	
  	navigated = false;
  	equipmentComponents: EquipmentComponentType[] = [];
	selectedEquipmentComponent: EquipmentComp; 
	tabComponents: boolean;
	rowsOnPage = 10;

	constructor(private equipmentTypeService : EquipmentTypeService, 
		private equipmentComponentService : EquipmentComponentService,
		private equipmentService : EquipmentService, 
		private router: Router, 
		private location: Location,
		private route: ActivatedRoute,
		private auth: Auth ){  }

	ngOnInit(): void {
	    this.equipment = new Equipment();
		this.route.params.forEach((params: Params) => {
	      	if (params['id'] !== undefined) {
	        	let id = +params['id'];
	        	this.equipmentService.getEquipment(id).subscribe(
					eq => this.equipment = eq,
					e => this.errorMessage = e,
					() => {this.isLoading = false; console.log(this.equipment); }
				);
				this.equipmentComponentService.getAll(id).subscribe(
					ec => this.equipmentComponents = ec,
					e => this.errorMessage = e,
					() => {this.isLoading = false; console.log(this.equipmentComponents); }
				);
	        }
        });

    	this.equipmentTypeService.getAll().subscribe(
			eq => this.equipmentTypes = eq,
			e => this.errorMessage = e,
			() => this.isLoading = false
		);
	}

	addNewComponent(): void {
		let link = ['/equipmentComponentDetail'];
  		this.router.navigate(link);
	}x

  	submitForm(): void { 
		if(this.equipment.equipmentId == null){
			this.equipmentService
	      	.add(this.equipment)
	      	.subscribe((r: Response) => {console.log('success'); this.goBack(); })
	    }
	    else{
			this.equipmentService
	      	.update(this.equipment)
	      	.subscribe((r: Response) => {console.log('success'); this.goBack(); })
	    }
  	}

	goBack(): void {
    	this.location.back();
  	}

  	delete(): void {
		this.equipmentComponentService.delete(this.selectedEquipmentComponent.equipmentComponentId)
		.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}

	tabEquipmentSelect(e){
		this.tabComponents = false;
	}

	tabComponentsSelect(e){
		this.tabComponents = true;
	}

	onSelect(e){
		this.selectedEquipmentComponent = e;
	}
}