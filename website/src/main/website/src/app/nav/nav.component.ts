// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component} from '@angular/core';
import { Auth } from '../services/auth.service'
import { environment } from '../../environments/environment';

@Component({
	selector: 'pt-nav',
	templateUrl: './nav.component.html',
	providers: [Auth]
})
export class NavComponent{

	participantActive: string;
	vehiclesActive: string;
	equipmentActive: string;
	trainingActive: string;
	devEnv: boolean;

  	constructor(private auth: Auth) { 				
    	if(environment.envName == "dev"){
    		this.devEnv = true;
    	}
    	else{
    		this.devEnv = false;
    	}
  	}

    onParticipantsSelected(): void {    	
		this.participantActive = "active";
		this.vehiclesActive = "";    
		this.equipmentActive = "";   
		this.trainingActive = "";   
	}	

	onVehiclesSelected(): void {    	
		this.participantActive = "";
		this.vehiclesActive = "active";    
		this.equipmentActive = "";   
		this.trainingActive = "";   
	}	

	onEquipmentSelected(): void {    	
		this.participantActive = "";
		this.vehiclesActive = "";    
		this.equipmentActive = "active";   
		this.trainingActive = "";   
	}	

	onTrainingSelected(): void {    	
		this.participantActive = "";
		this.vehiclesActive = "";    
		this.equipmentActive = "";   
		this.trainingActive = "active";   
	}

	onDashboardSelected(): void {  
		this.participantActive = "";
		this.vehiclesActive = "";    
		this.equipmentActive = "";   
		this.trainingActive = "";   
	}
		
}