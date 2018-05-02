// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { ParticipantTraining } from '../classes/participant-training';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class ParticipantTrainingService{

	private baseUrl: string;

	constructor(private authHttp: AuthHttp){ 
		this.baseUrl = environment.baseUrl + "/cvpt";
	}

    addParticipantTraining(pt: ParticipantTraining) : Observable<Response>{   
    	return this.authHttp
      	.post(`${this.baseUrl}/participants/${pt.participantId}/trainings`, JSON.stringify(pt), {headers: this.getHeaders()});
    }

    updateParticipantTraining(pt: ParticipantTraining) : Observable<Response>{   
    	return this.authHttp
      	.put(`${this.baseUrl}/participants/trainings/${pt.participantTrainingId}`, JSON.stringify(pt), {headers: this.getHeaders()});
    }

    getParticipantTraining(id: number): Observable<ParticipantTraining[]>{
		let participantTraining$ = this.authHttp
		.get(this.baseUrl + `/participants/${id}/trainings`, {headers: this.getHeaders()})
		.map((res:Response) => res.json())
		.catch(handleError);
		return participantTraining$;
    }

    private getHeaders(){
	    let headers = new Headers();
	    headers.append('Content-Type', 'application/json');
	    return headers;
    }

    removeParticipantTraining(id: number): Observable<Response> {  
		return this.authHttp
	  	.delete(this.baseUrl + `/participants/trainings/${id}`, {headers: this.getHeaders()});
  	}
}

function handleError (error: any) {
	let errorMsg = error.message || `An error has occured.`
	console.error(errorMsg);
	return Observable.throw(errorMsg);
}
