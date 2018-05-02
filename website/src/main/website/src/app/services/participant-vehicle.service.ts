// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { ParticipantVehicle } from '../classes/participant-vehicle';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class ParticipantVehicleService{

    private baseUrl: string;

  	constructor(private authHttp: AuthHttp){ 
    	this.baseUrl = environment.baseUrl + "/cvpt";
  	}

    addParticipantVehicle(pv: ParticipantVehicle) : Observable<Response>{   
    	return this.authHttp
      	.post(`${this.baseUrl}/participants/${pv.participantId}/vehicles`, JSON.stringify(pv), {headers: this.getHeaders()});
    }

    getParticipantVehicles(id: number): Observable<ParticipantVehicle[]>{
		let participantVehicles$ = this.authHttp
		.get(this.baseUrl + `/participants/${id}/vehicles`, {headers: this.getHeaders()})
		.map((res:Response) => res.json())
		.catch(handleError);
		return participantVehicles$;
    }

    private getHeaders(){
	    let headers = new Headers();
	    headers.append('Content-Type', 'application/json');
	    return headers;
    }

    removeParticipantVehicle(id: number): Observable<Response> {  
		return this.authHttp
	  	.delete(this.baseUrl + `/participants/vehicles/${id}`, {headers: this.getHeaders()});
  	}
}

function handleError (error: any) {
	let errorMsg = error.message || `An error has occured.`
	console.error(errorMsg);
	return Observable.throw(errorMsg);
}
