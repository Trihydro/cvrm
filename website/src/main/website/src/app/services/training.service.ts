// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Training } from '../classes/training';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class TrainingService{

  private baseUrl: string;

  constructor(private authHttp : AuthHttp){ 
    this.baseUrl = environment.baseUrl + "/cvpt";
  }

  getAll(): Observable<Training[]>{
    let training$ = this.authHttp
         .get(this.baseUrl + '/trainings', {headers: this.getHeaders()})
         .map((res:Response) => res.json())
         .catch(handleError);
    return training$;
  }

  getTraining(id: number): Observable<Training>{
    let training$ = this.authHttp
         .get(this.baseUrl + '/trainings/' + id, {headers: this.getHeaders()})
         .map((res:Response) => res.json())
         .catch(handleError);
    return training$;
  }

  getAvailableTraining(id: number): Observable<Training[]>{
    let training$ = this.authHttp
    .get(this.baseUrl + '/participants/' + id + '/availabletrainings', {headers: this.getHeaders()})
    .map((res:Response) => res.json())
    .catch(handleError);
    return training$;
  }

  add(training: Training) : Observable<Response>{   
    return this.authHttp
      .post(`${this.baseUrl}/trainings`, JSON.stringify(training), {headers: this.getHeaders()});
  }

  update(training: Training) : Observable<Response>{   
    return this.authHttp
      .put(`${this.baseUrl}/trainings/${training.trainingId}`, JSON.stringify(training), {headers: this.getHeaders()});
  }

   delete(id: number): Observable<Response> {  
    return this.authHttp
       .delete(this.baseUrl + '/trainings/' + id, {headers: this.getHeaders()});
   }

  private getHeaders(){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return headers;
  }
}

function handleError (error: any) {
	// log error
	let errorMsg = error.message || `An error has occured.`
	console.error(errorMsg);

	// throw an application level error
	return Observable.throw(errorMsg);
}
