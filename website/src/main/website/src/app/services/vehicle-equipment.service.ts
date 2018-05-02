// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { VehicleEquipment } from '../classes/vehicle-equipment';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class VehicleEquipmentService{

	private baseUrl: string;

	constructor(private authHttp : AuthHttp){ 
		this.baseUrl = environment.baseUrl + "/cvpt";
	}

    addVehicleEquipment(ve: VehicleEquipment) : Observable<Response>{
    	return this.authHttp  
      	.post(`${this.baseUrl}/vehicles/${ve.vehicleId}/equipment`, JSON.stringify(ve), {headers: this.getHeaders()});
    }

    getVehicleEquipment(id: number): Observable<VehicleEquipment[]>{
		let vehicleEquipment$ = this.authHttp
		.get(this.baseUrl + `/vehicles/${id}/equipment`, {headers: this.getHeaders()})
		.map((res:Response) => res.json())
		.catch(handleError);
		return vehicleEquipment$;
    }

    private getHeaders(){
	    let headers = new Headers();
	    headers.append('Content-Type', 'application/json');
	    return headers;
    }

    removeVehicleEquipment(id: number): Observable<Response> {  
		return this.authHttp
	  	.delete(this.baseUrl + `/vehicles/equipment/${id}`, {headers: this.getHeaders()});
  	}
}

function handleError (error: any) {
	let errorMsg = error.message || `An error has occured.`
	console.error(errorMsg);
	return Observable.throw(errorMsg);
}
