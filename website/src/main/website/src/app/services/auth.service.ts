// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable }      from '@angular/core';
import { tokenNotExpired } from 'angular2-jwt';
import { Router }          from '@angular/router';
import { myConfig }        from '../auth.config';
import { environment } from '../../environments/environment';
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/filter';

let Auth0Lock: any = require('auth0-lock').default;

@Injectable()
export class Auth {


  lock = new Auth0Lock('p4aA5vgDwSKz39gvVFlxsrlxYnSfY5YM', 'cvrm-its-dot.auth0.com',  {
   allowSignUp: false, 
   configurationBaseUrl: 'https://cdn.auth0.com',  
   auth: {
     autoParseHash: false,
     params: {
       scope: 'openid roles'
     } 
   },
    languageDictionary: {      
        title: "Resource Manager",
        emailInputPlaceholder: "email",
        passwordInputPlaceholder: "password",
    },
    theme: {      
      logo: "./WYDOTCVP_logo_transparent.png",
      primaryColor: "#17314C",
    }
  });

  userProfile: any;  

  constructor(private router: Router) {
    // Set userProfile attribute of already saved profile
    this.userProfile = JSON.parse(localStorage.getItem('profile'));   
    // Add callback for the Lock `authenticated` event
    this.lock.on("authenticated", (authResult) => {
      
      // Fetch profile information
      this.lock.getUserInfo(authResult.accessToken, (error, profile) => {
        if (error) {
          // Handle error
          console.log(error);
          return;
        }        
        localStorage.setItem('id_token', authResult.idToken);
        localStorage.setItem('access_token', authResult.accessToken);
        localStorage.setItem('profile', JSON.stringify(profile));
        this.userProfile = profile;

        console.log("app meta: " + profile.app_metadata);
        console.log("access: " + authResult.accessToken);
        console.log("id: " + authResult.idToken);

        this.userProfile = profile;
        this.router.navigate(['/dashboard']);
      });
    });    
  }
 
  public getProfile(): Object{
    return JSON.parse(localStorage.getItem('profile')); 
  }


  public login() {
    // Call the show method to display the widget.
    this.lock.show();
  }

  public handleAuthenticationWithHash(){
      this
      .router
      .events
      .filter(event => event.constructor.name === 'NavigationStart')
      .filter(event => (/access_token|id_token|error/).test(event.url))
      .subscribe(event => {
        this.lock.resumeAuth(window.location.hash, (error, authResult) => {
          if (error) return console.log(error);
          this.setUser(authResult);
          //this.router.navigate(['/dashboard']);
        });
    });
  }

  public authenticated(): boolean {
    // Check whether the id_token is expired or not
    return tokenNotExpired();
  }

  public logout(): void {
    // Remove token from localStorage
    localStorage.removeItem('access_token');
    localStorage.removeItem('id_token');
    localStorage.removeItem('profile');
    this.userProfile = undefined;
    //this.router.navigate(['/login']);
  }

  private setUser(authResult): void {
    localStorage.setItem('access_token', authResult.accessToken);
    localStorage.setItem('id_token', authResult.idToken);
    this.lock.getProfile(authResult.idToken, (error, profile) => {
      if (error) {
        // Handle error
        alert(error);
        return;
      }

      localStorage.setItem('profile', JSON.stringify(profile));
      this.userProfile = profile;
    });
  }

  //   Role - Super User
  // Full access to all features of the site, should only be used for Developers/troubleshooting issues
  // Access to User Administration
  public isAdmin() {
    return this.userProfile && this.userProfile.app_metadata
      && this.userProfile.app_metadata.roles
      && this.userProfile.app_metadata.roles.indexOf('ROLE_ADMIN') > -1;
  }

  // Role - Trainer
  //   Full access to Participant Names
  //   Ability to Add/Edit/Delete Participants
  //   Ability to associate participants to vehicles
  //   Ability to view reports for participants and vehicles
  //   Read only for vehicles/equipment/components
  //   View all reports
  public isTrainer() {
    return this.userProfile && this.userProfile.app_metadata
      && this.userProfile.app_metadata.roles
      && this.userProfile.app_metadata.roles.indexOf('ROLE_TRAINING') > -1;
  }

  // Role - Equipment Admins
  //   View obfuscated participant information
  //   View obfuscated vehicle information
  //   Add/Edit/Delete Equipment
  //   Add/Edit Delete Components
  //   Ability to view obfuscated reports for Equipment/Components
  //   Read only for participants/vehicles
  //   View all reports
  public isEquipmentAdmin() {
    return this.userProfile && this.userProfile.app_metadata
      && this.userProfile.app_metadata.roles
      && this.userProfile.app_metadata.roles.indexOf('ROLE_EQUIPMENT') > -1;
  }

  // Role - Vehicle Manager
  //   Add/Edit/Delete Vehicles
  //   View raw vehicle data
  //   Associate Equipment to Vehicles
  //   View Vehicle reports.
  //   Read only for Participants
  //   View all reports
  public isVehicleManager() {
    return this.userProfile && this.userProfile.app_metadata
      && this.userProfile.app_metadata.roles
      && this.userProfile.app_metadata.roles.indexOf('ROLE_VEHICLE') > -1;
  }

  // Role - Read Only
  //   View all obfuscated data
  //   View all reports
  public isReadOnly() {
    return this.userProfile && this.userProfile.app_metadata
      && this.userProfile.app_metadata.roles
      && this.userProfile.app_metadata.roles.indexOf('ROLE_READ') > -1;
  }

  public hasRole() {
    return this.userProfile && this.userProfile.app_metadata
      && this.userProfile.app_metadata.roles
      && this.userProfile.app_metadata.roles.length > 0;
  }

  public canEditParticipantData() {
    return this.isTrainer() || this.isAdmin();
  }

  public canEditEquipmentData() {
    return this.isEquipmentAdmin() || this.isAdmin();
  }

  public canEditVehicleData() {
   return this.isVehicleManager() || this.isAdmin();
  }

}
