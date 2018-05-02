// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { NgModule }             from '@angular/core';
import { RouterModule, Routes, CanActivate } from '@angular/router';
import { AuthGuard } from './services/auth-guard.service';

import { ParticipantComponent }		from './pages/participant/participant.component'; 
import { DashboardComponent }		from './pages/dashboard/dashboard.component'; 
import { EquipmentComponent }		from './pages/equipment/equipment.component'; 
import { VehicleComponent }		from './pages/vehicle/vehicle.component'; 
import { TrainingComponent }		from './pages/training/training.component'; 
import { EquipmentDetailComponent }		from './pages/equipment/equipment-detail.component'; 
import { EquipmentComponentDetailComponent } from './pages/equipment/equipment-component-detail.component'; 
import { ParticipantDetailComponent }		from './pages/participant/participant-detail.component'; 
import { OrganizationDetailComponent }		from './pages/organization/organization-detail.component'; 
import { VehicleDetailComponent }		from './pages/vehicle/vehicle-detail.component'; 
import { ParticipantVehicleAvailableListComponent }        from './pages/participant/participant-vehicle-available-list.component';
import { ParticipantTrainingAvailableListComponent }        from './pages/participant/participant-training-available-list.component';
import { VehicleEquipmentAvailableListComponent }        from './pages/vehicle/vehicle-equipment-available-list.component';
import { TrainingDetailComponent }        from './pages/training/training-detail.component'; 
import { ManageComponent }        from './pages/manage/manage.component';
import { LoginComponent }     from './pages/account/login.component';
import { SetPasswordComponent }     from './pages/account/set-password.component';

const routes: Routes = [
	{ path: '', redirectTo: '/dashboard', pathMatch: 'full', canActivate: [AuthGuard] },
	{ path: 'dashboard',  component: DashboardComponent, canActivate: [AuthGuard] },
	{ path: 'participants',     component: ParticipantComponent },
	{ path: 'equipment',     component: EquipmentComponent },
	{ path: 'vehicles',     component: VehicleComponent },
	{ path: 'training',     component: TrainingComponent },
	{ path: 'equipmentDetail', component: EquipmentDetailComponent },
	{ path: 'equipmentDetail/:id', component: EquipmentDetailComponent },
    { path: 'equipmentComponentDetail', component: EquipmentComponentDetailComponent },
    { path: 'equipmentComponentDetail/:equipmentId', component: EquipmentComponentDetailComponent },
    { path: 'equipmentComponentDetail/:equipmentId/:equipmentComponentId', component: EquipmentComponentDetailComponent },
    { path: 'participantDetail', component: ParticipantDetailComponent },
    { path: 'participantDetail/:id', component: ParticipantDetailComponent },
    { path: 'participantDetail/:id/:tab', component: ParticipantDetailComponent },
    { path: 'organizationDetail', component: OrganizationDetailComponent },
    { path: 'vehicleDetail', component: VehicleDetailComponent },
    { path: 'vehicleDetail/:id', component: VehicleDetailComponent },
    { path: 'participantVehicleAvailableList/:id', component: ParticipantVehicleAvailableListComponent },
    { path: 'participantTrainingAvailableList/:id', component: ParticipantTrainingAvailableListComponent },
    { path: 'trainingDetail', component: TrainingDetailComponent },
    { path: 'trainingDetail/:id', component: TrainingDetailComponent },
    { path: 'vehicleEquipmentAvailableList/:id', component: VehicleEquipmentAvailableListComponent },
    { path: 'vehicleDetail/:id/:tab', component: VehicleDetailComponent },
    { path: 'manage', component: ManageComponent, canActivate: [AuthGuard] },
    { path: 'login', component: LoginComponent },
    { path: 'setpassword', component: SetPasswordComponent },
    { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
