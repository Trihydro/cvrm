// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit, Input } from '@angular/core';
import { Equipment } from '../../classes/equipment';
import { EquipmentType } from '../../classes/equipment-type';
import { EquipmentComponentService } from '../../services/equipment-component.service';
import { EquipmentComp } from '../../classes/equipment-component';
import { EquipmentComponentType } from '../../classes/equipment-component-type';
import { Router }	from '@angular/router';
import { Location }               from '@angular/common';
import { Response } from '@angular/http';
import { ActivatedRoute, Params } from '@angular/router';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'equipment-component-list',
	templateUrl: './equipment-component-list.component.html',
	providers: [EquipmentComponentService, Auth]
})
export class EquipmentComponentListComponent implements OnInit{
	errorMessage: string = '';
  	isLoading: boolean = true;	
  	navigated = false;
  	equipmentComponents: EquipmentComponentType[] = [];
	selectedEquipmentComponent: EquipmentComp; 
	rowsOnPage = 10;
	@Input() equipmentId: number; 

	constructor(
		private equipmentComponentService : EquipmentComponentService,
		private router: Router, 
		private location: Location,
		private route: ActivatedRoute,
		private auth: Auth ){  }

	ngOnInit(): void {
		this.route.params.forEach((params: Params) => {
	      	if (params['id'] !== undefined) {
	        	let id = +params['id'];
	        	
				this.equipmentComponentService.getAll(id).subscribe(
					ec => this.equipmentComponents = ec,
					e => this.errorMessage = e,
					() => {this.isLoading = false; console.log(this.equipmentComponents); }
				);
	        }
        });
	}

	addNewComponent(): void {
		let link = ['/equipmentComponentDetail'];
  		this.router.navigate(link);
	}x

	goBack(): void {
    	this.location.back();
  	}

  	delete(): void {
		this.equipmentComponentService.delete(this.selectedEquipmentComponent.equipmentComponentId)
		.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}

	onSelect(e){
		this.selectedEquipmentComponent = e;
	}
}