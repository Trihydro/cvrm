// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Vehicle } from '../classes/vehicle';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class VehicleService{
    
  private baseUrl: string;

  constructor(private authHttp : AuthHttp){ 
    this.baseUrl = environment.baseUrl + "/cvpt";
  }

  getAll(): Observable<Vehicle[]>{
    let vehicles$ = this.authHttp
      .get(this.baseUrl + '/vehicles', {headers: this.getHeaders()})
      .map((res:Response) => res.json())
      .catch(handleError);
      return vehicles$;
  }  

  getVehicle(id: number): Observable<Vehicle>{
    let vehicle$ = this.authHttp
    .get(this.baseUrl + '/vehicles/' + id, {headers: this.getHeaders()})
    .map((res:Response) => res.json())
    .catch(handleError);
    return vehicle$;
  }

  getAvailableVehicles(id: number): Observable<Vehicle[]>{
    let vehicle$ = this.authHttp
    .get(this.baseUrl + '/participants/' + id + '/availablevehicles', {headers: this.getHeaders()})
    .map((res:Response) => res.json())
    .catch(handleError);
    return vehicle$;
  }

  private getHeaders(){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return headers;
  }

  add(vehicle: Vehicle) : Observable<Response>{   
    return this.authHttp
    .post(`${this.baseUrl}/vehicles`, JSON.stringify(vehicle), {headers: this.getHeaders()});
  }

  update(vehicle: Vehicle) : Observable<Response>{ 
    return this.authHttp
    .put(`${this.baseUrl}/vehicles/${vehicle.vehicleId}`, JSON.stringify(vehicle), {headers: this.getHeaders()});
  }

  delete(id: number): Observable<Response> {  
    return this.authHttp
    .delete(this.baseUrl + '/vehicles/' + id, {headers: this.getHeaders()});
  }
}

function handleError (error: any) {
	// log error
	// could be something more sofisticated
	let errorMsg = error.message || `An error has occured.`
	console.error(errorMsg);

	// throw an application level error
	return Observable.throw(errorMsg);
}
