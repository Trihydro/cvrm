// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { EquipmentComponentType } from '../classes/equipment-component-type';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class EquipmentComponentTypeService{
 
  private baseUrl: string;

  constructor(private authHttp: AuthHttp){ 
    this.baseUrl = environment.baseUrl + "/cvpt";
  }

  getAll(): Observable<EquipmentComponentType[]>{
    let equipmentCompTypes$ = this.authHttp
      .get(this.baseUrl + '/equipment/componenttypes', {headers: this.getHeaders()})
      .map((res:Response) => res.json())
      .catch(handleError);
      return equipmentCompTypes$;
  }  

  private getHeaders(){
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    return headers;
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
