// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { FormsModule }   from '@angular/forms';
import { ChartModule } from 'angular2-highcharts';

import { AppComponent }  from './app.component';
import { NavComponent }    from './nav/nav.component'; 
import { DashboardComponent }    from './pages/dashboard/dashboard.component'; 
import { EquipmentComponent }    from './pages/equipment/equipment.component'; 
import { ParticipantComponent }    from './pages/participant/participant.component'; 
import { VehicleComponent }    from './pages/vehicle/vehicle.component'; 
import { TrainingComponent }    from './pages/training/training.component'; 
import { TrainingDetailComponent }    from './pages/training/training-detail.component'; 
import { EquipmentDetailComponent }    from './pages/equipment/equipment-detail.component'; 
import { EquipmentComponentDetailComponent }    from './pages/equipment/equipment-component-detail.component'; 
import { EquipmentComponentListComponent }    from './pages/equipment/equipment-component-list.component'; 
import { ParticipantDetailComponent }    from './pages/participant/participant-detail.component';
import { OrganizationDetailComponent }    from './pages/organization/organization-detail.component'; 
import { VehicleDetailComponent }    from './pages/vehicle/vehicle-detail.component'; 
import { ParticipantVehicleAvailableListComponent }        from './pages/participant/participant-vehicle-available-list.component';
import { ParticipantTrainingAvailableListComponent }        from './pages/participant/participant-training-available-list.component';
import { ParticipantVehicleComponent }        from './pages/participant/participant-vehicle.component';
import { ParticipantTrainingComponent }        from './pages/participant/participant-training.component';
import { ParticipantFormComponent }        from './pages/participant/participant-form.component';
import { VehicleEquipmentAvailableListComponent }        from './pages/vehicle/vehicle-equipment-available-list.component';
import { VehicleEquipmentComponent }        from './pages/vehicle/vehicle-equipment.component';
import { ManageComponent }        from './pages/manage/manage.component';
import { LoginComponent }        from './pages/account/login.component';
import { SetPasswordComponent }        from './pages/account/set-password.component';
import { SettingsComponent }        from './pages/manage/settings.component';
import { AdminComponent }        from './pages/manage/admin.component';

import { AppRoutingModule }     from './app-routing.module';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { DataTableModule } from "angular2-datatable";
import { AuthGuard } from "./services/auth-guard.service";
import { Auth } from './services/auth.service';
import { Http, RequestOptions } from '@angular/http';
import { AuthHttp, AuthConfig } from 'angular2-jwt';

export function authHttpServiceFactory(http: Http, options: RequestOptions) {
  return new AuthHttp( new AuthConfig({}), http, options);
}


@NgModule({
  imports:      [ BrowserModule,  AppRoutingModule, HttpModule, FormsModule, DataTableModule, ChartModule],  
  declarations: [ AppComponent, NavComponent, ParticipantComponent, DashboardComponent, EquipmentComponent, VehicleComponent, 
  TrainingComponent, EquipmentDetailComponent, EquipmentComponentDetailComponent, ParticipantDetailComponent, OrganizationDetailComponent,
  VehicleDetailComponent, ParticipantVehicleAvailableListComponent, ParticipantVehicleComponent, ParticipantFormComponent, 
  TrainingDetailComponent, VehicleEquipmentAvailableListComponent, VehicleEquipmentComponent, ParticipantTrainingComponent,
  ParticipantTrainingAvailableListComponent, ManageComponent, LoginComponent, SettingsComponent, AdminComponent,
  EquipmentComponentListComponent, SetPasswordComponent],
  bootstrap:    [ AppComponent ],
  providers: [ 
   {
      provide: AuthHttp,
      useFactory: authHttpServiceFactory,
      deps: [ Http, RequestOptions ]
    },
      Auth,
  		AuthGuard,      
		{
			provide: LocationStrategy, 
			useClass: HashLocationStrategy
		}
	]
})
export class AppModule { }
