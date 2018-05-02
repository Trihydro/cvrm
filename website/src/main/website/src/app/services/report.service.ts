// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { VehicleCount } from '../classes/vehicle-count';
import { EquipmentDeployed } from '../classes/equipment-deployed';
import { ParticipantsTrained } from '../classes/participants-trained';
import { environment } from '../../environments/environment';
import { AuthHttp } from 'angular2-jwt';

@Injectable()
export class ReportService{
  
  private baseUrl: string;

  constructor(private authHttp: AuthHttp){ 
    console.log("env name: " + environment.envName);
    console.log("env: " + environment.baseUrl);
    this.baseUrl = environment.baseUrl + "/cvpt";
  }

  getEquipmentDeployed(): Observable<EquipmentDeployed[]>{
    let equipmentDeployed$ = this.authHttp
      .get(`${this.baseUrl}/reports/equipmentdeployed`, {headers: this.getHeaders()})
      .map((res:Response) => res.json())
      .catch(handleError);
      return equipmentDeployed$;
  }  

  getVehicleOrganizations(): Observable<VehicleCount[]>{
    let vehicleOrganizations$ = this.authHttp
      .get(`${this.baseUrl}/reports/vehicleorganizations`, {headers: this.getHeaders()})
      .map((res:Response) => res.json())
      .catch(handleError);
      return vehicleOrganizations$;
  }

   getParticipantsTrained(): Observable<ParticipantsTrained[]>{
    let participantsTrained$ = this.authHttp
      .get(`${this.baseUrl}/reports/participanttraining`, {headers: this.getHeaders()})
      .map((res:Response) => res.json())
      .catch(handleError);
      return participantsTrained$;
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
