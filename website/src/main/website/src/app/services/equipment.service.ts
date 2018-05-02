// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Equipment } from '../classes/equipment';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class EquipmentService{

  private baseUrl: string;

  constructor(private authHttp: AuthHttp){ 
    this.baseUrl = environment.baseUrl + "/cvpt";
  }

  getAll(): Observable<Equipment[]>{
    let equipment$ = this.authHttp
         .get(this.baseUrl + '/equipment', {headers: this.getHeaders()})
         .map((res:Response) => res.json())
         .catch(handleError);
    return equipment$;
  }

  getEquipment(id: number): Observable<Equipment>{
    let equipment$ = this.authHttp
         .get(this.baseUrl + '/equipment/' + id, {headers: this.getHeaders()})
         .map((res:Response) => res.json())
         .catch(handleError);
    return equipment$;
  }

  getAvailableEquipment(id: number): Observable<Equipment[]>{
    let equipment$ = this.authHttp
    .get(this.baseUrl + '/vehicles/' + id + '/availableequipment', {headers: this.getHeaders()})
    .map((res:Response) => res.json())
    .catch(handleError);
    return equipment$;
  }

  add(equipment: Equipment) : Observable<Response>{   
    return this.authHttp
      .post(`${this.baseUrl}/equipment`, JSON.stringify(equipment), {headers: this.getHeaders()});
  }

  update(equipment: Equipment) : Observable<Response>{   
    return this.authHttp
      .put(`${this.baseUrl}/equipment/${equipment.equipmentId}`, JSON.stringify(equipment), {headers: this.getHeaders()});
  }

  delete(id: number): Observable<Response> {  
   return this.authHttp
      .delete(this.baseUrl + '/equipment/' + id, {headers: this.getHeaders()});
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
