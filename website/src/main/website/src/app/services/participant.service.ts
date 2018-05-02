// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Participant } from '../classes/participant';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class ParticipantService{

	private baseUrl: string;

	constructor(private authHttp: AuthHttp){ 
    	this.baseUrl = environment.baseUrl + "/cvpt";
  	}

	getAll(): Observable<Participant[]>{
		let participants$ = this.authHttp
		.get(this.baseUrl + '/participants', {headers: this.getHeaders()})
		.map((res:Response) => res.json())
		.catch(handleError);
		return participants$;
    }

 	getParticipant(id: number): Observable<Participant>{
    	let participant$ = this.authHttp
			.get(this.baseUrl + '/participants/' + id, {headers: this.getHeaders()})
			.map((res:Response) => res.json())
			.catch(handleError);
    	return participant$;
  	}
  	
    private getHeaders(){
	    let headers = new Headers();
	    headers.append('Content-Type', 'application/json');
	    return headers;
    }

    add(participant: Participant) : Observable<Response>{   
    	return this.authHttp
      	.post(`${this.baseUrl}/participants`, JSON.stringify(participant), {headers: this.getHeaders()});
    }

	  update(participant: Participant) : Observable<Response>{ 
    	return this.authHttp
      	.put(`${this.baseUrl}/participants/${participant.participantId}`, JSON.stringify(participant), {headers: this.getHeaders()});
    }

    delete(id: number): Observable<Response> {  
   		return this.authHttp
      	.delete(this.baseUrl + '/participants/' + id, {headers: this.getHeaders()});
  	}  	
}

function handleError (error: any) {
	let errorMsg = error.message || `An error has occured.`
	console.error(errorMsg);
	return Observable.throw(errorMsg);
}
