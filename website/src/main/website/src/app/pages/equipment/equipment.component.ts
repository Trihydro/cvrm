// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { EquipmentService } from '../../services/equipment.service';
import { Equipment } from '../../classes/equipment';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { Auth } from '../../services/auth.service';

@Component({
	selector: 'pt-equipment',
	templateUrl: './equipment.component.html',
	providers: [EquipmentService, Auth]
})
export class EquipmentComponent implements OnInit{

	equipment: Equipment[] = [];
  	errorMessage: string = '';
  	isLoading: boolean = true;
  	selectedEquipment: Equipment;
	rowsOnPage = 10;

   	constructor(private equipmentService : EquipmentService, private router: Router, private auth: Auth){ }

	ngOnInit(){			
		this.equipmentService.getAll().subscribe(
		 /* happy path */ eq => this.equipment = eq,
         /* error path */ e => this.errorMessage = e,
         /* onComplete */ () => this.isLoading = false
		);
	}

	gotoDetail(equipment: Equipment): void {
		let link = ['/equipmentDetail', equipment.equipmentId];
  		this.router.navigate(link);
	}

	addNewDetail(): void {
		let link = ['/equipmentDetail'];
  		this.router.navigate(link);
	}

	onSelect(e)
	{
		this.selectedEquipment = e;
	}

	onEdit(e)
	{
		let link = ['/equipmentDetail'];
  		this.router.navigate(link, e);
	}

	delete(): void {
		this.equipmentService.delete(this.selectedEquipment.equipmentId)
		.subscribe((r: Response) => {console.log('success'); this.ngOnInit(); });
	}
}