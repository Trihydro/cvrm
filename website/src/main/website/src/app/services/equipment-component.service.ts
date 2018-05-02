// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { EquipmentComp } from '../classes/equipment-component';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class EquipmentComponentService{

 
  private baseUrl: string;
  private headers = new Headers({'Content-Type': 'application/json'});
  
  constructor(private authHttp: AuthHttp){ 
    this.baseUrl = environment.baseUrl + "/cvpt";
  }

  getAll(id: number): Observable<EquipmentComp[]>{
    let equipmentComponents$ = this.authHttp
         .get(this.baseUrl + '/equipment/' + id + "/components", {headers: this.getHeaders()})
         .map((res:Response) => res.json())
         .catch(handleError);
    return equipmentComponents$;
  }

  getEquipmentComponent(id: number): Observable<EquipmentComp>{
    let equipment$ = this.authHttp
         .get(this.baseUrl + '/equipment/components/' + id, {headers: this.getHeaders()})
         .map((res:Response) => res.json())
         .catch(handleError);
    return equipment$;
  }

  save(equipmentComponent: EquipmentComp) : Observable<Response>{   
    return this.authHttp
      .post(`${this.baseUrl}/equipment/${equipmentComponent.equipmentId}/components`, JSON.stringify(equipmentComponent), {headers: this.headers});
  }

  update(equipmentComponent: EquipmentComp) : Observable<Response>{   
    return this.authHttp
      .put(`${this.baseUrl}/equipment/${equipmentComponent.equipmentId}/components`, JSON.stringify(equipmentComponent), {headers: this.headers});
  }

  delete(id: number): Observable<Response> {  
   return this.authHttp
      .delete(this.baseUrl + '/equipment/components/' + id, {headers: this.headers});
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
