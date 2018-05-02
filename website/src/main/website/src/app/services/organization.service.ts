// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Organization } from '../classes/organization';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class OrganizationService{
  
  private baseUrl: string;

  constructor(private authHttp: AuthHttp){ 
    this.baseUrl = environment.baseUrl + "/cvpt";
  }

  getAll(): Observable<Organization[]>{
    let organizations$ = this.authHttp
      .get(this.baseUrl + '/organizations', {headers: this.getHeaders()})
      .map((res:Response) => res.json())
      .catch(handleError);
      return organizations$;
  }  

  private getHeaders(){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return headers;
  }

  add(organization: Organization) : Observable<Response>{   
    console.log(organization);
    return this.authHttp
    .post(`${this.baseUrl}/organizations`, JSON.stringify(organization), {headers: this.getHeaders()});
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
