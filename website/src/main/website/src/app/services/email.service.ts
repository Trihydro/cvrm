// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Http, Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';
import { Email } from '../classes/email';

@Injectable()
export class EmailService{
    
  private baseUrl: string;

  constructor(private authHttp : AuthHttp){ 
    this.baseUrl = environment.baseUrl + "/cvpt";
  }

  sendEmail(email: Email): Observable<Response>{
      console.log(email);
      return this.authHttp
        .post(`${this.baseUrl}/sendEmail`, JSON.stringify(email), {headers: this.getHeaders()});
  }  

  private getHeaders(){
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-type', 'application/json');
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
